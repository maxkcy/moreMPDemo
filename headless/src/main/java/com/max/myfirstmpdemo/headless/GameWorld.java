package com.max.myfirstmpdemo.headless;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.max.myfirstmpdemo.Packets.BlueShirtInitPacket;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;
import com.max.myfirstmpdemo.headless.Entities.BallEntity;
import com.max.myfirstmpdemo.headless.Entities.Entity;
import com.max.myfirstmpdemo.headless.Entities.PlayerEntity;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;

public class GameWorld {
    Array<ServerWebSocket> playersList;
   public Array<Item> playerItemListTeamRed;
   public Array<Item> playerItemListTeamBlue;

    public GameWorld(Array<ServerWebSocket> playersList) {
        this.playersList = playersList;
        this.initGameWorld();
    }

    World<Entity> world;
    BallEntity ballEntity;
    Item<Entity> ballItem;
    Pool<RedShirtInitPacket> redShirtInitPacketPool;
    Pool<BlueShirtInitPacket> blueShirtInitPacketPool;

    public void initGameWorld() {

        world = new World<>(4);
        ballEntity = new BallEntity();
        ballItem = new Item<Entity>(ballEntity);
        playerItemListTeamRed = new Array<>();
        playerItemListTeamBlue = new Array<>();
        world.add(ballItem, ballItem.userData.position.x, ballItem.userData.position.y, ballItem.userData.width, ballItem.userData.height);
        redShirtInitPacketPool = new Pool<RedShirtInitPacket>() {
            @Override
            protected RedShirtInitPacket newObject() {
                return new RedShirtInitPacket();
            }
        };
        intiTeamRed();

        blueShirtInitPacketPool = new Pool<BlueShirtInitPacket>() {
            @Override
            protected BlueShirtInitPacket newObject() {
                return new BlueShirtInitPacket();
            }
        };
        initTeamBlue();
        //send packet, client will assign ARRAYLIST.
        //^nope this is wrong... this whole page is wrong. redo it
    }

    public void update() {
        boolean staticStateSent;
        boolean idleStateSent;
        if (ballItem.userData.position.x != world.getRect(ballItem).x || ballItem.userData.position.y != world.getRect(ballItem).y) {
            float angle;
            angle = MathUtils.atan2(world.getRect(ballItem).y - ballItem.userData.position.y, ballItem.userData.position.x - ballItem.userData.position.x)
                    * MathUtils.radiansToDegrees;
            angle = (((angle % 360) + 360) % 360);
            ballItem.userData.position.x = world.getRect(ballItem).x;
            ballItem.userData.position.y = world.getRect(ballItem).y;

            ((BallEntity) ballItem.userData).state = BallEntity.States.MOVING;
            //Send BallPosition Packet, with position, angle, and enum state from a pool of ball position packets.
            staticStateSent = false;
        } else {
            if (staticStateSent = false) {
                ((BallEntity) ballItem.userData).state = BallEntity.States.STATIC;
                //Send staticStateEnum Packet}
                staticStateSent = true;
            }

        }
        for (Item<Entity> playerItem : playerItemListTeamRed) {
            if (playerItem.userData.position.x!= world.getRect(playerItem).x || playerItem.userData.position.y != world.getRect(playerItem).y
            && ((PlayerEntity)playerItem.userData).state != PlayerEntity.States.Kicking){
                ((PlayerEntity)playerItem.userData).state = PlayerEntity.States.Running;
                for (ServerWebSocket player : playersList) {

                    //send player position packet Running
                    idleStateSent = false;
                    }

                playerItem.userData.position.x = world.getRect(playerItem).x;
                playerItem.userData.position.y = world.getRect(playerItem).y;
                } else {
                if (idleStateSent = false){
                    ((PlayerEntity) playerItem.userData).state = PlayerEntity.States.Idle;
                    //send packet idleStateTrue

                    }
                }
            }
        for (Item<Entity> playerItem : playerItemListTeamBlue) {
            if (playerItem.userData.position.x!= world.getRect(playerItem).x || playerItem.userData.position.y != world.getRect(playerItem).y
                    && ((PlayerEntity)playerItem.userData).state != PlayerEntity.States.Kicking){
                ((PlayerEntity)playerItem.userData).state = PlayerEntity.States.Running;
                for (ServerWebSocket player : playersList) {

                    //send player position packet Running
                    idleStateSent = false;
                }
            } else {
                if (idleStateSent = false){
                    ((PlayerEntity) playerItem.userData).state = PlayerEntity.States.Idle;
                    //send packet idleStateTrue
                    idleStateSent = true;
                }
            }
        }

    }
    public void intiTeamRed(){
        for (int i = 0; i < playersList.size; i = i + 2) {
            Item<Entity> playerItem = new Item<>(new PlayerEntity(100, 100 + 100 / 2 * i));
            ((PlayerEntity) playerItem.userData).startPos.x = playerItem.userData.position.x;
            ((PlayerEntity) playerItem.userData).startPos.y = playerItem.userData.position.y;
            ((PlayerEntity) playerItem.userData).playerSocket = playersList.get(i);
            ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).setClientPlayerItem(playerItem);
            ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).team = ClientID.Team.RED;
            playerItemListTeamRed.add(playerItem);
            world.add(playerItem, playerItem.userData.position.x, playerItem.userData.position.y, playerItem.userData.width, playerItem.userData.height);
            //ugh send an int packet called redshirts.
            for (ServerWebSocket client: playersList) {
                RedShirtInitPacket redShirtInitPacket = redShirtInitPacketPool.obtain();
                redShirtInitPacket.setIDKey(client.toString());
                client.writeFinalBinaryFrame((Buffer.buffer(ServerMain.manualSerializer.serialize(redShirtInitPacketPool.obtain()))));
                redShirtInitPacketPool.free(redShirtInitPacket);
            }
        }
    }
        public void initTeamBlue(){
            for (int i = 1; i < playersList.size; i = i + 2) {
                Item<Entity> playerItem = new Item<>(new PlayerEntity(300, 100 + 100 / 2 * i));
                ((PlayerEntity) playerItem.userData).startPos.x = playerItem.userData.position.x;
                ((PlayerEntity) playerItem.userData).startPos.y = playerItem.userData.position.y;
                ((PlayerEntity) playerItem.userData).playerSocket = playersList.get(i);
                ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).setClientPlayerItem(playerItem);
                ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).team = ClientID.Team.BLUE;
                playerItemListTeamBlue.add(playerItem);
                world.add(playerItem, playerItem.userData.position.x, playerItem.userData.position.y, playerItem.userData.width, playerItem.userData.height);
                for (ServerWebSocket client: playersList) {
                    BlueShirtInitPacket blueShirtInitPacket = blueShirtInitPacketPool.obtain();
                    blueShirtInitPacket.setIDKey(client.toString());
                    client.writeFinalBinaryFrame((Buffer.buffer(ServerMain.manualSerializer.serialize(blueShirtInitPacketPool.obtain()))));
                    blueShirtInitPacketPool.free(blueShirtInitPacket);
                }
            }
        }
}


