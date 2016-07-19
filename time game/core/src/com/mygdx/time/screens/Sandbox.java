package com.mygdx.time.screens;

public class Sandbox extends LevelScreen{

	public Sandbox(String previousMap, String currentLevel) {
		super(previousMap, currentLevel);
	}

	public void show(){
		mapFile = "map/Sandbox.tmx";
		musicFile = "sound/fourseasons.mp3";
		musicVolume = 0.2f;
		startX = 5;
		startY = 2;
		super.show();
	}
}
