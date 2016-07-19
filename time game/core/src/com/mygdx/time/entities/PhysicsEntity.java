package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.time.screens.GameStage;

public abstract class PhysicsEntity extends Entity{

	protected Vector2 direction = new Vector2();
	protected float moveAngle;
	
	protected String blockedKey = "blocked";
	protected Cell cell;
	
	protected Vector2 worldDestination = new Vector2();
	protected float maxSpeed = 8f;
	
	protected Body body;
	protected String entityName;
	
	public PhysicsEntity(float x, float y, Texture texture, String entityName) {
		super(x, y, texture);
		this.entityName = entityName;
	}
	
	public PhysicsEntity(float x, float y, Texture texture, GameStage gameStage, String entityName) {
		this(x, y, texture, entityName);
		this.gameStage = gameStage;
		createBody(x, y, gameStage.getWorld());
	}
	
	public void createBody(float x, float y, World world){
		this.world = world;
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.DynamicBody;
		bd.linearDamping = 0.9f;
		
		FixtureDef fd = new FixtureDef();
		fd.density = 1f;
		fd.friction = 0f;
		fd.restitution = 0f;
		
		body = world.createBody(bd);
		
		gameStage.getLoader().attachFixture(body, entityName, fd, 2f);
		body.setUserData(this);
	    body.setTransform(x, y, 0);
	    body.setFixedRotation(true);
		this.setOrigin(0f,0f);
	    sprite.setOrigin(0f,0f);
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
	    if(Math.abs(body.getPosition().x + body.getLocalCenter().x - worldDestination.x) < 0.2 && Math.abs(body.getPosition().y + body.getLocalCenter().y - worldDestination.y) < 0.2){
	    	body.setLinearVelocity(0, 0);
	    	body.setAngularVelocity(0);
	    }
	}

	
}