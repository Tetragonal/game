package com.mygdx.time.brains;

import com.mygdx.time.Game;
import com.mygdx.time.entities.Mob;
import com.mygdx.time.manager.EntityLoader;
import com.mygdx.time.screens.GameStage;

public class AggroEnemyTestBrain extends WanderingBrain {
	public AggroEnemyTestBrain(String[] args) {
		super(args);
	}

	private float testProjectileSpawn = 0;
	private float testProjectileCounter = 0;
	
	String projectileName = null;
	
	public void act(){
		if(projectileName == null){
			projectileName = EntityLoader.getJSONArray(parentEntity.getName(), "attacks", Game.MOB).getString(0);
		}
		
		testProjectileSpawn += 1/Game.ENGINE_FPS;
		if(testProjectileSpawn > .1){
			for(int i=0; i<5; i++){
				((Mob) parentEntity).fireProjectile(0,0,25,15,testProjectileCounter%36*10+i*13, (GameStage) parentEntity.getStage(), "EnemyLaser", false);
			}
			testProjectileCounter++;
			testProjectileSpawn = 0;
		}
	super.act();
	
	}
}
