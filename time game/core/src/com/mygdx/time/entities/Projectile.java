package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.time.screens.KinematicEntity;

public class Projectile extends KinematicEntity{

	private float damage;
	private float duration;
	private float timer = 0;
	
	public Projectile(MapLayer collisionLayer, float x, float y, float damage, float speed, float angleDeg, float duration, Texture texture, World world, String name) {
		super(collisionLayer, x, y, texture, world, name);
		this.damage = damage;
		this.duration = duration;
		maxSpeed = speed;
		body.setBullet(true);
		worldDestination.set(x+100000*MathUtils.cosDeg(angleDeg), y+100000*MathUtils.sinDeg(angleDeg));
		sprite.setOrigin(0, sprite.getHeight()/2);
		sprite.rotate(angleDeg);
		setOriginY(getHeight()/2);
		rotateBy(angleDeg);
		body.setTransform(x, y, angleDeg*MathUtils.degreesToRadians);
		body.setLinearVelocity(maxSpeed*MathUtils.cosDeg(angleDeg), maxSpeed*MathUtils.sinDeg(angleDeg));
		
	}

	public float getDamage(){
		return damage;
	}
	
	@Override
	public void act(float delta){
	    body.setFixedRotation(false);
		timer += delta;
		if(timer > duration){
	          world.destroyBody(body);
	            body.setUserData(null);
	            body = null;
			this.remove();
		}else{
			super.act(delta);
		}
	}

}
