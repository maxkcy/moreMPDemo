package com.max.myfirstmpdemo.ClientWS;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.max.myfirstmpdemo.GameAssetsAndStuff.BluePlayer;
import com.max.myfirstmpdemo.GameAssetsAndStuff.RedPlayer;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;
import com.max.myfirstmpdemo.Packets.AsteroidStatePacket;
import com.max.myfirstmpdemo.Packets.BluePlayerStatePacket;
import com.max.myfirstmpdemo.Packets.BlueShirtInitPacket;
import com.max.myfirstmpdemo.Packets.RedPlayerStatePacket;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;
import com.max.myfirstmpdemo.Packets.ScorePacket;

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
                    if(game.roomScreen.redPlayers != null && !game.roomScreen.redPlayers.containsKey(redShirtInitPacket.IDKey)){
                game.roomScreen.redPlayers.put(redShirtInitPacket.IDKey, new RedPlayer(game));
                    Gdx.app.log(this.toString(), "new player " + redShirtInitPacket.IDKey + " added to: game.roomScreen.redPlayers");
                    }else if(game.roomScreen.redPlayers.containsKey(redShirtInitPacket.IDKey)){
                        game.roomScreen.redPlayers.removeValue(game.roomScreen.redPlayers.get(redShirtInitPacket.IDKey), true);
                        game.roomScreen.redPlayers.removeKey(redShirtInitPacket.IDKey);
                    }else{
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
                                    Gdx.app.log(this.toString(), "animation  set to idle animation " + game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation);

                                }
                                break;
                            }
                            case running: {
                                if (game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation != game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redRunningAnimation) {
                                    game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).resetStatetime();
                                    game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation = game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redRunningAnimation;
                                    Gdx.app.log(this.toString(), "animation  set to running animation " + game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation);

                                }
                                break;
                            }
                            case kicking: {
                                if (game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation != game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redKickingAnimation) {
                                    game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation = game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).redKickingAnimation;
                                    Gdx.app.log(this.toString(), "animation  set to kicking animation " + game.roomScreen.redPlayers.get(redPlayerStatePacket.getClientId()).animation);
                                }
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
            public boolean handle(final WebSocket webSocket, final BlueShirtInitPacket blueShirtInitPacket) {
                Gdx.app.log(this.toString(), "BlueShirtInitPacket Handle START");
                if(game.roomScreen.bluePlayers != null && !game.roomScreen.bluePlayers.containsKey(blueShirtInitPacket.IDKey)){
                    game.roomScreen.bluePlayers.put(blueShirtInitPacket.IDKey, new BluePlayer(game));
                    Gdx.app.log(this.toString(), "new player " + blueShirtInitPacket.IDKey + " added to: game.roomScreen.BluePlayers");
                }else if(game.roomScreen.bluePlayers.containsKey(blueShirtInitPacket.IDKey)){
                    game.roomScreen.bluePlayers.removeValue(game.roomScreen.bluePlayers.get(blueShirtInitPacket.IDKey), true);
                    game.roomScreen.bluePlayers.removeKey(blueShirtInitPacket.IDKey);
                } else{
                    Gdx.app.log(this.toString(), "game.roomScreen.bluePlayers == null");}


                Gdx.app.log(this.toString(), "BlueShirtInitPacket Handle END");
                return true;
            }
        });

        webSocketHandler.registerHandler(BluePlayerStatePacket.class, new WebSocketHandler.Handler<BluePlayerStatePacket>() {

            @Override
            public boolean handle(final WebSocket webSocket, final BluePlayerStatePacket bluePlayerStatePacket) {

                Gdx.app.log(this.toString(), "RedPlayerStatePacket Handle START\n packet from: " + bluePlayerStatePacket.getClientId());
                Gdx.app.log(this.toString(), "PlayerStatePacket from " + bluePlayerStatePacket.getClientId() + " \nis being handled." +
                        " bluePlayerStatePacket.State: " + bluePlayerStatePacket.getState());

                if(game.roomScreen.bluePlayers.containsKey(bluePlayerStatePacket.getClientId())){
                    Gdx.app.log(this.toString(), "bluePlayerStatePacket.ClientId has matched w/ a BluePlayer in bluePlayers Array");
                    try {
                        switch (bluePlayerStatePacket.getState()) {
                            case idle: {
                                if (game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).animation != game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).blueIdleAnimation) {
                                    game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).setAnimation(BluePlayer.blueIdleAnimation);
                                    Gdx.app.log(this.toString(), "animation  set to idle animation " + game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).animation);
                                }
                                break;
                            }
                            case running: {
                                if (game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).animation != game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).blueRunningAnimation) {
                                    game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).resetStatetime();
                                    game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).animation = game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).blueRunningAnimation;
                                    Gdx.app.log(this.toString(), "animation  set to running animation " + game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).animation);
                                }
                                break;
                            }
                            case kicking: {
                                if (game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).animation != game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).blueKickingAnimation) {
                                    game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).animation = game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).blueKickingAnimation;
                                    Gdx.app.log(this.toString(), "animation  set to kicking animation " + game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).animation);
                                }
                                break;
                            }

                            default: {
                                Gdx.app.log(this.toString(), " no state given ??? " + bluePlayerStatePacket.getState());
                                break;
                            }
                        }
                    }catch (Exception ex){
                        Gdx.app.log(this.toString(), " state null ??? " + bluePlayerStatePacket.getState());
                    }
                    game.roomScreen.bluePlayers.get(bluePlayerStatePacket.getClientId()).setPosition(bluePlayerStatePacket.x, bluePlayerStatePacket.y);

                }else {Gdx.app.log(this.toString(), "bluePlayerStatePacket.getClientId does not match a key in game.roomScreen.bluePlayers.keys " + bluePlayerStatePacket.getClientId());}

                Gdx.app.log(this.toString(), "BluePlayerStatePacket Handle END");
                return true;
            }
        });

        webSocketHandler.registerHandler(AsteroidStatePacket.class, new WebSocketHandler.Handler<AsteroidStatePacket>(){
            @Override
            public boolean handle(final WebSocket webSocket, final AsteroidStatePacket asteroidStatePacket) {
                Gdx.app.log(this.toString(), "AsteroidStatePacket Handle Start");
                game.roomScreen.asteroidBall.setPosition(asteroidStatePacket.getX(), asteroidStatePacket.getY());
                Gdx.app.log(this.toString(), "Asteroid StatePacket set asteroidBall position");
                Gdx.app.log(this.toString(), "AsteroidStatePacket Handle END");
                return true;
            }
        });

        webSocketHandler.registerHandler(ScorePacket.class, new WebSocketHandler.Handler<ScorePacket>() {
            @Override
            public boolean handle(WebSocket webSocket, ScorePacket scorePacket) {
                Gdx.app.log(this.toString(), "ScorePacket Handle Start");
                switch (scorePacket.getTeam()){
                    case Red:{
                        game.roomScreen.hud.setRedScore(scorePacket.getScore());
                        break;
                    }

                    case Blue:{
                        game.roomScreen.hud.setBlueScore(scorePacket.getScore());
                        break;
                    }
                }
                Gdx.app.log(this.toString(), "ScorePacket End");
                return true;
            }
        });


        return webSocketHandler;
    }


}
