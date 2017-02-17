package com.mygdx.time.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

public abstract class CollidableEntity extends Entity{
	//old Box2D collision/movement, change later (movement will be in Mob)
	protected Vector2 direction = new Vector2();
	protected float moveAngle;
	protected Vector2 worldDestination = new Vector2();
	protected float modifiedMovementSpeed = 10f;
	protected Body body;
	protected String entityName;
	protected boolean isAirborne;
	
	//from srugs
	public int mask;
	public int category;
	public boolean isSolid; //whether it blocks movement on collision
	public ArrayList<Polygon> hitbox = new ArrayList<Polygon>();
	public Rectangle boundingBox;
	
	public CollidableEntity(float x, float y, GameStage gameStage, String entityName, boolean isSensor) {
		super(x, y, entityName);
		this.entityName = entityName;
		this.gameStage = gameStage;
		createBody(x, y, isSensor);
		isSolid = !isSensor;
		
		
		boundingBox = sprite.getBoundingRectangle();
		hitbox.add(new Polygon(new float[]{x,y, x+sprite.getWidth(),y, x+sprite.getWidth(),y+sprite.getHeight(), x,y+sprite.getHeight()}));
		for(Polygon p : hitbox){
			boundingBox.merge(p.getBoundingRectangle());
		}
	}
	
	public void translate(float x, float y){
		
	}
	
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
		
		gameStage.getLoader().attachFixture(body, EntityEnum.valueOf(entityName).getPhysicsName(), fd, 2f);
		body.setUserData(this);
	    body.setTransform(x, y, 0);
	    body.setFixedRotation(true);
		this.setOrigin(0f,0f);
	    sprite.setOrigin(0f,0f);
		
		Filter f = new Filter();
	    f.categoryBits = EntityEnum.valueOf(entityName).getCategory();
	    category = EntityEnum.valueOf(entityName).getCategory();
	    f.maskBits = EntityEnum.valueOf(entityName).getMask();
	    mask = EntityEnum.valueOf(entityName).getMask();
    	if(isAirborne){
    		f.maskBits = (short) (f.maskBits | Game.MASK_AIRBORNE);
    		mask = (short) (f.maskBits | Game.MASK_AIRBORNE);
    	}else{
    		f.maskBits = (short) (f.maskBits | Game.MASK_GROUNDED);
    		mask = (short) (f.maskBits | Game.MASK_GROUNDED);
    	}
		
		for(int i=0; i<body.getFixtureList().size; i++){
			body.getFixtureList().get(i).setFilterData(f);
		}
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
		
	public Body getBody(){
		return body;
	}

	public boolean isAirborne() {
		return isAirborne;
	}

	public void collideWith(CollidableEntity e){
		
	}
	
	public void endCollideWith(CollidableEntity e){
		
	}
	
	public void move(){
	    //direction.set(worldDestination.x-body.getPosition().x- body.getLocalCenter().x, worldDestination.y-body.getPosition().y - body.getLocalCenter().y);
		direction.set(worldDestination.x-this.getX()- this.getWidth()/2, worldDestination.y-this.getY() - this.getHeight()/2);
	    moveAngle  = direction.angle();
	    //body.setTransform(this.getX()+modifiedMovementSpeed*MathUtils.cosDeg(moveAngle)/Game.ENGINE_FPS, this.getY()+modifiedMovementSpeed*MathUtils.sinDeg(moveAngle)/Game.ENGINE_FPS,0);
	    for(Polygon p : hitbox){
	    	p.translate(modifiedMovementSpeed*MathUtils.cosDeg(moveAngle)/Game.ENGINE_FPS, modifiedMovementSpeed*MathUtils.sinDeg(moveAngle)/Game.ENGINE_FPS);
	    	p.setRotation((float) rotation);
	    }
	    boundingBox.setPosition(boundingBox.x+modifiedMovementSpeed*MathUtils.cosDeg(moveAngle)/Game.ENGINE_FPS, boundingBox.y+modifiedMovementSpeed*MathUtils.sinDeg(moveAngle)/Game.ENGINE_FPS);
	    this.moveBy(modifiedMovementSpeed*MathUtils.cosDeg(moveAngle)/Game.ENGINE_FPS, modifiedMovementSpeed*MathUtils.sinDeg(moveAngle)/Game.ENGINE_FPS);
	    sprite.setPosition(this.getX(), this.getY());
	    /*
	    body.setLinearVelocity(modifiedMovementSpeed*MathUtils.cosDeg(moveAngle), modifiedMovementSpeed*MathUtils.sinDeg(moveAngle));
	    
	    if(Math.abs(body.getPosition().x + body.getLocalCenter().x - worldDestination.x) < 0.1){
	    	body.setLinearVelocity(0, body.getLinearVelocity().y);
	    	body.setAngularVelocity(0);
	    }
	    if(Math.abs(body.getPosition().y + body.getLocalCenter().y - worldDestination.y) < 0.2){
	    	body.setLinearVelocity(body.getLinearVelocity().x, 0);
	    	body.setAngularVelocity(0);
	    }
	    */
	}
	
}