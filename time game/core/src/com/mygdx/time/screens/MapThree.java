package com.mygdx.time.screens;

public class MapThree extends LevelScreen{

	public MapThree(String previousMap, String currentLevel) {
		super(previousMap, currentLevel);
	}

	public void show(){
		mapFile = "map/test map 3.tmx";
		musicFile = "sound/fourseasons.mp3";
		musicVolume = 0.2f;
		startX = 15;
		startY = 5;
		super.show();
	}
}
