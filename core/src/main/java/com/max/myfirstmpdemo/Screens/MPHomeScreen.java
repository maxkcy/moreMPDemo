package com.max.myfirstmpdemo.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.max.myfirstmpdemo.GameAssetsAndStuff.GameAssets;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;
import com.max.myfirstmpdemo.Packets.RoomEnum;
import com.max.myfirstmpdemo.Packets.RoomPacket;

public class MPHomeScreen extends ScreenAdapter {
BitmapFont font;
MyFirstMpDemoMain game;
public GameAssets gameAssets;
public Stage stage;
public  Skin skin;
public ImageTextButton joinGameButtom;


    public MPHomeScreen(MyFirstMpDemoMain game) {
        this.game = game;
        gameAssets = new GameAssets(game);

    }

    @Override
    public void show() {
        font = gameAssets.getSgx().getFont("font");
        stage = new Stage(new FitViewport(600,450));
        Gdx.input.setInputProcessor(this.stage);
        skin = gameAssets.getSgx();

       joinGameButtom = new ImageTextButton("Join Game!!!", skin);
        joinGameButtom.setPosition(300 - joinGameButtom.getWidth()/2, 225);
        stage.addActor(joinGameButtom);

        ClickListener clicky = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RoomPacket roomPacket = new RoomPacket(RoomEnum.QUE);
                //roomPacket.roomEnum = RoomEnum.QUE; //<-- redundant but works
                if (game.clientWS.webSocket.isOpen() && joinGameButtom.isDisabled() == false) { //{joinGameButtom.removeListener(joinGameButtom.getClickListener());}
                    joinGameButtom.setDisabled(true);
                    game.clientWS.webSocket.send(roomPacket);
                    System.out.println("packet sent to server to add you to que...");
                    super.clicked(event, x, y);
                }
            }
        };
        joinGameButtom.addListener(clicky);

    }
    public static String string = new String("hello \nthis is The Multiplayer Home/Lobby Screen");
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.75f, .5f, .5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().apply();
        game.getBatch().setProjectionMatrix(stage.getViewport().getCamera().combined);
        game.getBatch().begin();

        font.draw(game.getBatch(),string , 85, 75);
        game.getBatch().end();

            if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
                font.setColor(.75f,.75f,.5f,.75f);
                string = "you pressed the enter key  :) ... \n this is a testing demo";

            }
        stage.draw();
        stage.act();
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
    stage.dispose();
    font.dispose();
    }
}
