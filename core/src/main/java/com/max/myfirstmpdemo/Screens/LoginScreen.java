package com.max.myfirstmpdemo.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.max.myfirstmpdemo.GameAssetsAndStuff.GameAssets;
import com.max.myfirstmpdemo.LoadingPathsAndScreen.SkinPaths;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class LoginScreen extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private MyFirstMpDemoMain game;
    private GameAssets gameAssets;

    public LoginScreen(MyFirstMpDemoMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        gameAssets = new GameAssets(game);
        stage = new Stage(new ScreenViewport());
        skin = gameAssets.getSgx();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Label signupLabel = new Label("SIGN UP?", skin);
        signupLabel.setEllipsis("...");
        signupLabel.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("''Sign Up?'' Label clicked");
            }
        });
        table.add(signupLabel).colspan(4);

        table.row();
        TextField textField = new TextField("User Name", skin);
        textField.setAlignment(Align.center);

        table.add(textField).align(Align.left);

        Label saveLabel = new Label("Save:", skin);
        saveLabel.setEllipsis("...");
        table.add(saveLabel).align(Align.right);

        CheckBox checkBox = new CheckBox("Save:", skin);
        table.add(checkBox).align(Align.left);

        table.row();
       TextField textFieldPass = new TextField("password", skin);
        textFieldPass.setAlignment(Align.center);
        textFieldPass.isPasswordMode();
        table.add(textFieldPass);

        saveLabel = new Label("Save:", skin);
        saveLabel.setEllipsis("...");
        table.add(saveLabel).align(Align.right);

        checkBox = new CheckBox(null, skin);
        table.add(checkBox).align(Align.left);

        table.row();
        TextButton textButtonLogin = new TextButton("Login", skin);
        table.add(textButtonLogin);

        table.row();
        ImageTextButton imageTextButton = new ImageTextButton("Google", skin);
        table.add(imageTextButton);

        imageTextButton = new ImageTextButton("Facebook", skin);
        table.add(imageTextButton).align(Align.left);

        imageTextButton = new ImageTextButton("Apple", skin);
        table.add(imageTextButton);

        stage.addActor(table);

        textField.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
                            table.getCell(textFieldPass).getActor().setText("poopy");
                }
                return true;
            }
        });

        textButtonLogin.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("HI u clicked login button :)");
                game.clientWS.init();
                //Gdx.app.postRunnable(()-> game.setScreen(game.mpHomeScreen));
                System.out.println("new Screen");

            }
        });

        /*stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("Skins/sgx-ui.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        table.add();

        Button button = new Button(skin);
        table.add(button);

        table.add();

        table.row();
        table.add();

        ImageTextButton imageTextButton = new ImageTextButton("asdfadsf", skin);
        table.add(imageTextButton);

        table.add();

        table.row();
        table.add();

        Label label = new Label("Lorem ipsum", skin);
        table.add(label);

        table.add();
        stage.addActor(table);*/


    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}
