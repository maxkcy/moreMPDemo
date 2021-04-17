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
import com.max.myfirstmpdemo.Packets.CountDownPacket;
import com.max.myfirstmpdemo.Packets.RoomEnum;
import com.max.myfirstmpdemo.Packets.RoomPacket;
import com.max.myfirstmpdemo.PacketsSerializer;
import com.max.myfirstmpdemo.Screens.MPHomeScreen;
import com.max.myfirstmpdemo.Screens.RoomScreen;
import com.max.myfirstmpdemo.Tools;

import java.net.InetAddress;

public class ClientWS {

   public WebSocket webSocket;
   public ManualSerializer serializer;
   private MyFirstMpDemoMain game;
   float count;

   public ClientWS(MyFirstMpDemoMain game) {
       this.game = game;
    }

    public void init(){
       //String wss;
       //wss = WebSockets.toSecureWebSocketUrl("maxkcyfun.fun", 443);
        webSocket = WebSockets.newSocket(WebSockets.toSecureWebSocketUrl("maxkcyfun.fun", 443) + "/myws");

  
        System.out.println(webSocket.isSecure());
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
        WebSocketHandler webSocketHandler = new WebSocketHandler(){

            @Override
            protected boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException {
                /*if((packet instanceof RoomPacket)){
                    System.out.println("message from server: Request received. Added to que");
                    MPHomeScreen.string = "Waiting in server's queue to join next game";
                }
                if(packet instanceof CountDownPacket){
                    System.out.println("message from server: Countdown Packet" + "Time is: " + ((CountDownPacket) packet).getTime() );
                    MPHomeScreen.string = "im lazy to make a new screen for game,\nbut you are in it now, and here is the countdown time: \n"
                    +((CountDownPacket) packet).getTime();
                }*/
                return super.onMessage(webSocket, packet);
                //return FULLY_HANDLED; //<-- only handles once???
                //return false;
            }



            @Override
            public boolean onOpen(WebSocket webSocket) {
                System.out.println("Websocket connection opened");
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
               System.out.println(error);
               return FULLY_HANDLED;
            }
        };
        webSocketHandler.registerHandler(CountDownPacket.class, new WebSocketHandler.Handler<CountDownPacket>() {
            @Override
            public boolean handle(final WebSocket webSocket, final CountDownPacket packet) {
                System.out.println("message from server: Countdown Packet" + "Time is: " + packet.getTime());
                if(game.getScreen() != game.roomScreen){
                    Gdx.app.postRunnable(()-> game.setScreen(game.roomScreen)); // really bad way of handling because you dont want to check every time, just send a different packet to switch to screen, then . but this is demo
                }
                RoomScreen.message = ("im not too lazy to make a new screen for game,\nyou are in it now," +
                        "\nand here is the countdown time: " + packet.getTime());
                return true;
            }
        });

        webSocketHandler.registerHandler(RoomPacket.class, new WebSocketHandler.Handler<RoomPacket>() {
            @Override
            public boolean handle(final WebSocket webSocket, final RoomPacket packet) {
                if(packet.roomEnum == RoomEnum.QUE){
                System.out.println("message from server: Request received. Added to que");
                MPHomeScreen.string = "Waiting in server's queue to join next game";
                }else if(packet.roomEnum == RoomEnum.MPHOMELOBBY){
                    count++;
                    System.out.println("message from server: Sent back to MPHomeScreen/Lobby");
                    Gdx.app.postRunnable(()-> game.setScreen(game.mpHomeScreen));
                    MPHomeScreen.string = ("Hello! This is The Multiplayer Home/Lobby Screen\nnumber of times played this session: " + count);
                    game.mpHomeScreen.joinGameButtom.setDisabled(false);

                }

                return true;
            }
        });

   return webSocketHandler;
   }

   public void dispose(){
       webSocket.close();
   }
}
