package com.mygdx.time.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.time.manager.CollisionHandler;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

public abstract class CollidableEntity extends Entity{
	//old Box2D collision/movement, change later (movement will be in Mob)
	public Vector2 direction = new Vector2();
	public Float moveAngle;
	protected Vector2 worldDestination = new Vector2();
	public float modifiedMovementSpeed = 10f;
	protected String entityName;
	protected boolean isAirborne;
	
	//from srugs
	public int mask;
	public int category;
	public boolean isSolid; //whether it blocks movement on collision
	public ArrayList<Polygon> hitbox = new ArrayList<Polygon>();
	public Rectangle boundingBox;
	
	public Vector2 previousPosition = new Vector2();
	
	public CollidableEntity(float x, float y, GameStage gameStage, String entityName, boolean isSensor) {
		super(x, y, entityName);
		this.entityName = entityName;
		this.gameStage = gameStage;
		isSolid = !isSensor;
		isAirborne = EntityEnum.valueOf(entityName).isAirborne();
		
		//not sure if necessary
		this.setOrigin(0f,0f);
	    sprite.setOrigin(0f,0f);
		
		boundingBox = sprite.getBoundingRectangle();
		hitbox.add(new Polygon(new float[]{0,0, sprite.getWidth(),0, sprite.getWidth(),sprite.getHeight(), 0,sprite.getHeight()}));
		for(Polygon p : hitbox){
			//boundingBox.merge(p.getBoundingRectangle());
		}
		setCategoryMask();
	}
	
	public void setCategoryMask(){
		category = EntityEnum.valueOf(entityName).getCategory();
	    mask = EntityEnum.valueOf(entityName).getMask();
    	if(isAirborne){
    		mask = (short) (mask | Game.MASK_AIRBORNE);
    	}else{
    		mask = (short) (mask | Game.MASK_GROUNDED);
    	}
    	CollisionHandler.getInstance().addEntity(this);
	}
	
	public void act(float delta){
		/*
		Vector2 position = body.getPosition();
		float degrees = (float) Math.toDegrees(body.getAngle());
		this.setPosition(position.x, position.y);
		this.setRotation(degrees);
	    sprite.setPosition(position.x, position.y);
	    sprite.setRotation(degrees);	
	    */
		move();
    }
		
	public boolean isAirborne() {
		return isAirborne;
	}

	public void collideWith(CollidableEntity e){

	}
	
	public void endCollideWith(CollidableEntity e){
		
	}
	
	public void move(){
		setPreviousPosition();
		direction.set(worldDestination.x-this.getX()- this.getWidth()/2, worldDestination.y-this.getY() - this.getHeight()/2);
		if(Math.abs(getX() + sprite.getWidth()/2 - worldDestination.x) < 0.1 && Math.abs(getY() + sprite.getHeight()/2 - worldDestination.y) < 0.1){
			moveAngle = null; //cancel movement if close enough to worldDestination
		}else{
			moveAngle  = direction.angle();
		}
	    if(moveAngle != null){
	    	this.moveBy(modifiedMovementSpeed*MathUtils.cosDeg(moveAngle)/Game.ENGINE_FPS, modifiedMovementSpeed*MathUtils.sinDeg(moveAngle)/Game.ENGINE_FPS);
	    	updateBoundingBox();
	    }
   	
	}
	
	public void setPreviousPosition(){
		previousPosition.set(this.getX(), this.getY());
	}
	
	public void updatePolygonHitbox(){
	    for(Polygon p : hitbox){
	    	p.setRotation((float) rotation);
	    	p.setPosition(getX(), getY());
	    	p.getTransformedVertices();
	    }
	}
	
	public void updateBoundingBox(){
		boundingBox.set(sprite.getBoundingRectangle());
	}
	
	
}