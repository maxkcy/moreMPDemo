package com.max.myfirstmpdemo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.max.myfirstmpdemo.ClientWS.ClientWS;
import com.max.myfirstmpdemo.LoadingPathsAndScreen.Loader;
import com.max.myfirstmpdemo.Screens.LoginScreen;
import com.max.myfirstmpdemo.Screens.MPHomeScreen;
import com.max.myfirstmpdemo.Screens.RoomScreen;
import com.max.myfirstmpdemo.Screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyFirstMpDemoMain extends Game {


	private SpriteBatch batch;
	private AssetManager assetManager;
	private SplashScreen splashScreen;
	public ClientWS clientWS;
	public LoginScreen loginScreen;
	public MPHomeScreen mpHomeScreen;
	public RoomScreen roomScreen;


	public MyFirstMpDemoMain(SpriteBatch batch, AssetManager assetManager) {
		this.batch = batch;
		this.assetManager = assetManager;
	}

	public MyFirstMpDemoMain() {
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		splashScreen = new SplashScreen(this);


		clientWS = new ClientWS(this);

		Gdx.app.postRunnable(()-> setScreen(splashScreen));
		Gdx.input.setCatchKey(Input.Keys.SPACE, true);//disables gwt, android keys for scrolling

	}

	@Override
	public void render() {
		//Gdx.gl.glClearColor(.8f, 1, .9f, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); //works across all screens because this render is called before all screens aka super.render() which calls render from next screen
		}
		/*if(Gdx.input.isKeyPressed(Input.Keys.N)){
			Gdx.graphics.setWindowedMode(Gdx.graphics.)
		}*/ //needs work on later because of full screen bug, unable to press escape, and display on tv monitor gets stuck
		super.render();
	}

	@Override
	public void dispose() {
		System.out.println("dispose called");
		batch.dispose();
		assetManager.dispose();
		WebSockets.closeGracefully(clientWS.webSocket);
		try{clientWS.webSocket.close();
			System.out.println("Yay it works");
		}catch (Exception exception){
			System.out.println(exception);
			System.out.println("NOY it doesnt work :(");
		}
		splashScreen.dispose();
		loginScreen.dispose();
		mpHomeScreen.dispose();
		//clientWS.dispose();
		super.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}