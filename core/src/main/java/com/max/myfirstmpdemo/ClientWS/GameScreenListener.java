package com.max.myfirstmpdemo.ClientWS;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.max.myfirstmpdemo.GameAssetsAndStuff.RedPlayer;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;

public class GameScreenListener {
    private MyFirstMpDemoMain game;

    public GameScreenListener(MyFirstMpDemoMain game) {
        this.game = game;
    }

    public WebSocketHandler getListener() {
        WebSocketHandler webSocketHandler = new WebSocketHandler();
        webSocketHandler.registerHandler(RedShirtInitPacket.class, new WebSocketHandler.Handler<RedShirtInitPacket>() {
            @Override
            public boolean handle(WebSocket webSocket, RedShirtInitPacket redShirtInitPacket) {
                game.roomScreen.redPlayers.put(redShirtInitPacket.IDKey, new RedPlayer(game));
                return true;
            }
        });
        return webSocketHandler;
    }
}
