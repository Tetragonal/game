package com.mygdx.time.manager;

import com.mygdx.time.screens.MapTwo;
import com.mygdx.time.screens.MapOne;
import com.mygdx.time.screens.MapThree;
import com.mygdx.time.screens.LevelScreen;

public enum LevelScreenEnum {
	
	map_1 {
		public LevelScreen setScreen(Object... params) { return new MapOne(); }
	},
	map_2 {
		public LevelScreen setScreen(Object... params) { return new MapTwo(); }
	},
	map_3 {
		public LevelScreen setScreen(Object... params) { return new MapThree(); }
	}
	;
	
	public abstract LevelScreen setScreen(Object... params);
	
}
