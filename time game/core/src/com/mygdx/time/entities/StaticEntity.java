package com.mygdx.time.entities;

import com.mygdx.time.screens.GameStage;

public class StaticEntity extends CollidableEntity {
	
	public StaticEntity(float x, float y, GameStage gameStage, String name) {
		super(x, y, gameStage, name, false);
		//body.setType(BodyType.StaticBody);
	}

}
