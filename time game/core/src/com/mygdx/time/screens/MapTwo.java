package com.mygdx.time.screens;

public class MapTwo extends LevelScreen{

	public MapTwo(String previousMap, String currentLevel) {
		super(previousMap, currentLevel);
	}

	public void show(){
		mapFile = "map/test map 2.tmx";
		musicFile = "sound/castaway.mp3";
		musicVolume = 0.2f;
		startX = 5;
		startY = 2;
		super.show();
	}
}
