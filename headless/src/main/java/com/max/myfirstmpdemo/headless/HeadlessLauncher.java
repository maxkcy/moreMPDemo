package com.max.myfirstmpdemo.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;


/** Launches the headless application. Can be converted into a utilities project or a server application. */
public class HeadlessLauncher {
	public static void main(String[] args) {
		createApplication();
	}

	private static Application createApplication() {
		// Note: you can use a custom ApplicationListener implementation for the headless project instead of MyFirstMpDemoMain.
		return new HeadlessApplication(new ServerMain(), getDefaultConfiguration());
	}

	private static HeadlessApplicationConfiguration getDefaultConfiguration() {
		HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
		configuration.renderInterval = 1/20f; // When this value is negative, MyFirstMpDemoMain#render() is never called.
		return configuration;
	}
}