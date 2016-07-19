package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.time.screens.GameStage;

public class KinematicEntity extends PhysicsEntity{
	public KinematicEntity(float x, float y, Texture texture, GameStage gameStage, String name) {
		super(x, y, texture, gameStage, name);
		body.setType(BodyType.KinematicBody);
		body.setLinearVelocity(2, 0);
	}
	
}
