package com.mygdx.time.entities;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.screens.GameStage;

public class WanderingEnemy extends Mob{

	public WanderingEnemy(float x, float y, String entityName, boolean isAirborne) {
		super(x, y, entityName, isAirborne);
	}
	
	public WanderingEnemy(float x, float y, GameStage gameStage, String entityName, boolean isAirborne) {
		this(x, y, entityName, isAirborne);
		this.gameStage = gameStage;
		createBody(x, y, gameStage.getWorld());
		body.applyLinearImpulse(10, 0, body.getPosition().x + body.getLocalCenter().x, body.getPosition().y + body.getLocalCenter().y, true);
	}

	public void act(float delta){
		if(MathUtils.random(1000) > 992){
			worldDestination.set(body.getPosition().x+body.getLocalCenter().x+MathUtils.random(-50, 50)/25f, body.getPosition().y+body.getLocalCenter().y+MathUtils.random(-50,50)/25f);
		}
		super.act(delta);
	}
	
}
