package com.mygdx.time.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.time.combat.Buff;
import com.mygdx.time.combat.DamageCalculator;
import com.mygdx.time.manager.CollisionHandler;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

public class Projectile extends CollidableEntity{

	private float damage;
	private float duration;
	private float timer = 0;
	
	public Projectile(float x, float y, float damage, float speed, float angleDeg, float duration, GameStage gameStage, String entityName, boolean isAlly) {
		super(x, y, gameStage, entityName, true);
		this.damage = damage;
		this.duration = duration;
		modifiedMovementSpeed = (int)speed;
		worldDestination.set(x+100000*MathUtils.cosDeg(angleDeg), y+100000*MathUtils.sinDeg(angleDeg));
		this.setOrigin(0, sprite.getHeight()/2);
		sprite.rotate(angleDeg);
		rotation = angleDeg;
		rotateBy(angleDeg);
		float radians = angleDeg*MathUtils.degreesToRadians;
		this.setPosition(x+(float)Math.sin(radians)*sprite.getHeight()/2, y-(float)Math.cos(radians)*sprite.getHeight()/2);
		sprite.setPosition(x+(float)Math.sin(radians)*sprite.getHeight()/2, y-(float)Math.cos(radians)*sprite.getHeight()/2);
		//testing
		if(isAlly){
			CollisionHandler.getInstance().allyAttackList.add(this);
		}else{
			CollisionHandler.getInstance().enemyAttackList.add(this);
		}
		//too lazy to make actual bounding box calc
		//should be used for all rotated sprites
		float max = Math.max(sprite.getWidth(), sprite.getHeight());
		boundingBox.set(getX()-max, getY()-max, 2*max, 2*max);
	}

	public float getDamage(){
		return damage;
	}
	
	@Override
	public void act(float delta){
		timer += delta;
		if(timer > duration){
			flagForDelete();
		}else{
			super.act(delta);
		}
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		super.collideWith(e);
		flagForDelete();
		System.out.println("Proj collided");
		if(e instanceof Mob){
			((Mob)e).takeDamage(DamageCalculator.calculateDamage((Mob)e, DamageCalculator.PHYSICAL, damage));
			//Buff b = new Buff(60*10);
			//b.flatArmor = -1;
			//((Mob)e).addBuff(b);
			//System.out.println(e.getName() + " has " + ((Mob) e).buffList.size() + " stacks of debuff");
			//Game.console = e.getName() + " has " + ((Mob) e).buffList.size() + " stacks of debuff\n";
		}
	}
	
	@Override
	public void flagForDelete(){
		super.flagForDelete();
	}
}
