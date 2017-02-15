package com.mygdx.time.entities;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.screens.GameStage;

public class WanderingEnemy extends Mob{
	public WanderingEnemy(float x, float y, GameStage gameStage, String entityName, boolean isAirborne) {
		super(x, y, gameStage, entityName, isAirborne);
		//createBody(x, y, false);
		body.applyLinearImpulse(10, 0, body.getPosition().x + body.getLocalCenter().x, body.getPosition().y + body.getLocalCenter().y, true);
		baseMovementSpeed = 3;
	}

	public void act(float delta){
		if(MathUtils.random(1000) > 984){
			worldDestination.set(body.getPosition().x+body.getLocalCenter().x+MathUtils.random(-75, 75)/25f, body.getPosition().y+body.getLocalCenter().y+MathUtils.random(-75,75)/25f);
		}
		super.act(delta);
	}
	
}
