package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.screens.GameStage;

public class Projectile extends PhysicsEntity{

	private float damage;
	private float duration;
	private float timer = 0;
	
	public Projectile(float x, float y, float damage, float speed, float angleDeg, float duration, Texture texture, GameStage gameStage, String entityName) {
		super(x, y, texture, gameStage, entityName);
		this.damage = damage;
		this.duration = duration;
		maxSpeed = speed;
		body.setBullet(true);
		worldDestination.set(x+100000*MathUtils.cosDeg(angleDeg), y+100000*MathUtils.sinDeg(angleDeg));
//		sprite.setOrigin(0, sprite.getHeight()/2);
		sprite.rotate(angleDeg);
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
			flagForDelete();
		}else{
			super.act(delta);
		}
	}
}
