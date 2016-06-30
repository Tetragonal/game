package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.time.TimeGame;

public class AggroEnemyTest extends WanderingEnemy{

	private float testProjectileSpawn = 0;
	private float testProjectileCounter = 0;
	
	public AggroEnemyTest(TiledMapTileLayer collisionLayer, float x, float y, Texture texture) {
		super(collisionLayer, x, y, texture);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void act(float delta){
		super.act(delta);
		
		testProjectileSpawn += delta;
		if(testProjectileSpawn > .1){
			fireProjectile(0,0,10,75,testProjectileCounter%36*10, TimeGame.assets.get("img/laser.png"));
			testProjectileCounter++;
			testProjectileSpawn = 0;
		}
		
	}
	
	
}
