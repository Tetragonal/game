package com.mygdx.time.combat;

import java.util.HashSet;

import com.mygdx.time.Game;
import com.mygdx.time.entities.CollidableEntity;
import com.mygdx.time.entities.Entity;
import com.mygdx.time.entities.Mob;

public class Attack extends CollidableEntity{
	public Entity parentEntity;
	public float damage;
	public double range;
	public HashSet<CollidableEntity> collidedEntities = new HashSet<CollidableEntity>();
	public Attack(float x, float y, String entityName, Entity parentEntity, boolean isAlly) {
		super(x, y, parentEntity.gameStage, entityName, Game.ATTACK, true);
		if(isAlly){
			createBody(x, y, !isSolid, Game.CATEGORY_ALLY_ATTACK, Game.MASK_ALLY_ATTACK);
		}else{
			createBody(x, y, !isSolid, Game.CATEGORY_ENEMY_ATTACK, Game.MASK_ENEMY_ATTACK);
		}
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
