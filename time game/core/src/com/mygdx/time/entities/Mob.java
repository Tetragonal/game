package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.time.screens.KinematicEntity;

public class Mob extends PhysicsEntity{
	
	private int health;
	private int maxHealth = 500;
	
	public Mob(MapLayer collisionLayer, float x, float y, Texture texture, String name) {
		super(collisionLayer, x, y, texture, name);
		health = maxHealth;
	}
	public Mob(MapLayer collisionLayer, float x, float y, Texture texture, World world, String name) {
		super(collisionLayer, x, y, texture, name);
		health = maxHealth;
		createBody(x, y, world);
	}
	
	public void fireProjectile(float offsetX, float offsetY, float damage, float speed, float angleDeg, Texture texture, World world){
		Projectile projectile = new Projectile(collisionLayer, getX()+sprite.getWidth()/2+offsetX, getY()+sprite.getHeight()/2+offsetY, damage, speed, angleDeg, 3, texture, world, "laser");
		getStage().addActor(projectile);
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		if(health <= 0){
			isFlaggedForDelete = true;
		}
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
