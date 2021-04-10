package com.max.myfirstmpdemo.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.max.myfirstmpdemo.GameAssetsAndStuff.GameAssets;
import com.max.myfirstmpdemo.LoadingPathsAndScreen.Loader;
import com.max.myfirstmpdemo.LoadingPathsAndScreen.SkinPaths;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class SplashScreen implements Screen {
    public SplashScreen() {
    }
    private Stage stage;
    public GameAssets gameAssets;
    Skin skin;
    MyFirstMpDemoMain game;
    ProgressBar progressBar;
    Loader loader;

    public SplashScreen(MyFirstMpDemoMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        loader = new Loader(game);
        gameAssets = new GameAssets(game);
        stage = new Stage(new FitViewport(500,300));
        skin = gameAssets.getCleanCrispy();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Image image = new Image(skin, "badlogic");
        image.setScaling(Scaling.fill);
        table.add(image).minSize(1.0f).maxSize(400.0f).prefSize(250f);

        table.row();
        progressBar = new ProgressBar(0.0f, 1000.0f, 1.0f, false, skin);
        progressBar.setValue(0.0f);
        progressBar.setAnimateDuration(1.0f);
        progressBar.setAnimateInterpolation(Interpolation.linear);
        progressBar.setRound(true);
        progressBar.setVisualInterpolation(Interpolation.linear);
        progressBar.sizeBy(10000f,4.9f);
        //progressBar.setWidth(400f);
        //progressBar.setSize(400f, 5f);
        progressBar.rotateBy(2.5f);
        table.add(progressBar).padBottom(25.0f).spaceTop(25.0f).expandX().maxWidth(10000000.0f).maxHeight(5.0f);
        table.getCell(progressBar).prefSize(250,40);
        stage.addActor(table);

       //table.setDebug(true);
        //table.setDebug(true, true);

    }
    float value = 0;
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        value += Gdx.graphics.getDeltaTime();
        stage.draw();
        stage.act();
        progressBar.setValue(game.getAssetManager().getProgress() * progressBar.getMaxValue());
        System.out.println(game.getAssetManager().getProgress() * progressBar.getMaxValue());
        if(game.getAssetManager().isFinished() && progressBar.getValue() == progressBar.getMaxValue() && value > 1.1f){
            game.loginScreen = new LoginScreen(game);
            game.mpHomeScreen = new MPHomeScreen(game);
            game.roomScreen = new RoomScreen(game);
            Gdx.app.postRunnable(()-> game.setScreen(game.loginScreen));;
        }
        //value += 5;

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
