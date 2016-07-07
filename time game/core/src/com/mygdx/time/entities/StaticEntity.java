package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class StaticEntity extends PhysicsEntity {
	
	public StaticEntity(MapLayer collisionLayer, float x, float y, Texture texture, World world, String name) {
		super(collisionLayer, x, y, texture, world, name);
		body.setType(BodyType.StaticBody);
	}

}
