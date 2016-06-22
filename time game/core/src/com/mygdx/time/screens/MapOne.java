package com.mygdx.time.screens;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapOne extends LevelScreen{

	public void show(){
		map = new TmxMapLoader().load("map/test map.tmx");
		startX = 5;
		startY = 2;
		super.show();
	}
}
