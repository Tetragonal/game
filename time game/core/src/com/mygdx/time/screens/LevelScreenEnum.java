package com.mygdx.time.screens;

public enum LevelScreenEnum {
	//temporary
	
	SewersMap1 {
		public LevelScreen setScreen(String previousMap) {
			LevelScreen SewersMap1 = new LevelScreen(previousMap, "SewersMap1", "map/SewersMap1.tmx");
			SewersMap1.musicFile = "sound/goreshit - zomb.mp3";
			SewersMap1.musicVolume = 0.2f;
			SewersMap1.startX = 5;
			SewersMap1.startY = 2;
			return SewersMap1;
		}
	},
	
	map_1 {
		public LevelScreen setScreen(String previousMap) {
			LevelScreen mapOne = new LevelScreen(previousMap, "map_1", "map/test map.tmx");
			mapOne.musicFile = "sound/fourseasons.mp3";
			mapOne.musicVolume = 0.2f;
			mapOne.startX = 5;
			mapOne.startY = 2;
			return mapOne;
		}
	},
	map_2 {
		public LevelScreen setScreen(String previousMap) {
			LevelScreen mapTwo = new LevelScreen(previousMap, "map_2", "map/test map 2.tmx");
			mapTwo.musicFile = "sound/castaway.mp3";
			mapTwo.musicVolume = 0.2f;
			mapTwo.startX = 5;
			mapTwo.startY = 2;
			return mapTwo;
		}
	},
	map_3 {
		public LevelScreen setScreen(String previousMap) {
			LevelScreen mapThree = new LevelScreen(previousMap, "map_3", "map/test map 3.tmx");
			mapThree.musicFile = "sound/fourseasons.mp3";
			mapThree.musicVolume = 0.2f;
			mapThree.startX = 15;
			mapThree.startY = 5;
			return mapThree;
		}
	},
	Sandbox {
		public LevelScreen setScreen(String previousMap) {
			LevelScreen sandbox = new LevelScreen(previousMap, "map_3", "map/Sandbox.tmx");
			sandbox.musicFile = "sound/fourseasons.mp3";
			sandbox.musicVolume = 0.2f;
			sandbox.startX = 5;
			sandbox.startY = 2;
			return sandbox;
		}
	}
	;
	
	public abstract LevelScreen setScreen(String previousMap);
	
}
