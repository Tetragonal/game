package com.mygdx.time.brains;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.time.entities.CollidableEntity;

public class NpcWanderingBrain extends Brain{
	
	Vector2 anchor;
	
	@Override
	public void setParent(CollidableEntity collidableEntity){
		super.setParent(collidableEntity);
		anchor = collidableEntity.getSpriteCenter();
		//unfinished
	}
	
	public void act(){
		if(MathUtils.randomBoolean(0.016f)){
			parentEntity.worldDestination.set(body.getPosition().x+body.getLocalCenter().x+MathUtils.random(-75, 75)/25f, body.getPosition().y+body.getLocalCenter().y+MathUtils.random(-75,75)/25f);
		}
	}
}
