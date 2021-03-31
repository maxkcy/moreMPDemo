package com.max.myfirstmpdemo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.max.myfirstmpdemo.Screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyFirstMpDemoMain extends Game {


	private SpriteBatch batch;
	private AssetManager assetManager;
	private SplashScreen splashScreen;
	public ClientWS clientWS;
	public LoginScreen loginScreen;
	public MPHomeScreen mpHomeScreen;



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


	}

	@Override
	public void render() {

		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
		WebSockets.closeGracefully(clientWS.webSocket);
		try{clientWS.webSocket.close();
			super.dispose();
		}catch (Exception exception){
			System.out.println(exception);
		}
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}