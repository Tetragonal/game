package com.mygdx.time.screens;

public enum LevelScreenEnum {
	
	//idea: add the data from MapOne, MapTwo, etc into the level screen enum and remove the classes entirely?
	
	map_1 {
		public LevelScreen setScreen(String previousMap) { return new MapOne(previousMap, "map_1"); }
	},
	map_2 {
		public LevelScreen setScreen(String previousMap) { return new MapTwo(previousMap, "map_2"); }
	},
	map_3 {
		public LevelScreen setScreen(String previousMap) { return new MapThree(previousMap, "map_3"); }
	},
	Sandbox {
		public LevelScreen setScreen(String previousMap) { return new MapThree(previousMap, "Sandbox"); }
	}
	;
	
	public abstract LevelScreen setScreen(String previousMap);
	
}
