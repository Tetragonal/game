package com.mygdx.time.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.time.TimeGame;
import com.mygdx.time.screens.LevelScreen;

public class LevelScreenManager {
	
	// Singleton: unique instance
	private static LevelScreenManager instance;
	
	// Reference to game
	private Game game;
	
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
	
	// Initialization with the game class
	public void initialize() {
		this.game = (TimeGame)Gdx.app.getApplicationListener();
	}
	
	// Show in the game the screen which enum type is received
	public void setScreen(LevelScreenEnum screenEnum, Object... params) {
		
//		// Get current screen to dispose it
//		Screen currentScreen = game.getScreen();
		
		// Show new screen
		LevelScreen newScreen = screenEnum.setScreen(params);
//		newScreen.buildStage(); //builds actors
		game.setScreen(newScreen);
		
//		// Dispose previous screen
//		if (currentScreen != null) {
//			currentScreen.dispose();
//		}
	}
}