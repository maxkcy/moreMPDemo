package com.max.myfirstmpdemo.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.max.myfirstmpdemo.GameAssetsAndStuff.AsteroidBall;
import com.max.myfirstmpdemo.GameAssetsAndStuff.BluePlayer;
import com.max.myfirstmpdemo.GameAssetsAndStuff.GameAssets;
import com.max.myfirstmpdemo.GameAssetsAndStuff.RedPlayer;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;
import com.max.myfirstmpdemo.Packets.TouchDownPacket;

public class RoomScreen extends ScreenAdapter {

    MyFirstMpDemoMain game;
OrthographicCamera cam;
FitViewport viewport;
public BitmapFont font;
public Sprite footBallPitchBackround;
public static String message;
public GameAssets gameAssets;
public ArrayMap<String, RedPlayer> redPlayers;
public ArrayMap<String, BluePlayer> bluePlayers;
Vector3 touch;
TouchDownPacket touchDownPacket;
public AsteroidBall asteroidBall;

    public RoomScreen(MyFirstMpDemoMain game) {
        this.game = game;
        gameAssets = new GameAssets(game);
        redPlayers = new ArrayMap<>();
        bluePlayers = new ArrayMap<>();
        asteroidBall = new AsteroidBall(game);
    }

    @Override
    public void show() {
        super.show();
        touch = new Vector3();
        touchDownPacket = new TouchDownPacket();
        cam = new OrthographicCamera();
        viewport = new FitViewport(600f, 400f, cam);
        cam.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);// put this line before assigning viewport to generate error
        footBallPitchBackround = gameAssets.getFootballPitchBackground();
        footBallPitchBackround.setBounds(0,0, 600f, 400f);
        font = gameAssets.getSgx().getFont("font");
        message = "welcome to the game room";
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(.2f, .88f, .2f, .65f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        cam.update();

        game.getBatch().setProjectionMatrix(cam.combined);
        game.getBatch().begin();
        footBallPitchBackround.draw(game.getBatch());
        font.draw(game.getBatch(), message, 85, 80);

        for (RedPlayer redPlayer : redPlayers.values()) {
            redPlayer.update(delta);
        }
        for (BluePlayer bluePlayer : bluePlayers.values()){
            bluePlayer.update(delta);
        }

        asteroidBall.update(delta);
        game.getBatch().end();

        if (Gdx.input.isTouched()){
            touch = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            touchDownPacket.setX(touch.x);
            touchDownPacket.setY(touch.y);
            game.clientWS.webSocket.send(touchDownPacket);
            Gdx.app.log(this.toString(), "TouchDownPacket sent");
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }



    @Override
    public void dispose() {
        super.dispose();
        redPlayers.clear();
        bluePlayers.clear();
    }
}
