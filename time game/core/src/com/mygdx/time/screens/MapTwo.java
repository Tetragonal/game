package com.mygdx.time.screens;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapTwo extends LevelScreen{

	public void show(){
		map = new TmxMapLoader().load("map/test map 2.tmx");
		startX = 5;
		startY = 2;
		super.show();
	}
}
