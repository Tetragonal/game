package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.time.TimeGame;
import com.mygdx.time.screens.GameStage;

public class AggroEnemyTest extends WanderingEnemy{

	private float testProjectileSpawn = 0;
	private float testProjectileCounter = 0;
	
	public AggroEnemyTest(float x, float y, Texture texture, GameStage stage, String name, boolean isAirborne) {
		super(x, y, texture, stage, name, isAirborne);
	}

	@Override
	public void act(float delta){
		super.act(delta);
		
		testProjectileSpawn += delta;
		if(testProjectileSpawn > .1){
			fireProjectile(0,0,10,2,testProjectileCounter%36*10, TimeGame.assets.get("img/laser.png"), gameStage);
			testProjectileCounter++;
			testProjectileSpawn = 0;
		}
		
	}
	
	
}
