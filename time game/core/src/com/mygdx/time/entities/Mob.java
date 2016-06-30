package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Mob extends CollidibleEntity{
	
	private int health;
	private int maxHealth = 500;
	
	public Mob(TiledMapTileLayer collisionLayer, float x, float y, Texture texture) {
		super(collisionLayer, x, y, texture);
		health = maxHealth;
		// TODO Auto-generated constructor stub
		
	}
	
	public void fireProjectile(float offsetX, float offsetY, float damage, float speed, float angleDeg, Texture texture){
		Projectile projectile = new Projectile(collisionLayer, location.x+sprite.getWidth()/2+offsetX, location.y+sprite.getHeight()/2+offsetY, damage, speed, angleDeg, texture);
		getStage().addActor(projectile);
	}

	public void takeDamage(float damage){
		health -= damage;
	}
	
	public void heal(float healing){
		health += healing;
	}
	
	public float getHealth(){
		return health;
	}
	
	public float getMaxHealth(){
		return maxHealth;
	}

}
