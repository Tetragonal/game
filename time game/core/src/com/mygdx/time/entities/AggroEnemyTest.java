package com.mygdx.time.entities;

import com.mygdx.time.screens.GameStage;

public class AggroEnemyTest extends WanderingEnemy{

	private float testProjectileSpawn = 0;
	private float testProjectileCounter = 0;
	
	public AggroEnemyTest(float x, float y, GameStage stage, String name, boolean isAirborne) {
		super(x, y, stage, name, isAirborne);
	}

	@Override
	public void act(float delta){
		super.act(delta);
		
		testProjectileSpawn += delta;
		if(testProjectileSpawn > .1){
			for(int i=0; i<3; i++){
				fireProjectile(0,0,0,2,testProjectileCounter%36*10+i*13, gameStage, "ENEMY_LASER");
			}
			testProjectileCounter++;
			testProjectileSpawn = 0;
		}
		
	}
	
	
}
