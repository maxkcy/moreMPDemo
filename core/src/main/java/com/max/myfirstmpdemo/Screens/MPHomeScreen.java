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
GameAssets gameAssets;
public Stage stage;
public  Skin skin;

    public MPHomeScreen(MyFirstMpDemoMain game) {
        this.game = game;
        gameAssets = new GameAssets(game);
    }

    @Override
    public void show() {
        font = gameAssets.getSgx().getFont("font");
        this.stage = new Stage(new FitViewport(500,500));
        Gdx.input.setInputProcessor(this.stage);
        skin = gameAssets.getSgx();

        ImageTextButton joinGameButtom = new ImageTextButton("Join Game!!!", skin);
        joinGameButtom.setPosition(250 - joinGameButtom.getWidth()/2, 225);
        stage.addActor(joinGameButtom);

        joinGameButtom.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                RoomPacket roomPacket = new RoomPacket(RoomEnum.QUE);
                //roomPacket.roomEnum = RoomEnum.QUE; <-- redundant
                game.clientWS.webSocket.send(roomPacket);
                System.out.println("packet sent to server to add you to que...");
                super.clicked(event, x, y);
            }
        });

    }
    String string = new String("hello \nthis is The Multiplayer Home/Lobby Screen");
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.75f, .5f, .5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();

        font.draw(game.getBatch(),string , 85, 75);
        game.getBatch().end();

            if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
                font.setColor(.75f,.75f,.5f,.75f);
                string = "in que";

            }
        stage.draw();
        stage.act();
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
    stage.dispose();
        font.dispose();
    }
}
