package com.mygdx.time.entities;

import java.util.ArrayList;
import java.util.Iterator;

import com.mygdx.time.combat.Buff;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

public class Mob extends CollidableEntity{

	public MobHealthBar mobHealthBar = null;
	
	public boolean isSleeping = false;
	
	public float health, maxHealth = 500;
	
	public int baseArmor, modifiedArmor;
	public int baseFireResist, modifiedFireResist;
	public int baseIceResist, modifiedIceResist;
	public int baseLightningResist, modifiedLightningResist;
	
	public int baseMovementSpeed = 10;
	public int baseAttack, modifiedAttack;
	
	ArrayList<Buff> buffList = new ArrayList<Buff>();
	

	public Mob(float x, float y, GameStage gameStage, String entityName, boolean isAirborne) {
		super(x, y, gameStage, entityName, false);
		worldDestination.set(x+sprite.getWidth()/2, y+sprite.getHeight()/2);
		health = maxHealth;
	}
	
	public void fireProjectile(float offsetX, float offsetY, float damage, float speed, float angleDeg, float duration, GameStage gameStage, String entityName, boolean isAlly){
		Projectile projectile = new Projectile(getX()+sprite.getWidth()/2+offsetX, getY()+sprite.getHeight()/2+offsetY, damage, speed, angleDeg, duration, gameStage, entityName, isAlly);
		getStage().addActor(projectile);
	}
	
	@Override
	public void act(float delta){
		if(mobHealthBar == null){
			mobHealthBar = new MobHealthBar(getX(), getY()-sprite.getHeight() +1, this);
		}
		if(!isSleeping){
			super.act(delta);
			if(health <= 0){
				flagForDelete();
			}
			handleBuffs();
		}
	}
	
	public void takeDamage(float damage){
		health -= damage;
	}
	
	public void heal(float healing){
		health = Math.min(healing+health,maxHealth);
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
	
	public void addBuff(Buff buff){
		buffList.add(buff);
		recalculateStats();
		System.out.println("Added buff");
		System.out.println(buffList.get(0).percentMovementSpeed);
	}
	
	public void removeBuff(Buff buff){
		buffList.remove(buff);
		recalculateStats();
	}
	
	public void recalculateStats(){
		int flatArmor = baseArmor;
		int percentArmor = 0;
		int flatFireResist = baseFireResist;
		int percentFireResist = 0;
		int flatIceResist = baseIceResist;
		int percentIceResist = 0;
		int flatLightningResist = baseLightningResist;
		int percentLightningResist = 0;
		int flatMovementSpeed = baseMovementSpeed;
		int percentMovementSpeed = 0;
		int flatAttack = baseAttack;
		int percentAttack = 0;
		
		for(Buff buff : buffList){
			flatArmor += buff.flatArmor;
			percentArmor += buff.percentArmor;
			flatFireResist += buff.flatFireResist;
			percentFireResist += buff.percentFireResist;
			flatIceResist += buff.flatIceResist;
			percentIceResist += buff.percentIceResist;
			flatLightningResist += buff.flatLightningResist;
			percentLightningResist += buff.percentLightningResist;
			flatMovementSpeed += buff.flatMovementSpeed;
			percentMovementSpeed += buff.percentMovementSpeed;
			flatAttack += buff.flatAttack;
			percentAttack += buff.percentAttack;
		}
		modifiedArmor = (int)(flatArmor * (1+percentArmor/100.0));
		modifiedAttack =  (int)(flatAttack * (1+percentAttack/100.0));
		modifiedFireResist = (int)(flatFireResist * (1+percentFireResist/100.0));
		modifiedIceResist = (int)(flatIceResist * (1+percentIceResist/100.0));;
		modifiedLightningResist = (int)(flatLightningResist * (1+percentLightningResist/100.0));;
		modifiedMovementSpeed = (int)(flatMovementSpeed * (1+percentMovementSpeed/100.0));
	}
	
	private void handleBuffs(){
		for (Iterator<Buff> iterator = buffList.iterator(); iterator.hasNext();) {
			Buff b = iterator.next();
		    if (!b.isPermanent && b.endTick <= Game.gameTick) {
		        iterator.remove();
		    }
		    heal((float)-b.damagePerTick);
		}
		recalculateStats();
	}
}
