package com.max.myfirstmpdemo.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.websocket.GwtWebSockets;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
	GwtApplicationConfiguration cfg;
	private static final int PADDING = 100;

	@Override
		public GwtApplicationConfiguration getConfig () {
			Window.enableScrolling(false);
			Window.setMargin("0");
			Window.addResizeHandler(new ResizeListener());

			// Resizable application, uses available space in browser
			//return new GwtApplicationConfiguration(true);
			// Fixed size application:

			return new GwtApplicationConfiguration(Window.getClientWidth() - PADDING, Window.getClientHeight() /*- PADDING*/);
			//int w = Window.getClientWidth() - PADDING;
			//int h = Window.getClientHeight() - PADDING;
			//cfg = new GwtApplicationConfiguration(w, h);
			//return cfg;
		}

	class ResizeListener implements ResizeHandler {
		@Override
		public void onResize(ResizeEvent event) {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			} else {
				int width = event.getWidth() - PADDING;
				int height = event.getHeight() /*- PADDING*/;
				getRootPanel().setWidth("" + width + "px");
				getRootPanel().setHeight("" + height + "px");
				getApplicationListener().resize(width, height);
				Gdx.graphics.setWindowedMode(width, height);
			}
		}
	}

		@Override
		public ApplicationListener createApplicationListener () {
			GwtWebSockets.initiate();
			return new MyFirstMpDemoMain();
		}


}
