package com.max.myfirstmpdemo.headless;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.max.myfirstmpdemo.Packets.BlueShirtInitPacket;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;
import com.max.myfirstmpdemo.PacketsSerializer;
import com.max.myfirstmpdemo.Tools;


import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

public class ServerMain extends Game {
    public Vertx vertx;
    public static ManualSerializer manualSerializer;
    public HttpServer httpServer;
    public HttpServerOptions httpServerOptions;
    public Array<ServerWebSocket> clientWSList; // = new Array<>();
    public Queue<ServerWebSocket> waitingForGameQueue;// = new Queue<>();
    public HandleFrame handleFrame;// = new HandleFrame(this);
    public Array<GameRoom> gameRoomArray;// = new Array<>();
    public static ArrayMap<ServerWebSocket, ClientID> clientHash;

    @Override
    public void create() {
        vertx = Vertx.vertx();
        manualSerializer = new ManualSerializer();
        PacketsSerializer.register(manualSerializer);
        clientWSList = new Array<>();
        waitingForGameQueue = new Queue<>();
        handleFrame = new HandleFrame(this);
        gameRoomArray = new Array<>();
        clientHash = new ArrayMap<>();
        this.launch();


    }


    @Override
    public void render() {

        if(waitingForGameQueue.size >= 2){ //
            GameRoom gameRoom = new GameRoom(this);

            /*for (ServerWebSocket serverWebSocket:
                 waitingForGameQueue) {
                gameRoom.playersList.add(serverWebSocket);
            }*/ //defunct old code
            int i = 0;
            for (int playersToBeAdded = 2; i < playersToBeAdded; i++){
            gameRoom.playersList.add(waitingForGameQueue.first());
            clientHash.get(waitingForGameQueue.first()).setClientGameRoom(gameRoom);
            Gdx.app.log(this.toString(), waitingForGameQueue.first() + " added to gameRoom.playersList. Num of players in queue: " + waitingForGameQueue.size);
            waitingForGameQueue.removeFirst();
            }
            System.out.println("The GameRoom has players: " + gameRoom.playersList);
            gameRoomArray.add(gameRoom);
            //waitingForGameQueue.clear();
            gameRoom.show();
        }

        for(GameRoom gameRoom : gameRoomArray){
            gameRoom.render(Gdx.graphics.getDeltaTime());

            if(gameRoom.isActive == false){
                gameRoomArray.removeValue(gameRoom, true);
                gameRoom.dispose();
                System.out.println("Game room disposed");
                gameRoom = null;
            }
        }
        super.render();
    }


    @Override
    public void dispose() {
        super.dispose();
        httpServer.close();
    }
    boolean handled = false;
    private void launch(){
        httpServerOptions = new HttpServerOptions();
        httpServerOptions.setEnabledSecureTransportProtocols(httpServerOptions.getEnabledSecureTransportProtocols());
        httpServer = vertx.createHttpServer(httpServerOptions);
        System.out.println("Launching Server...");

        httpServer.webSocketHandler(new Handler<ServerWebSocket>(){
            @Override
            public void handle(ServerWebSocket client) {
                System.out.println("connection from (WS handler)"+ client.textHandlerID());
                clientWSList.add(client);
                clientHash.put(client, new ClientID(client));


                client.frameHandler(new Handler<WebSocketFrame>(){
                    @Override
                    public void handle(WebSocketFrame event) {
                        handleFrame.handleFrame(client, event);
                        handleFrame.handleGame(client, event);
                    }
                });

                client.closeHandler(new Handler<Void>() {
                    @Override
                    public void handle(Void event) {
                        System.out.println("client disconnected (WS handler)"+ client.textHandlerID());
                        handled = false;
                        waitingForGameQueue.forEach(serverWebSocket -> {
                            if(serverWebSocket == client){
                                waitingForGameQueue.removeValue(client, true);
                                System.out.println(client + "removed from queue");
                                handled = true;
                            }
                        });
                        if(handled == false){
                            for (GameRoom gameRoom : gameRoomArray) {
                                if (gameRoom.playersList.contains(client, true)) {


                                    if (clientHash.get(client).getTeam() == ClientID.Team.BLUE){
                                        gameRoom.gameWorld.playerItemListTeamBlue.removeValue(clientHash.get(client).getClientPlayerItem(), true);
                                        BlueShirtInitPacket blueShirtInitPacket = new BlueShirtInitPacket();
                                        blueShirtInitPacket.setIDKey(clientHash.get(client).playerID);
                                        for (ServerWebSocket player : gameRoom.playersList){
                                            player.writeFinalBinaryFrame((Buffer.buffer(ServerMain.manualSerializer.serialize(blueShirtInitPacket))));
                                        }

                                    }else if(clientHash.get(client).getTeam() == ClientID.Team.RED){
                                        RedShirtInitPacket redShirtInitPacket = new RedShirtInitPacket();
                                        redShirtInitPacket.setIDKey(clientHash.get(client).playerID);
                                        for (ServerWebSocket player : gameRoom.playersList){
                                            player.writeFinalBinaryFrame((Buffer.buffer(ServerMain.manualSerializer.serialize(redShirtInitPacket))));
                                        }
                                        gameRoom.gameWorld.playerItemListTeamRed.removeValue(clientHash.get(client).getClientPlayerItem(), true);
                                    }
                                    gameRoom.gameWorld.world.remove(clientHash.get(client).getClientPlayerItem());
                                    ServerMain.clientHash.get(client).setClientPlayerItem(null);
                                    clientHash.get(client).setTeam(null);
                                    gameRoom.gameWorld.playersList.removeValue(client, true);
                                    gameRoom.playersList.removeValue(client, true);
                                    clientHash.get(client).setClientGameRoom(null);


                                break;
                                }
                            }
                        }
                        handled = true;
                        clientHash.removeKey(client);
                        clientWSList.removeValue(client, true);
                    }
                });


            }
        });
        httpServer.listen(Tools.PORT);

        System.out.println("Server Started \n" +
                "listening for new connections...");
    }
}
