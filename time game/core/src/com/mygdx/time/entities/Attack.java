package com.mygdx.time.entities;

import java.util.HashSet;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.time.TimeGame;

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
			Sound tpSound = TimeGame.assets.get("sound/warp2.ogg");
			tpSound.play();
		}
	}
	
}
