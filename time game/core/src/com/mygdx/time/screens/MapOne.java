package com.mygdx.time.screens;

public class MapOne extends LevelScreen{

	public MapOne(String previousMap, String currentLevel) {
		super(previousMap, currentLevel);
	}

	public void show(){
		mapFile = "map/test map.tmx";
		musicFile = "sound/fourseasons.mp3";
		musicVolume = 0.2f;
		startX = 5;
		startY = 2;
		super.show();
	}
}
