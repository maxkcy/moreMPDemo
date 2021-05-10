package com.max.myfirstmpdemo.headless;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.github.czyzby.websocket.serialization.Transferable;
import com.max.myfirstmpdemo.Packets.AsteroidStatePacket;
import com.max.myfirstmpdemo.Packets.BluePlayerStatePacket;
import com.max.myfirstmpdemo.Packets.BlueShirtInitPacket;
import com.max.myfirstmpdemo.Packets.RedPlayerStatePacket;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;
import com.max.myfirstmpdemo.Packets.TouchDownPacket;
import com.max.myfirstmpdemo.headless.Entities.BallEntity;
import com.max.myfirstmpdemo.headless.Entities.Entity;
import com.max.myfirstmpdemo.headless.Entities.PlayerEntity;

import java.util.concurrent.ConcurrentLinkedQueue;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;

public class GameWorld {
    Array<ServerWebSocket> playersList;
   public Array<Item> playerItemListTeamRed;
   public Array<Item> playerItemListTeamBlue;

    public GameWorld(Array<ServerWebSocket> playersList) {
        this.playersList = playersList;
    }

    World<Entity> world;
    BallEntity ballEntity;
    Item<Entity> ballItem;
    AsteroidStatePacket asteroidStatePacket;
    Pool<RedShirtInitPacket> redShirtInitPacketPool;
    Pool<BlueShirtInitPacket> blueShirtInitPacketPool;
    Pool<RedPlayerStatePacket> redPlayerStatePacketPool;
    //RedPlayerStatePacket redPlayerStatePacket;
    BluePlayerStatePacket bluePlayerStatePacket;


    public void initGameWorld() {

        world = new World<>(4);
        ballEntity = new BallEntity(world);
        ballItem = new Item<Entity>(ballEntity);
        asteroidStatePacket = new AsteroidStatePacket();
        playerItemListTeamRed = new Array<>();
        playerItemListTeamBlue = new Array<>();
        ((BallEntity)ballItem.userData).resetPosition();
        ((BallEntity) ballItem.userData).state = BallEntity.States.STATIC;
        world.add(ballItem, ballItem.userData.position.x, ballItem.userData.position.y, ballItem.userData.width, ballItem.userData.height);
        redShirtInitPacketPool = new Pool<RedShirtInitPacket>() {
            @Override
            protected RedShirtInitPacket newObject() {
                return new RedShirtInitPacket();
            }
        };


        blueShirtInitPacketPool = new Pool<BlueShirtInitPacket>() {
            @Override
            protected BlueShirtInitPacket newObject() {
                return new BlueShirtInitPacket();
            }
        };

        redPlayerStatePacketPool = new Pool<RedPlayerStatePacket>() {
            @Override
            protected RedPlayerStatePacket newObject() {
                return new RedPlayerStatePacket();
            }
        };

        bluePlayerStatePacket = new BluePlayerStatePacket();

        intiTeamRed();
        initTeamBlue();

        //redPlayerStatePacket = new RedPlayerStatePacket();
        //send packet, client will assign ARRAYLIST.
        //^nope this is wrong... this whole page is wrong. redo it
    }

    public void update() {
        asteroidStatePacketMethod();
        redPlayerStatePacketMethod();
        bluePlayerStatePacketMethod();
        packetHandlingMethod();
        checkBallBoundaries();
    }

    public void intiTeamRed(){
        for (int i = 0; i < playersList.size; i = i + 2) {
            Item<Entity> playerItem = new Item<>(new PlayerEntity(100, 100 + 100 / 2 * i));
            Gdx.app.log(String.valueOf(this),playerItem.userData.position.x + " " + playerItem.userData.position.y);
            ((PlayerEntity) playerItem.userData).startPos.x = playerItem.userData.position.x;
            ((PlayerEntity) playerItem.userData).startPos.y = playerItem.userData.position.y;
            ((PlayerEntity) playerItem.userData).playerSocket = playersList.get(i);
            ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).setClientPlayerItem(playerItem);
            ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).setTeam(ClientID.Team.RED);
            playerItemListTeamRed.add(playerItem);
            world.add(playerItem, playerItem.userData.position.x, playerItem.userData.position.y, playerItem.userData.width, playerItem.userData.height);
            //ugh send an int packet called redshirts.
            for (ServerWebSocket client: playersList) {
                RedShirtInitPacket redShirtInitPacket = redShirtInitPacketPool.obtain();
                redShirtInitPacket.setIDKey(((PlayerEntity) playerItem.userData).playerSocket.toString());
                client.writeFinalBinaryFrame((Buffer.buffer(ServerMain.manualSerializer.serialize(redShirtInitPacket))));
                Gdx.app.log(this.toString(), "redShirtInitPacket Sent to: IDKey: " + client.toString());
                redShirtInitPacketPool.free(redShirtInitPacket);
            }
        }
    }
        public void initTeamBlue(){
            for (int i = 1; i < playersList.size; i = i + 2) {
                Item<Entity> playerItem = new Item<>(new PlayerEntity(300, 100 + 100 / 2 * (i - 1)));
                ((PlayerEntity) playerItem.userData).startPos.x = playerItem.userData.position.x;
                ((PlayerEntity) playerItem.userData).startPos.y = playerItem.userData.position.y;
                ((PlayerEntity) playerItem.userData).playerSocket = playersList.get(i);
                ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).setClientPlayerItem(playerItem);
                ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).setTeam(ClientID.Team.BLUE);
                playerItemListTeamBlue.add(playerItem);
                world.add(playerItem, playerItem.userData.position.x, playerItem.userData.position.y, playerItem.userData.width, playerItem.userData.height);
                for (ServerWebSocket client: playersList) {
                    BlueShirtInitPacket blueShirtInitPacket = blueShirtInitPacketPool.obtain();
                    blueShirtInitPacket.setIDKey(((PlayerEntity) playerItem.userData).playerSocket.toString());
                    client.writeFinalBinaryFrame((Buffer.buffer(ServerMain.manualSerializer.serialize(blueShirtInitPacket))));
                    Gdx.app.log(this.toString(), "blueShirtInitPacket Sent to: IDKey: " + client.toString());
                    blueShirtInitPacketPool.free(blueShirtInitPacket);
                }
            }
        }

    public void redPlayerStatePacketMethod(){
        for (Item<Entity> playerItem : playerItemListTeamRed) {
            RedPlayerStatePacket redPlayerStatePacket = redPlayerStatePacketPool.obtain();
            redPlayerStatePacket.setClientId(ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).playerID);

            if (playerItem.userData.position.x!= world.getRect(playerItem).x || playerItem.userData.position.y != world.getRect(playerItem).y
                    && ((PlayerEntity)playerItem.userData).state != PlayerEntity.States.Kicking){
                ((PlayerEntity)playerItem.userData).state = PlayerEntity.States.Running;

                redPlayerStatePacket.setState(RedPlayerStatePacket.States.running);
                redPlayerStatePacket.setPosition(world.getRect(playerItem).x, world.getRect(playerItem).y);

                for (ServerWebSocket player : playersList) {
                    //send player position packet Running
                    player.writeFinalBinaryFrame(
                            Buffer.buffer(ServerMain.manualSerializer.serialize(redPlayerStatePacket)));
                    Gdx.app.log(String.valueOf(this), "RedPlayerStatePacked running sent to " + player + " " + redPlayerStatePacket.x + " " + redPlayerStatePacket.y);
                }

                ((PlayerEntity)playerItem.userData).idleStateSent = false;
                ((PlayerEntity)playerItem.userData).kickingStateSent = false;
                playerItem.userData.position.x = world.getRect(playerItem).x;
                playerItem.userData.position.y = world.getRect(playerItem).y;
            } else {
                if (!((PlayerEntity) playerItem.userData).idleStateSent){
                    ((PlayerEntity) playerItem.userData).state = PlayerEntity.States.Idle;
                    //send packet idleState
                    redPlayerStatePacket.setState(RedPlayerStatePacket.States.idle);
                    redPlayerStatePacket.setPosition(world.getRect(playerItem).x, world.getRect(playerItem).y);

                    for (ServerWebSocket player : playersList) {
                        //send player position packet Idle
                        player.writeFinalBinaryFrame(
                                Buffer.buffer(ServerMain.manualSerializer.serialize(redPlayerStatePacket)));
                        Gdx.app.log(this.toString(),"redplaystatepacket idle (" + redPlayerStatePacket.getState() + ") sent w/ positions " + redPlayerStatePacket.x + " " + redPlayerStatePacket.y +"\n " +
                                "sent to:" + player);
                    }
                    ((PlayerEntity)playerItem.userData).idleStateSent = true;

                }
            }
            redPlayerStatePacketPool.free(redPlayerStatePacket);

        }
    }

    public void bluePlayerStatePacketMethod(){
        for (Item<Entity> playerItem : playerItemListTeamBlue) {

            bluePlayerStatePacket.setClientId(ServerMain.clientHash.get(((PlayerEntity) playerItem.userData).playerSocket).playerID);

            if (playerItem.userData.position.x!= world.getRect(playerItem).x || playerItem.userData.position.y != world.getRect(playerItem).y
                    && ((PlayerEntity)playerItem.userData).state != PlayerEntity.States.Kicking){
                ((PlayerEntity)playerItem.userData).state = PlayerEntity.States.Running;

                bluePlayerStatePacket.setState(BluePlayerStatePacket.States.running);
                bluePlayerStatePacket.setPosition(world.getRect(playerItem).x, world.getRect(playerItem).y);

                for (ServerWebSocket player : playersList) {
                    //send player position packet Running
                    player.writeFinalBinaryFrame(
                            Buffer.buffer(ServerMain.manualSerializer.serialize(bluePlayerStatePacket)));
                    Gdx.app.log(String.valueOf(this), "BluePlayerStatePacked running sent to " + player + " " + bluePlayerStatePacket.x + " " + bluePlayerStatePacket.y);
                }

                ((PlayerEntity)playerItem.userData).idleStateSent = false;
                ((PlayerEntity)playerItem.userData).kickingStateSent = false;
                playerItem.userData.position.x = world.getRect(playerItem).x;
                playerItem.userData.position.y = world.getRect(playerItem).y;
            } else {
                if (((PlayerEntity) playerItem.userData).idleStateSent == false){
                    ((PlayerEntity) playerItem.userData).state = PlayerEntity.States.Idle;
                    //send packet idleState
                    bluePlayerStatePacket.setState(BluePlayerStatePacket.States.idle);
                    bluePlayerStatePacket.setPosition(world.getRect(playerItem).x, world.getRect(playerItem).y);

                    for (ServerWebSocket player : playersList) {
                        //send player position packet Idle
                        player.writeFinalBinaryFrame(
                                Buffer.buffer(ServerMain.manualSerializer.serialize(bluePlayerStatePacket)));
                        Gdx.app.log(this.toString(),"bluePlayerStatePacket idle (" + bluePlayerStatePacket.getState() + ") sent w/ positions " + bluePlayerStatePacket.x + " " + bluePlayerStatePacket.y +"\n " +
                                "sent to:" + player);
                    }
                    ((PlayerEntity)playerItem.userData).idleStateSent = true;

                }
            }

        }
    }
    boolean staticSent = false;
    public void asteroidStatePacketMethod(){
        if (ballItem.userData.position.x != world.getRect(ballItem).x || ballItem.userData.position.y != world.getRect(ballItem).y) {
            /*float angle;
            angle = MathUtils.atan2(world.getRect(ballItem).y - ballItem.userData.position.y, ballItem.userData.position.x - ballItem.userData.position.x)
                    * MathUtils.radiansToDegrees;
            angle = (((angle % 360) + 360) % 360);*/
            ballItem.userData.position.x = world.getRect(ballItem).x;
            ballItem.userData.position.y = world.getRect(ballItem).y;

            ((BallEntity) ballItem.userData).state = BallEntity.States.MOVING;

            asteroidStatePacket.setX(ballItem.userData.position.x);
            asteroidStatePacket.setY(ballItem.userData.position.y);

            for (ServerWebSocket player : playersList) {
                player.writeFinalBinaryFrame(Buffer.buffer(ServerMain.manualSerializer.serialize(asteroidStatePacket)));
            Gdx.app.log(this.toString(), "asteroid position while moving sent");
            staticSent = false;
            }

        } else {
            if (((BallEntity) ballItem.userData).state == BallEntity.States.MOVING || staticSent == false) {
                ((BallEntity) ballItem.userData).state = BallEntity.States.STATIC;
                //Send staticStateEnum Packet ...honestly Static State isnt necessary for this
                asteroidStatePacket.setX(ballItem.userData.position.x);
                asteroidStatePacket.setY(ballItem.userData.position.y);
                for (ServerWebSocket player : playersList) {
                    player.writeFinalBinaryFrame(Buffer.buffer(ServerMain.manualSerializer.serialize(asteroidStatePacket)));
                    Gdx.app.log(this.toString(), "asteroid position static moving sent");
                }
                staticSent = true;
            }

        }

    }


    public ConcurrentLinkedQueue<Transferable> packetQueue = new ConcurrentLinkedQueue<Transferable>();
    public Array<Transferable> workList = new Array();
    Transferable element;
    public int cap = 20000;

    public void packetHandlingMethod(){
        for (int i = 0; i <= cap && (element = packetQueue.poll()) != null; i++) {
                workList.add(element);
            }

        for (Transferable element : workList) {

            if (element instanceof TouchDownPacket) {
                Gdx.app.log(this.toString(), "TouchDownPacket from: " + ((TouchDownPacket) element).getServerWebSocket());
                ClientID clientID = ServerMain.clientHash.get(((TouchDownPacket) element).getServerWebSocket());
                float angle = MathUtils.atan2(((TouchDownPacket) element).getY() - clientID.getClientPlayerItem().userData.position.y,
                        ((TouchDownPacket) element).getX() - clientID.getClientPlayerItem().userData.position.x)
                        * MathUtils.radiansToDegrees;
                angle = (((angle % 360) + 360) % 360);

                float newX;
                float newY;
                if (((TouchDownPacket) element).getX() - clientID.getClientPlayerItem().userData.position.x < -5 ||
                            ((TouchDownPacket) element).getX() - clientID.getClientPlayerItem().userData.position.x > 5) {
                        newX = clientID.getClientPlayerItem().userData.position.x + MathUtils.cosDeg(angle) * 5;
                }else {
                    newX = ((TouchDownPacket) element).getX();
                }

                if (((TouchDownPacket) element).getY() - clientID.getClientPlayerItem().userData.position.y < -5 ||
                            ((TouchDownPacket) element).getY() - clientID.getClientPlayerItem().userData.position.y > 5) {
                        newY = clientID.getClientPlayerItem().userData.position.y + MathUtils.sinDeg(angle) * 5;
                } else {
                    newY = ((TouchDownPacket) element).getY();
                }

                clientID.getClientGameRoom().gameWorld.world.move(clientID.getClientPlayerItem(), newX, newY,
                        ((PlayerEntity) clientID.getClientPlayerItem().userData).collisionFilter);

                if (((PlayerEntity) clientID.getClientPlayerItem().userData).touchedBall == true){
                    float playerToBallAngle = MathUtils.atan2((((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity.position.y) - (clientID.getClientPlayerItem().userData.position.y),
                                (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity.position.x) - (clientID.getClientPlayerItem().userData.position.x)) * MathUtils.radiansToDegrees;
                    /* Messi Dribble
                    float angle2 = MathUtils.atan2( (clientID.getClientPlayerItem().userData.position.y) - (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity.position.y),
                           (clientID.getClientPlayerItem().userData.position.x) - (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity.position.x)) * MathUtils.radiansToDegrees;*/

                        //use angle for Straight dribble
                    playerToBallAngle = (((playerToBallAngle % 360) + 360) % 360);

                    (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity).world.move(((PlayerEntity) clientID.getClientPlayerItem().userData).worldBallItem,
                            (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity).world.getRect(((PlayerEntity) clientID.getClientPlayerItem().userData).worldBallItem).x + (MathUtils.cosDeg(playerToBallAngle) * 7),
                            (((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity).world.getRect(((PlayerEntity) clientID.getClientPlayerItem().userData).worldBallItem).y + (MathUtils.sinDeg(playerToBallAngle) * 7), ((PlayerEntity) clientID.getClientPlayerItem().userData).ballEntity.collisionFilter);
                        ((PlayerEntity) clientID.getClientPlayerItem().userData).touchedBall = false;
                    }
                }
            }
            workList.clear();
        }

        boolean hitWallXleft;
        boolean hitWallXright;
        boolean hitWallYtop;
        boolean hitWallYbottom;

        int hitWallXleftCount;
        int hitWallXrightCount;
        int hitWallYtopCount;
        int hitWallYbottomCount;

        float bounceOutForce = 7f;

        public void checkBallBoundaries(){
            if(world.getRect(ballItem).x < ballEntity.boundary.boundaryXLeft){
                hitWallXleft = true;
                hitWallXleftCount = 1;
            }else if (world.getRect(ballItem).x > ballEntity.boundary.boundaryXRight - 32){
                hitWallXright = true;
                hitWallXrightCount = 1;
            }

            if(world.getRect(ballItem).y > ballEntity.boundary.boundaryYtop - 32){
                hitWallYtop = true;
                hitWallYtopCount = 1;
            }else if(world.getRect(ballItem).y < ballEntity.boundary.boundaryYBottom){
                hitWallYbottom = true;
                hitWallYbottomCount = 1;
            }

            if(hitWallXleft == true && hitWallXleftCount < 5){
                world.move(ballItem, world.getRect(ballItem).x + bounceOutForce/hitWallXleftCount, world.getRect(ballItem).y, ballEntity.collisionFilter);
                hitWallXleftCount++;
            }else if(hitWallXleftCount >=5){
                hitWallXleft = false;
                hitWallXleftCount = 1;
            }

            if(hitWallXright == true && hitWallXrightCount < 5){
                world.move(ballItem, world.getRect(ballItem).x - bounceOutForce/hitWallXrightCount, world.getRect(ballItem).y, ballEntity.collisionFilter);
                hitWallXrightCount++;
            }else if(hitWallXrightCount >=5){
                hitWallXright = false;
                hitWallXrightCount = 1;
            }

            if(hitWallYtop == true && hitWallYtopCount < 5){
                world.move(ballItem, world.getRect(ballItem).x, world.getRect(ballItem).y -bounceOutForce/hitWallYtopCount, ballEntity.collisionFilter);
                hitWallYtopCount++;
            }else if(hitWallYtopCount >=5){
                hitWallYtop = false;
                hitWallYtopCount = 1;
            }

            if(hitWallYbottom == true && hitWallYbottomCount < 5){
                world.move(ballItem, world.getRect(ballItem).x, world.getRect(ballItem).y + bounceOutForce/hitWallYbottomCount, ballEntity.collisionFilter);
                hitWallYbottomCount++;
            }else if(hitWallXleftCount >=5){
                hitWallYbottom = false;
                hitWallYbottomCount = 1;
            }

        }


}


