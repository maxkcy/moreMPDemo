package com.max.myfirstmpdemo.headless;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Queue;
import com.dongbat.jbump.Item;
import com.github.czyzby.websocket.serialization.Transferable;
import com.max.myfirstmpdemo.Packets.CountDownPacket;
import com.max.myfirstmpdemo.Packets.RoomEnum;
import com.max.myfirstmpdemo.Packets.RoomPacket;
import com.max.myfirstmpdemo.Packets.TouchDownPacket;
import com.max.myfirstmpdemo.headless.Entities.Entity;
import com.max.myfirstmpdemo.headless.Entities.PlayerEntity;

import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import jdk.nashorn.internal.ir.CallNode;

public class GameRoom extends ScreenAdapter {
    public ServerMain serverMain;
    public Array<ServerWebSocket> playersList;
    public float time;// = 300.0f;
    public boolean isActive;// = true;
    public GameWorld gameWorld;

    public ConcurrentLinkedQueue<Transferable> packetQueue = new ConcurrentLinkedQueue<Transferable>();
    public Array<Transferable> workList = new Array();
    Transferable element;


    //public CountDownPacket countDownPacket = new CountDownPacket(300.0f);
    //for testies and this is demo im going to create a pool to keep pooling fresh in my memory
    Pool<CountDownPacket> countDownPacketPool;// = new Pool<CountDownPacket>() {
    Pool<RoomPacket> roomPacketPool;

    public GameRoom(ServerMain serverMain) {
        this.serverMain = serverMain;
        playersList = new Array<>();
        System.out.println("New GameRoom initiated");
    }


    @Override
    public void show() {

        time = 60.0f;
        isActive = true;

        countDownPacketPool = new Pool<CountDownPacket>() {
            @Override
            protected CountDownPacket newObject() {
                return new CountDownPacket();
            }

            @Override
            protected void reset(CountDownPacket object) {
                super.reset(object);
                object.setTime(time);
            }
        };

        roomPacketPool = new Pool<RoomPacket>() {
            @Override
            protected RoomPacket newObject() {
                return new RoomPacket();
            }
        };
        //System.out.println("The GameRoom has players: " + playersList);
        gameWorld = new GameWorld(playersList);
        gameWorld.initGameWorld();
        //last edits you do, send packet to switch to room screen here and change countdown packet logic
    }

    public int cap = 20000;

    @Override
    public void render(float delta) {
        //super.render(delta);
        if (isActive) {
            gameWorld.update();

            for (ServerWebSocket serverWebSocket :
                    playersList) {
                CountDownPacket countDownPacket = countDownPacketPool.obtain();
                //CountDownPacket countDownPacket = new CountDownPacket();
                countDownPacket.setTime(time);
                //System.out.println(countDownPacket.getTime());
                serverWebSocket.writeFinalBinaryFrame(Buffer.buffer(serverMain.manualSerializer.serialize(countDownPacket)));
                //Gdx.app.log(this.toString(), "CountDownPacket w/ time: " + countDownPacket.getTime() + " sent to: " + serverWebSocket);
                countDownPacketPool.free(countDownPacket);
            }
            //System.out.println("Countdown packets sent");
            //System.out.println(time);
        }

        if (time <= 0.0f || playersList.isEmpty()) {
            if (isActive == true) {
                System.out.println("Game Room has ended");
                isActive = false;
                for (ServerWebSocket serverWebSocket :
                        playersList) {
                    RoomPacket roomPacket = roomPacketPool.obtain();
                    roomPacket.roomEnum = RoomEnum.MPHOMELOBBY;
                    serverWebSocket.writeFinalBinaryFrame(Buffer.buffer(serverMain.manualSerializer.serialize(roomPacket)));
                    roomPacketPool.free(roomPacket);
                }

            }
        }
        time = time - delta;


        for (int i = 0; i <= cap && (element = packetQueue.poll()) != null; i++) {
            workList.add(element);
        }

        for (Transferable elemento : workList) {

            if (elemento instanceof TouchDownPacket) {
                Gdx.app.log(this.toString(), "TouchDownPacket from: " + ((TouchDownPacket) elemento).getServerWebSocket());
                ClientID clientID = ServerMain.clientHash.get(((TouchDownPacket) elemento).getServerWebSocket());
                float angle = MathUtils.atan2(((TouchDownPacket) elemento).getY() - clientID.getClientPlayerItem().userData.position.y,
                        ((TouchDownPacket) elemento).getX() - clientID.getClientPlayerItem().userData.position.x)
                        * MathUtils.radiansToDegrees;
                angle = (((angle % 360) + 360) % 360);

                float newX;
                float newY;

                if (((TouchDownPacket) elemento).getX() - clientID.getClientPlayerItem().userData.position.x < -5 ||
                        ((TouchDownPacket) elemento).getX() - clientID.getClientPlayerItem().userData.position.x > 5) {
                    newX = clientID.getClientPlayerItem().userData.position.x + MathUtils.cosDeg(angle) * 5;
                } else {
                    newX = ((TouchDownPacket) elemento).getX();
                }

                if (((TouchDownPacket) elemento).getY() - clientID.getClientPlayerItem().userData.position.y < -5 ||
                        ((TouchDownPacket) elemento).getY() - clientID.getClientPlayerItem().userData.position.y > 5) {
                    newY = clientID.getClientPlayerItem().userData.position.y + MathUtils.sinDeg(angle) * 5;
                } else {
                    newY = ((TouchDownPacket) elemento).getY();
                }

                clientID.getClientGameRoom().gameWorld.world.move(clientID.getClientPlayerItem(), newX, newY,
                        ((PlayerEntity) clientID.getClientPlayerItem().userData).collisionFilter);

                if (((PlayerEntity) clientID.getClientPlayerItem().userData).touchedBall == true){

                    (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity).world.move(((PlayerEntity) clientID.getClientPlayerItem().userData).worldBallItem,
                            (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity.position.x) + (MathUtils.cosDeg(angle) * 2),
                            (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity.position.y) + (MathUtils.sinDeg(angle) * 2), ((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity.collisionFilter);
                }


            }
        }
    workList.clear();
    }

        @Override
        public void dispose(){
            super.dispose();
            countDownPacketPool.clear();
            roomPacketPool.clear();
        }
    }

