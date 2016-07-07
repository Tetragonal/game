package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.time.TimeGame;

public class AggroEnemyTest extends WanderingEnemy{

	private float testProjectileSpawn = 0;
	private float testProjectileCounter = 0;
	
	public AggroEnemyTest(MapLayer collisionLayer, float x, float y, Texture texture, World world, String name) {
		super(collisionLayer, x, y, texture, name);
		createBody(x, y, world);
	}

	@Override
	public void act(float delta){
		super.act(delta);
		
		testProjectileSpawn += delta;
		if(testProjectileSpawn > .1){
			fireProjectile(0,0,10,2,testProjectileCounter%36*10, TimeGame.assets.get("img/laser.png"), world);
			testProjectileCounter++;
			testProjectileSpawn = 0;
		}
		
	}
	
	
}
