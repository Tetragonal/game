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
	public boolean isSolid;
	
	public CollidableEntity(float x, float y, GameStage gameStage, String entityName, boolean isSensor) {
		super(x, y, entityName);
		this.entityName = entityName;
		this.gameStage = gameStage;
		createBody(x, y, isSensor);
		isSolid = !isSensor;
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
		
		gameStage.getLoader().attachFixture(body, EntityEnum.valueOf(entityName).getPhysicsName(), fd, sprite.getWidth());
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
	
	public void act(float delta){
		Vector2 position = body.getPosition();
		float degrees = (float) Math.toDegrees(body.getAngle());
		this.setPosition(position.x, position.y);
		this.setRotation(degrees);
	    sprite.setPosition(position.x, position.y);
	    sprite.setRotation(degrees);
		move();
	}
		
	public Body getBody(){
		return body;
	}
	
	public void move(){
	    direction.set(worldDestination.x-body.getPosition().x- body.getLocalCenter().x, worldDestination.y-body.getPosition().y - body.getLocalCenter().y);
	    moveAngle  = direction.angle();
	    
	    body.setLinearVelocity(modifiedMovementSpeed*MathUtils.cosDeg(moveAngle), modifiedMovementSpeed*MathUtils.sinDeg(moveAngle));
	    
	    if(Math.abs(body.getPosition().x + body.getLocalCenter().x - worldDestination.x) < 0.1){
	    	body.setLinearVelocity(0, body.getLinearVelocity().y);
	    	body.setAngularVelocity(0);
	    }
	    if(Math.abs(body.getPosition().y + body.getLocalCenter().y - worldDestination.y) < 0.2){
	    	body.setLinearVelocity(body.getLinearVelocity().x, 0);
	    	body.setAngularVelocity(0);
	    }
	}

	public boolean isAirborne() {
		return isAirborne;
	}

	public void collideWith(CollidableEntity e){
		
	}
	
	public void endCollideWith(CollidableEntity e){
		
	}
	
}