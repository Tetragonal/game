package com.mygdx.time.entities;

import java.util.HashMap;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.time.TimeGame;
import com.mygdx.time.combat.Buff;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

public class BlizzardAoE extends CollidableEntity{
	
	HashMap<Mob, Buff> debuffTracker = new HashMap<Mob,Buff>();
	
	public BlizzardAoE(float x, float y, GameStage gameStage, String entityName) {
		super(x, y, gameStage, entityName, true);
		// TODO Auto-generated constructor stub
		modifiedMovementSpeed = 0;
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		if(e instanceof Mob){
			if(!debuffTracker.containsKey(e)){
				Buff b = new Buff();
				b.percentMovementSpeed = -60;
				b.damagePerTick = 1;
				((Mob) e).addBuff(b);
				debuffTracker.put((Mob) e, b);
				Game.console = e.getName() + " is chilled";
				if(e instanceof Player){
					((Sound) TimeGame.assets.get("sound/frost.wav")).play();
				}
			}
		}
	}
	
	@Override
	public void endCollideWith(CollidableEntity e){
		if(e instanceof Mob){
			((Mob) e).removeBuff(debuffTracker.get(e));
			debuffTracker.remove(e);
			Game.console = e.getName() + " is no longer chilled";
		}
	}
}
