package com.max.myfirstmpdemo.ClientWS;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.max.myfirstmpdemo.GameAssetsAndStuff.RedPlayer;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;
import com.max.myfirstmpdemo.Packets.BlueShirtInitPacket;
import com.max.myfirstmpdemo.Packets.RedPlayerStatePacket;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;

import static com.max.myfirstmpdemo.Packets.RedPlayerStatePacket.States.*;

public class GameScreenListener {
    private MyFirstMpDemoMain game;

    public GameScreenListener(MyFirstMpDemoMain game) {
        this.game = game;
    }

    public WebSocketHandler getListener() {
        WebSocketHandler webSocketHandler = new WebSocketHandler(){

            @Override
            public void registerHandler(Class<?> packetClass, Handler<?> handler) {
                super.registerHandler(packetClass, handler);
            }

            @Override
            public void setFailIfNoHandler(boolean failIfNoHandler) {
                super.setFailIfNoHandler(failIfNoHandler);
            }

            @Override
            public boolean onOpen(WebSocket webSocket) {
                return super.onOpen(webSocket);
            }

            @Override
            public boolean onClose(WebSocket webSocket, WebSocketCloseCode code, String reason) {
                return super.onClose(webSocket, code, reason);
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
            protected boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException {
                return super.onMessage(webSocket, packet);
            }

            @Override
            public boolean onError(WebSocket webSocket, Throwable error) {
                return super.onError(webSocket, error);
            }
        };

        webSocketHandler.registerHandler(RedShirtInitPacket.class, new WebSocketHandler.Handler<RedShirtInitPacket>() {
            @Override
            public boolean handle(final WebSocket webSocket, final RedShirtInitPacket redShirtInitPacket) {

                    Gdx.app.log(this.toString(), "RedShirtInitPacket Handle START");
                    if(game.roomScreen.redPlayers != null){
                game.roomScreen.redPlayers.put(redShirtInitPacket.IDKey, new RedPlayer(game));
                    Gdx.app.log(this.toString(), "new player " + redShirtInitPacket.IDKey + " added to: game.roomScreen.redPlayers");}
                else{
                    Gdx.app.log(this.toString(), "game.roomScreen.redPlayers == null");}


                Gdx.app.log(this.toString(), "RedShirtInitPacket Handle END");
                return true;
            }
        });

        webSocketHandler.registerHandler(RedPlayerStatePacket.class, new WebSocketHandler.Handler<RedPlayerStatePacket>() {
            @Override
            public boolean handle(final WebSocket webSocket, final RedPlayerStatePacket redPlayerStatePacket) {

                Gdx.app.log(this.toString(), "RedPlayerStatePacket Handle START\n packet from: " + redPlayerStatePacket.getClientId());
                Gdx.app.log(this.toString(), "PlayerStatePacket from " + redPlayerStatePacket.getClientId() + " \nis being handled." +
                        " redPlayerStatePacket.State: " + redPlayerStatePacket.getState());

                if(game.roomScreen.redPlayers.containsKey(redPlayerStatePacket.getClientId())){
                    Gdx.app.log(this.toString(), "redPlayerStatePacket.ClientId has matched w/ a RedPlayer in redPlayers Array");
                    try {
                        switch (redPlayerStatePacket.getState()) {
                            case idle: {
                                if (game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation != game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redIdleAnimation) {
                                    game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).setAnimation(RedPlayer.redIdleAnimation);
                                }
                                Gdx.app.log(this.toString(), "animation  set to idle animation " + game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation);
                                break;
                            }
                            case running: {
                                if (game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation != game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redRunningAnimation) {
                                    game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation = game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redRunningAnimation;
                                }
                                Gdx.app.log(this.toString(), "animation  set to running animation " + game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation);
                                break;
                            }
                            case kicking: {
                                if (game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation != game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redKickingAnimation) {
                                    game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation = game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redKickingAnimation;
                                }
                                Gdx.app.log(this.toString(), "animation  set to kicking animation " + game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation);
                                break;
                            }

                            default: {
                                Gdx.app.log(this.toString(), " no state given ??? " + redPlayerStatePacket.getState());
                                break;
                            }
                        }
                    }catch (Exception ex){
                        Gdx.app.log(this.toString(), " state null ??? " + redPlayerStatePacket.getState());
                    }
                    game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).setPosition(redPlayerStatePacket.x, redPlayerStatePacket.y);

            }else {Gdx.app.log(this.toString(), "redPlayerStatePacket.getClientId does not match a key in game.roomScreen.redPlayers.keys " + redPlayerStatePacket.getClientId());}

                Gdx.app.log(this.toString(), "RedPlayerStatePacket Handle END");
                return true;
            }
        });

        webSocketHandler.registerHandler(BlueShirtInitPacket.class, new WebSocketHandler.Handler<BlueShirtInitPacket>() {
            @Override
            public boolean handle(WebSocket webSocket, BlueShirtInitPacket blueShirtInitPacket) {
                Gdx.app.log(this.toString(), "BlueShirtInitPacket handle START");
                Gdx.app.log(this.toString(), "blueShirtInitPacket handled ...\n" +
                        "printing out a line of blah because blueShirtInitPacket handling will be implemented after\n" +
                        "redpackets are at least drawn... handling... handled.");
                Gdx.app.log(this.toString(), "BlueShirtIntiPacket handle END");
                return true;
            }
        });

        return webSocketHandler;
    }
}
