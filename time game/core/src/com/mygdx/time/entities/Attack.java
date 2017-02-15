package com.mygdx.time.entities;

import com.mygdx.time.screens.GameStage;

public class Attack extends CollidableEntity{
	int damage;
	Entity parentEntity;
	
	public Attack(float x, float y, GameStage gameStage, String entityName) {
		super(x, y, gameStage, entityName, false);
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		if(e instanceof Mob){
			((Mob) e).takeDamage(damage);
		}
	}
	
}
