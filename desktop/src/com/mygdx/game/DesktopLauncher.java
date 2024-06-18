package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.Skyrim2DGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setIdleFPS(60);
		//config.setResizable(false);
		config.setTitle("Skyrim2D");
		config.setWindowedMode(1920, 1080);
		config.useVsync(true);
		new Lwjgl3Application(new Skyrim2DGame(), config);
	}
}
