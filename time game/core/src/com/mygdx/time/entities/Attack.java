package com.mygdx.time.entities;

import java.util.HashSet;

public class Attack extends CollidableEntity{
	Entity parentEntity;
	float damage;
	public HashSet<CollidableEntity> collidedEntities = new HashSet<CollidableEntity>();
	public Attack(float x, float y, String entityName, Entity parentEntity) {
		super(x, y, parentEntity.gameStage, entityName, true);
		this.parentEntity = parentEntity;
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		if(e instanceof Mob && !collidedEntities.contains(e)){
			collidedEntities.add(e);
			((Mob) e).takeDamage(damage);
		}
	}
	
}
