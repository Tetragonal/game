package com.mygdx.time.brains;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.entities.CollidableEntity;

public class WanderingBrain extends Brain{
	
	int startingLocation;
	
	float wanderChance;
	float wanderDistance;
	
	public WanderingBrain(String[] args) {
		wanderChance = Float.parseFloat(args[0]);
		wanderDistance = Float.parseFloat(args[1]);
	}

	@Override
	public void setParent(CollidableEntity collidableEntity){
		super.setParent(collidableEntity);
	}
	
	public void act(){
		if(MathUtils.randomBoolean(wanderChance)){
			parentEntity.worldDestination.set(body.getPosition().x+body.getLocalCenter().x+MathUtils.random(-wanderDistance,wanderDistance), body.getPosition().y+body.getLocalCenter().y+MathUtils.random(-wanderDistance,wanderDistance));
		}
	}
}
