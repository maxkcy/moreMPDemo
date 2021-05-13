package com.max.myfirstmpdemo.Screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.max.myfirstmpdemo.GameAssetsAndStuff.GameAssets;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class Hud implements Disposable {
    MyFirstMpDemoMain game;
    GameAssets gameAssets;
    BitmapFont font;
    OrthographicCamera cam;
    Viewport viewport;



    int redScore;
    int blueScore;

    public Hud setRedScore(int redScore) {
        this.redScore = redScore;
        return this;
    }

    public Hud setBlueScore(int blueScore) {
        this.blueScore = blueScore;
        return this;
    }

    public Hud(MyFirstMpDemoMain game) {
        this.game = game;
        gameAssets = new GameAssets(game);
        font = gameAssets.getSgx().getFont("font");

        redScore = 0;
        blueScore = 0;
    }

    public void init(){
        cam = new OrthographicCamera();
        viewport = new FitViewport(600,400, cam);
        cam.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
    }

    //public String redScoreString  = "Red: " + redScore;
    //public String blueScoreString = "Blue: " + blueScore;

    public void update(){
        viewport.apply();
        cam.update();
        game.getBatch().setProjectionMatrix(cam.combined);
        game.getBatch().begin();
        font.draw(game.getBatch(), "Red: " + redScore, 50, 385);
        font.draw(game.getBatch(),"Blue: " + blueScore , 150, 385);
        game.getBatch().end();
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }


    @Override
    public void dispose() {

    }
}
