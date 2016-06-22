package com.mygdx.time.screens;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapThree extends LevelScreen{

	public void show(){
		map = new TmxMapLoader().load("map/test map 3.tmx");
		startX = 15;
		startY = 5;
		super.show();
	}
}
