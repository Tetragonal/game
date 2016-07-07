package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

public class WanderingEnemy extends Mob{

	public WanderingEnemy(MapLayer collisionLayer, float x, float y, Texture texture, String name) {
		super(collisionLayer, x, y, texture, name);
	}
	
	public WanderingEnemy(MapLayer collisionLayer, float x, float y, Texture texture, World world, String name) {
		super(collisionLayer, x, y, texture, name);
		createBody(x, y, world);
		body.applyLinearImpulse(10, 0, body.getPosition().x + body.getLocalCenter().x, body.getPosition().y + body.getLocalCenter().y, true);
	}

	public void act(float delta){
		if(MathUtils.random(1000) > 992){
			worldDestination.set(sprite.getX()+sprite.getWidth()/2+MathUtils.random(-50, 50), sprite.getY()+sprite.getHeight()/2+MathUtils.random(-50,50));
		}
		super.act(delta);
	}
	
}
