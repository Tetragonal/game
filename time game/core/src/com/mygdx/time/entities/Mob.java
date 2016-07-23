package com.mygdx.time.entities;

import com.mygdx.time.screens.GameStage;

public class Mob extends PhysicsEntity{
	
	private int health;
	private int maxHealth = 500;
	MobHealthBar mobHealthBar = null;
	
	public Mob(float x, float y, String entityName, boolean isAirborne) {
		super(x, y, entityName);
		worldDestination.set(x+sprite.getWidth()/2, y+sprite.getHeight()/2);
		health = maxHealth;
	}
	public Mob(float x, float y, GameStage gameStage, String entityName, boolean isAirborne) {
		this(x, y, entityName, isAirborne);
		this.gameStage = gameStage;
		createBody(x, y, gameStage.getWorld());
	}
	
	public void fireProjectile(float offsetX, float offsetY, float damage, float speed, float angleDeg, GameStage gameStage, String entityName){
		Projectile projectile = new Projectile(getX()+sprite.getWidth()/2+offsetX, getY()+sprite.getHeight()/2+offsetY, damage, speed, angleDeg, 3, gameStage, entityName);
		getStage().addActor(projectile);
	}
	
	@Override
	public void act(float delta){
		if(mobHealthBar == null){
			mobHealthBar = new MobHealthBar(body.getPosition().x, body.getPosition().y-sprite.getHeight() +1, this);
		}
		super.act(delta);
		if(health <= 0){
			flagForDelete();
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

	public float getHealthPercent(){
		return health*1f/maxHealth;
	}
}
