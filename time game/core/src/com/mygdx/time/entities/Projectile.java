package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;

public class Projectile extends CollidibleEntity{

	private float damage;
	private float angleDeg;
	
	public Projectile(TiledMapTileLayer collisionLayer, float x, float y, float damage, float speed, float angleDeg, Texture texture) {
		super(collisionLayer, x, y, texture);
		this.debug();
		this.damage = damage;
		this.angleDeg = angleDeg;
		
		maxSpeed = speed;
		worldDestination.set(x+100000*MathUtils.cosDeg(angleDeg), y+100000*MathUtils.sinDeg(angleDeg));
		
//		sprite.setOrigin(getWidth()/2,getHeight()/2);
		sprite.setOrigin(0, sprite.getHeight()/2);
		sprite.rotate(angleDeg);
		setOriginY(getHeight()/2);
		rotateBy(angleDeg);
		
	}

	public float getDamage(){
		return damage;
	}
	
	@Override
	public void act(float delta){
		if(collidesTop() || collidesBottom() || collidesLeft() || collidesRight()){
			this.remove();
		}
		super.act(delta);
	}

}
