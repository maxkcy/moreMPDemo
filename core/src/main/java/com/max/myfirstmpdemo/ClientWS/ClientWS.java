package com.max.myfirstmpdemo.ClientWS;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketAdapter;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;
import com.max.myfirstmpdemo.Packets.RoomEnum;
import com.max.myfirstmpdemo.Packets.RoomPacket;
import com.max.myfirstmpdemo.PacketsSerializer;
import com.max.myfirstmpdemo.Screens.MPHomeScreen;

public class ClientWS {

   public WebSocket webSocket;
   public ManualSerializer serializer;
   private MyFirstMpDemoMain game;

   public ClientWS(MyFirstMpDemoMain game) {
       this.game = game;
    }

    public void init(){
        webSocket = WebSockets.newSocket(WebSockets.toWebSocketUrl("localhost", 8778));
        //inorder for the initialization error to go away call
        // ...it's important CommonWebSockets.initiate(); in the launcher.
        serializer = new ManualSerializer();
        PacketsSerializer.register(serializer);
        webSocket.setSerializer(serializer);
        webSocket.setSendGracefully(true);


        webSocket.addListener(getListener());
        webSocket.connect();

    }

    private WebSocketListener getListener() {
        WebSocketHandler webSocketAdapter = new WebSocketHandler(){

            @Override
            protected boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException {
                if((packet instanceof RoomPacket)){
                    System.out.println("message from server: Request received. Added to que");
                }
                //return super.onMessage(webSocket, packet);
                return FULLY_HANDLED;
            }

            @Override
            public boolean onOpen(WebSocket webSocket) {
                Gdx.app.postRunnable(()-> game.setScreen(game.mpHomeScreen));
//game.setScreen(game.mpHomeScreen); <-dont use this
                return FULLY_HANDLED;
            }

            @Override
            public boolean onClose(WebSocket webSocket, WebSocketCloseCode code, String reason) {

                System.out.println("socket closed");
                return FULLY_HANDLED;
            }

            @Override
            public boolean onMessage(WebSocket webSocket, String packet) {
                return super.onMessage(webSocket, packet);
            }

            @Override
            public boolean onMessage(WebSocket webSocket, byte[] packet) {


                return super.onMessage(webSocket, packet);
            }


            @Override
            public boolean onError(WebSocket webSocket, Throwable error) {
               System.out.println(error + " :(");
               return FULLY_HANDLED;
            }
        };
   return webSocketAdapter;
   }
}
