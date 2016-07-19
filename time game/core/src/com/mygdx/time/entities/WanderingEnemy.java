package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.screens.GameStage;

public class WanderingEnemy extends Mob{

	public WanderingEnemy(float x, float y, Texture texture, String name, boolean isAirborne) {
		super(x, y, texture, name, isAirborne);
	}
	
	public WanderingEnemy(float x, float y, Texture texture, GameStage gameStage, String entityName, boolean isAirborne) {
		super(x, y, texture, entityName, isAirborne);
		this.gameStage = gameStage;
		createBody(x, y, gameStage.getWorld());
		body.applyLinearImpulse(10, 0, body.getPosition().x + body.getLocalCenter().x, body.getPosition().y + body.getLocalCenter().y, true);
	}

	public void act(float delta){
		if(MathUtils.random(1000) > 992){
			worldDestination.set(sprite.getX()+sprite.getWidth()/2+MathUtils.random(-50, 50), sprite.getY()+sprite.getHeight()/2+MathUtils.random(-50,50));
		}
		super.act(delta);
	}
	
}
