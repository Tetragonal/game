package com.mygdx.time.brains;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.time.entities.CollidableEntity;

public class Brain {
	public CollidableEntity parentEntity;
	public Body body;
	
	public void setParent(CollidableEntity parentEntity){
		this.parentEntity = parentEntity;
		body = parentEntity.getBody();
	}
	
	public void act(){
		
	}
}
