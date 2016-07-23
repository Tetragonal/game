package com.mygdx.time.entities;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.time.screens.GameStage;

public class KinematicEntity extends PhysicsEntity{
	public KinematicEntity(float x, float y, GameStage gameStage, String name) {
		super(x, y, gameStage, name);
		body.setType(BodyType.KinematicBody);
		body.setLinearVelocity(2, 0);
	}
	
}
