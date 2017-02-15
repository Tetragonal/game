package com.mygdx.time.manager;

import com.badlogic.gdx.Gdx;
import com.mygdx.time.TimeGame;
import com.mygdx.time.screens.LevelScreen;
import com.mygdx.time.screens.LevelScreenEnum;

public class LevelScreenManager {
	
	// Singleton: unique instance
	private static LevelScreenManager instance;
	
	// Singleton: private constructor
	private LevelScreenManager() {
		super();
	}
	
	// Singleton: retrieve instance
	public static LevelScreenManager getInstance() {
		if (instance == null) {
			instance = new LevelScreenManager();
		}
		return instance;
	}
	
	/** Sets screen by searching through the enum for the keyword */
	public void setScreen(String mapName, String previousScreen) {
		System.out.println(mapName + " " + previousScreen);
		LevelScreen newScreen = LevelScreenEnum.valueOf(mapName).setScreen(previousScreen);
		((TimeGame)Gdx.app.getApplicationListener()).setScreen(newScreen);
	}
}