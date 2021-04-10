package com.max.myfirstmpdemo.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.max.myfirstmpdemo.GameAssetsAndStuff.GameAssets;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class RoomScreen extends ScreenAdapter {
MyFirstMpDemoMain game;
SplashScreen splashScreen;
OrthographicCamera cam;
FitViewport viewport;
public BitmapFont font;
public Sprite footBallPitchBackround;
public static String message;
public GameAssets gameAssets;

    public RoomScreen(MyFirstMpDemoMain game) {
        this.game = game;
        gameAssets = new GameAssets(game);
    }

    @Override
    public void show() {
        super.show();
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
        game.getBatch().end();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }



    @Override
    public void dispose() {
        super.dispose();
    }
}
