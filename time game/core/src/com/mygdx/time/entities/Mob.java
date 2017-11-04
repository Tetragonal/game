package com.mygdx.time.entities;

import java.util.ArrayList;
import java.util.Iterator;

import com.mygdx.time.Game;
import com.mygdx.time.brains.Brain;
import com.mygdx.time.brains.BrainEnum;
import com.mygdx.time.combat.Attack;
import com.mygdx.time.combat.Buff;
import com.mygdx.time.manager.EntityLoader;
import com.mygdx.time.screens.GameStage;

import attacks.AttackEnum;

public class Mob extends CollidableEntity{

	public MobHealthBar mobHealthBar = null;
	
	public boolean isSleeping;
	
	public float health, maxHealth;
	
	public int baseArmor, modifiedArmor;
	public int baseFireResist, modifiedFireResist;
	public int baseIceResist, modifiedIceResist;
	public int baseLightningResist, modifiedLightningResist;
	
	public float baseMovementSpeed = 0;
	public float baseAttack, modifiedAttack;
	
	ArrayList<Buff> buffList = new ArrayList<Buff>();
	
	Attack attack;
	Brain brain;

	public Mob(float x, float y, GameStage gameStage, String entityName){
		super(x, y, gameStage, entityName, Game.MOB, false);
		createBody(x, y, !isSolid, EntityLoader.getCategory(entityName), EntityLoader.getMask(entityName));
		String attackName = EntityLoader.getValue(entityName, "attack", Game.MOB);
		if(attackName != null){
			String[] attackArgs = EntityLoader.getValue(attackName, "attack parameter", Game.ATTACK).split(", ");
			this.attack = AttackEnum.valueOf(attackName).createAttack(attackArgs, this);
		}
		String[] brainArgs = EntityLoader.getValue(entityName, "brain parameter", Game.MOB).split(", ");
		this.brain = BrainEnum.valueOf(EntityLoader.getValue(entityName, "brain", Game.MOB)).createBrain(brainArgs);
		if(this.brain != null){
			this.brain.setParent(this);
		}
		//no destination
		worldDestination.set(x+sprite.getWidth()/2, y+sprite.getHeight()/2);
		
		baseMovementSpeed = Float.parseFloat(EntityLoader.getValue(entityName, "base speed", Game.MOB));
		baseAttack = Float.parseFloat(EntityLoader.getValue(entityName, "base attack", Game.MOB));
		maxHealth = Float.parseFloat(EntityLoader.getValue(entityName, "health", Game.MOB));
		health = maxHealth;
		recalculateStats();
	}
	
	public void fireProjectile(float offsetX, float offsetY, float damage, float speed, float angleDeg, GameStage gameStage, String entityName, boolean isAlly){
		Projectile projectile = new Projectile(getX()+sprite.getWidth()/2+offsetX, getY()+sprite.getHeight()/2+offsetY, damage, speed, angleDeg, 10, gameStage, entityName, isAlly);
		getStage().addActor(projectile);
	}
	
	@Override
	public void act(float delta){
		if(mobHealthBar == null){
			mobHealthBar = new MobHealthBar(this);
		}
		if(!isSleeping){
			super.act(delta);
			if(brain != null){
				brain.act();
			}
			if(health <= 0){
				isFlaggedForDelete = true;
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
	
	public boolean addBuff(Buff buff){
		if(buff.maxStacks != Buff.NO_STACK_LIMIT){
			int count = 0;
			for(int i=0; i<buffList.size(); i++){
				if(buff.id == buffList.get(i).id){
					count++;
				}
			}
			if(buff.maxStacks<=count){
				return false;
			}
		}
		buffList.add(buff);
		recalculateStats();
		return true;
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
		float flatMovementSpeed = baseMovementSpeed;
		int percentMovementSpeed = 0;
		float flatAttack = baseAttack;
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
		modifiedMovementSpeed = (float) (flatMovementSpeed * (1+percentMovementSpeed/100.0));
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
