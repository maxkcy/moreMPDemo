package com.max.myfirstmpdemo.ClientWS;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.data.WebSocketException;
import com.max.myfirstmpdemo.GameAssetsAndStuff.RedPlayer;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;
import com.max.myfirstmpdemo.Packets.RedPlayerStatePacket;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;

public class GameScreenListener {
    private MyFirstMpDemoMain game;

    public GameScreenListener(MyFirstMpDemoMain game) {
        this.game = game;
    }

    public WebSocketHandler getListener() {
        WebSocketHandler webSocketHandler = new WebSocketHandler(){

            @Override
            protected boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException {
                return super.onMessage(webSocket, packet);
            }
        };

        webSocketHandler.registerHandler(RedShirtInitPacket.class, new WebSocketHandler.Handler<RedShirtInitPacket>() {
            @Override
            public boolean handle(final WebSocket webSocket, final RedShirtInitPacket redShirtInitPacket) {
                try{
                if(game.roomScreen.redPlayers != null){
                game.roomScreen.redPlayers.put(redShirtInitPacket.IDKey, new RedPlayer(game));
                    Gdx.app.log(this.toString(), "new player added to: game.roomScreen.redPlayers");}
                else{
                    Gdx.app.log(this.toString(), "game.roomScreen.redPlayers == null");}}
                catch(Exception ex){Gdx.app.log(this.toString(), "some error thrown");}
                return true;
            }
        });

        webSocketHandler.registerHandler(RedPlayerStatePacket.class, new WebSocketHandler.Handler<RedPlayerStatePacket>() {
            @Override
            public boolean handle(final WebSocket webSocket, final RedPlayerStatePacket redPlayerStatePacket) {
                if(game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId())!= null){
                    Gdx.app.log(this.toString(), "PlayerStatePacket handled");
                switch (redPlayerStatePacket.getState()){
                    case idle:
                        if(game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation != game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redIdleAnimation){
                        game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation = game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redIdleAnimation;}
                    break;
                    case running:
                        if(game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation != game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redRunningAnimation){
                        game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation = game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redRunningAnimation;}
                    break;
                    case kicking:
                        if(game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation != game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redKickingAnimation){
                        game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation = game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redKickingAnimation;}
                    default:
                        break;
                }
                    game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).setPosition(redPlayerStatePacket.x, redPlayerStatePacket.y);

            }
                return true;
            }
        });

        return webSocketHandler;
    }
}
