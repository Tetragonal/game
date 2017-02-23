package com.mygdx.time.entities;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
				((Mob) e).addBuff(b);
				debuffTracker.put((Mob) e, b);
				Game.console = e.getName() + " is chilled";
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
	
	
	@Override
	public void createBody(float x, float y, boolean isSensor){
		isAirborne = EntityEnum.valueOf(entityName).isAirborne();
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.DynamicBody;
		
		FixtureDef fd = new FixtureDef();
		fd.density = 1f;
		fd.friction = 0f;
		fd.restitution = 0f;
		fd.isSensor = isSensor;

		body = gameStage.getWorld().createBody(bd);
		
		gameStage.getLoader().attachFixture(body, EntityEnum.valueOf(entityName).getPhysicsName(), fd, 4*2f);
		body.setUserData(this);
	    body.setTransform(x, y, 0);
	    body.setFixedRotation(true);
		this.setOrigin(0f,0f);
	    sprite.setOrigin(0f,0f);
		
		Filter f = new Filter();
	    f.categoryBits = EntityEnum.valueOf(entityName).getCategory();
	    f.maskBits = EntityEnum.valueOf(entityName).getMask();
    	if(isAirborne){
    		f.maskBits = (short) (f.maskBits | Game.MASK_AIRBORNE);
    	}else{
    		f.maskBits = (short) (f.maskBits | Game.MASK_GROUNDED);

    	}
		
		for(int i=0; i<body.getFixtureList().size; i++){
			body.getFixtureList().get(i).setFilterData(f);
		}
	}
}
