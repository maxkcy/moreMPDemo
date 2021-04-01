package com.max.myfirstmpdemo.headless;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.max.myfirstmpdemo.PacketsSerializer;


import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

public class ServerMain extends Game {
    public Vertx vertx;
    public ManualSerializer manualSerializer;
    public HttpServer httpServer;
    public Array<ServerWebSocket> clientWSList; // = new Array<>();
    public Queue<ServerWebSocket> waitingForGameQueue;// = new Queue<>();
    public HandleFrame handleFrame;// = new HandleFrame(this);
    public Array<GameRoom> gameRoomArray;// = new Array<>();


    @Override
    public void create() {
        vertx = Vertx.vertx();
        manualSerializer = new ManualSerializer();
        PacketsSerializer.register(manualSerializer);
        clientWSList = new Array<>();
        waitingForGameQueue = new Queue<>();
        handleFrame = new HandleFrame(this);
        gameRoomArray = new Array<>();
        this.launch();
    }

    @Override
    public void render() {
        super.render();
        if(waitingForGameQueue.size == 2){
            GameRoom gameRoom = new GameRoom(this);
            gameRoom.show();
            for (ServerWebSocket serverWebSocket:
                 waitingForGameQueue) {
                gameRoom.playersList.add(serverWebSocket);
            }
            System.out.println("The GameRoom has players: " + gameRoom.playersList);
            gameRoomArray.add(gameRoom);
            waitingForGameQueue.clear();
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

    }

    private void launch(){
        httpServer = vertx.createHttpServer();
        System.out.println("Launching Server...");

        httpServer.webSocketHandler(new Handler<ServerWebSocket>(){
            @Override
            public void handle(ServerWebSocket client) {
                System.out.println("connection from (WS handler)"+ client.textHandlerID());
                clientWSList.add(client);

                client.frameHandler(new Handler<WebSocketFrame>(){
                    @Override
                    public void handle(WebSocketFrame event) {
                        handleFrame.handleFrame(client, event);
                    }
                });

                client.closeHandler(new Handler<Void>() {
                    @Override
                    public void handle(Void event) {
                        System.out.println("client disconnected (WS handler)"+ client.textHandlerID());
                        clientWSList.removeValue(client, true);
                    }
                });

            }
        });
        httpServer.listen(8778);
        System.out.println("Server is listening for new connections...");
    }
}
