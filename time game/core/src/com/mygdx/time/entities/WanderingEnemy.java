package com.mygdx.time.entities;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.screens.GameStage;

public class WanderingEnemy extends Mob{
	public WanderingEnemy(float x, float y, GameStage gameStage, String entityName, boolean isAirborne) {
		super(x, y, gameStage, entityName, isAirborne);
		baseMovementSpeed = 3;
	}

	public void act(float delta){
		if(MathUtils.random(1000) > 984){
			worldDestination.set(getX()+sprite.getWidth()/2+MathUtils.random(-75, 75)/25f, getY()+sprite.getHeight()/2+MathUtils.random(-75,75)/25f);
		}
		super.act(delta);
	}
	
}
