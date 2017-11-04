package com.mygdx.time.entities;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.Game;
import com.mygdx.time.combat.Buff;
import com.mygdx.time.combat.BuffEnum;
import com.mygdx.time.combat.DamageCalculator;
import com.mygdx.time.manager.EntityLoader;
import com.mygdx.time.screens.GameStage;

public class Projectile extends CollidableEntity{

	private float damage;
	private float duration;
	private float timer = 0;
	
	private String onHit;
	private int onHitParam;
	
	public Projectile(float x, float y, float damage, float speed, float angleDeg, float duration, GameStage gameStage, String entityName, boolean isAlly) {
		super(x, y, gameStage, entityName, Game.PROJECTILE, true);
		if(isAlly){
			createBody(x, y, !isSolid, Game.CATEGORY_ALLY_ATTACK, Game.MASK_ALLY_ATTACK);
		}else{
			createBody(x, y, !isSolid, Game.CATEGORY_ENEMY_ATTACK, Game.MASK_ENEMY_ATTACK);
		}
		this.damage = damage;
		this.duration = duration;
		modifiedMovementSpeed = (int)speed;
		body.setBullet(true);
		worldDestination.set(x+Integer.MAX_VALUE*MathUtils.cosDeg(angleDeg), y+Integer.MAX_VALUE*MathUtils.sinDeg(angleDeg));
		sprite.rotate(angleDeg);
		rotateBy(angleDeg);
		//to make the center (0,sprite.getHeight()/2)
		float radians = angleDeg*MathUtils.degreesToRadians;
		body.setTransform(x+(float)Math.sin(radians)*sprite.getHeight()/2, y-(float)Math.cos(radians)*sprite.getHeight()/2, radians);
		body.setLinearVelocity(modifiedMovementSpeed*MathUtils.cosDeg(angleDeg), modifiedMovementSpeed*MathUtils.sinDeg(angleDeg));
		
		
		onHit = EntityLoader.getValue(entityName, "on hit", Game.PROJECTILE);
		
		try{
		onHitParam = (int)Float.parseFloat(EntityLoader.getValue(entityName, "on hit parameter", Game.PROJECTILE));
		}catch(Exception e){
		
		}
	}

	public float getDamage(){
		return damage;
	}
	
	@Override
	public void act(float delta){
	    body.setFixedRotation(false);
		timer += delta;
		if(timer > duration){
			isFlaggedForDelete = true;
		}else{
			super.act(delta);
		}
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		this.isFlaggedForDelete = true;
		if(e instanceof Mob){
			((Mob)e).takeDamage(DamageCalculator.calculateDamage((Mob)e, DamageCalculator.PHYSICAL, damage));
			
			if(!onHit.equals("")){
				Buff b = BuffEnum.valueOf(onHit).getBuff(new String[]{""+onHitParam});
				((Mob)e).addBuff(b);
				Game.console = e.getName() + " has " + ((Mob) e).buffList.size() + " stacks of debuff\n";
			}
			
			//System.out.println(e.getName() + " has " + ((Mob) e).buffList.size() + " stacks of debuff");
		}
	}
}
