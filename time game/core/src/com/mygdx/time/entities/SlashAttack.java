package com.mygdx.time.entities;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.time.TimeGame;
import com.mygdx.time.map.Game;

public class SlashAttack extends Attack{
	float offsetX, offsetY, initialAngle;
	int startTick;
	float rotationRate; //deg/sec
	float totalAngle;
	public SlashAttack(float offsetX, float offsetY, float centerAngle, float rotationRate, float totalAngle, float damage, String entityName, Entity parentEntity) {
		super(parentEntity.getX()+offsetX, parentEntity.getY()+offsetY, entityName, parentEntity);
		this.damage = damage;
		initialAngle = centerAngle-totalAngle/2;
		this.rotationRate = rotationRate;
		this.totalAngle = totalAngle;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		modifiedMovementSpeed = 0;
		body.setBullet(true);
		worldDestination.set(body.getPosition().x, body.getPosition().y);
		body.setFixedRotation(false);
		startTick = Game.gameTick;

		body.setTransform(0, 0, (float) Math.toRadians(initialAngle));
		float cosX = offsetX*(float)Math.cos(body.getAngle());
		float sinX = offsetX*(float)Math.sin(body.getAngle());
		float sinY = offsetY*(float)Math.sin(body.getAngle());
		float cosY = offsetY*(float)Math.cos(body.getAngle());
		float centerX = parentEntity.getX()+parentEntity.getWidth()/2;
		float centerY = parentEntity.getY()+parentEntity.getHeight()/2;
		int deltaTick = Game.gameTick-startTick;
		//(float)Math.cos(body.getAngle())*sprite.getHeight()/2 is to make the body center (0,sprite.getHeight()/2)
		body.setTransform(centerX+cosX-sinY+(float)Math.sin(body.getAngle())*sprite.getHeight()/2, centerY+sinX+cosY-(float)Math.cos(body.getAngle())*sprite.getHeight()/2, (float) Math.toRadians(initialAngle+deltaTick*rotationRate/Game.ENGINE_FPS));
		float degrees = (float) Math.toDegrees(body.getAngle());
		this.setPosition(centerX+cosX-sinY, centerY+sinX+cosY);
		this.setRotation(degrees);
	    sprite.setPosition(centerX+cosX-sinY, centerY+sinX+cosY);
	    sprite.setRotation(degrees);
	}
	
	@Override
	public void act(float delta){
		float cosX = offsetX*(float)Math.cos(body.getAngle());
		float sinX = offsetX*(float)Math.sin(body.getAngle());
		float sinY = offsetY*(float)Math.sin(body.getAngle());
		float cosY = offsetY*(float)Math.cos(body.getAngle());
		float centerX = parentEntity.getX()+parentEntity.getWidth()/2;
		float centerY = parentEntity.getY()+parentEntity.getHeight()/2;
		int deltaTick = Game.gameTick-startTick;
		body.setTransform(centerX+cosX-sinY+(float)Math.sin(body.getAngle())*sprite.getHeight()/2, centerY+sinX+cosY-(float)Math.cos(body.getAngle())*sprite.getHeight()/2, (float) Math.toRadians(initialAngle+deltaTick*rotationRate/Game.ENGINE_FPS));
		float degrees = (float) Math.toDegrees(body.getAngle());
		this.setPosition(body.getPosition().x, body.getPosition().y);
		this.setRotation(degrees);
	    sprite.setPosition(body.getPosition().x, body.getPosition().y);
	    sprite.setRotation(degrees);
	    if(Math.toDegrees(body.getAngle())-initialAngle>totalAngle){
	    	this.isFlaggedForDelete = true;
	    }
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		super.collideWith(e);
		((Sound) TimeGame.assets.get("sound/warp2.ogg")).play();
	}
}
