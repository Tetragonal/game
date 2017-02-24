package com.mygdx.time.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.time.TimeGame;
import com.mygdx.time.combat.Buff;
import com.mygdx.time.map.Game;

public class FrostExplosion extends Attack{

	int startTick;
	int delay = 120;
	int duration = 120;
	int stage = 0;
	String explosionEntityName;
	public FrostExplosion(float x, float y, String markEntityName, String explosionEntityName, Entity parentEntity) {
		super(x, y, markEntityName, parentEntity);
		startTick = Game.gameTick;
		sprite.setCenter(x,y);
		this.setPosition(sprite.getX(), sprite.getY());
		this.explosionEntityName = explosionEntityName;
		damage = 200;
	}

	@Override
	public void act(float delta){
		if(stage == 0 && Game.gameTick > startTick+delay){
			this.entityName = explosionEntityName;
			this.setX(this.getX()+sprite.getWidth()/2);
			Texture texture = TimeGame.assets.get(EntityEnum.valueOf(explosionEntityName).getTextureFile());
			this.sprite = new Sprite(texture);
			sprite.setSize(sprite.getWidth()/Game.PPM, sprite.getHeight()/Game.PPM);
			sprite.setCenterX(this.getX());
			sprite.setY(this.getY());
			setSize(sprite.getWidth(), sprite.getHeight());
			stage = 1;
			createBody(this.getX()-sprite.getWidth()/2, this.getY(), true);
			((Sound) TimeGame.assets.get("sound/frost.wav")).play();
		}
		if(Game.gameTick > startTick+delay+duration){
			isFlaggedForDelete = true;
		}
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		if(e instanceof Mob && !collidedEntities.contains(e)){
			Buff b = new Buff(300);
			b.percentMovementSpeed = -90;
			((Mob) e).addBuff(b);
		}
		super.collideWith(e);
	}
}
