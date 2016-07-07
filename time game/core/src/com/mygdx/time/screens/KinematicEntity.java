package com.mygdx.time.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.time.entities.PhysicsEntity;

public class KinematicEntity extends PhysicsEntity{
	public KinematicEntity(MapLayer collisionLayer, float x, float y, Texture texture, World world, String name) {
		super(collisionLayer, x, y, texture, world, name);
		body.setType(BodyType.KinematicBody);
		body.setLinearVelocity(2, 0);
	}
	
}
