package com.mygdx.time.entities;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.combat.Buff;
import com.mygdx.time.combat.DamageCalculator;
import com.mygdx.time.manager.CollisionHandler;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

public class Projectile extends CollidableEntity{

	private float damage;
	private float duration;
	private float timer = 0;
	
	public Projectile(float x, float y, float damage, float speed, float angleDeg, float duration, GameStage gameStage, String entityName) {
		super(x, y, gameStage, entityName, false);
		this.damage = damage;
		this.duration = duration;
		modifiedMovementSpeed = (int)speed;
		body.setBullet(true);
		worldDestination.set(x+100000*MathUtils.cosDeg(angleDeg), y+100000*MathUtils.sinDeg(angleDeg));
		//sprite.setOrigin(0, sprite.getHeight()/2);
		//body.setTransform(0, sprite.getHeight()/2, 0);
		this.setOrigin(0, sprite.getHeight()/2);
		sprite.rotate(angleDeg);
		rotation = angleDeg;
		rotateBy(angleDeg);
		float radians = angleDeg*MathUtils.degreesToRadians;
		body.setTransform(x+(float)Math.sin(radians)*sprite.getHeight()/2, y-(float)Math.cos(radians)*sprite.getHeight()/2, radians);
		body.setLinearVelocity(modifiedMovementSpeed*MathUtils.cosDeg(angleDeg), modifiedMovementSpeed*MathUtils.sinDeg(angleDeg));
		//testing
		CollisionHandler.getInstance().enemyAttackList.add(this);
		
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
		body.setTransform(this.getX(), this.getY(), moveAngle*MathUtils.degreesToRadians);
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		flagForDelete();
		System.out.println("Proj collided");
		if(e instanceof Mob){
			((Mob)e).takeDamage(DamageCalculator.calculateDamage((Mob)e, DamageCalculator.PHYSICAL, damage));
			Buff b = new Buff(60*10);
			b.flatArmor = -1;
			((Mob)e).addBuff(b);
			System.out.println(e.getName() + " has " + ((Mob) e).buffList.size() + " stacks of debuff");
			Game.console = e.getName() + " has " + ((Mob) e).buffList.size() + " stacks of debuff\n";
		}
	}
	
	@Override
	public void flagForDelete(){
		super.flagForDelete();
	}
}
