package com.mygdx.time.entities;

import static com.mygdx.time.screens.LevelScreen.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.time.screens.LevelScreen;

public abstract class PhysicsEntity extends Entity{

	protected Vector2 location = new Vector2();
	protected Vector2 direction = new Vector2();
	protected Vector2 worldDestination = new Vector2();
	protected float moveAngle;
	
	protected String blockedKey = "blocked";
	protected Cell cell;
	protected MapLayer collisionLayer;
	
	protected float maxSpeed = 8f;
	
	protected Body body;
	String name;
	
	public PhysicsEntity(MapLayer collisionLayer, float x, float y, Texture texture, String name) {
		super(x, y, texture);
		this.collisionLayer = collisionLayer;
		worldDestination.set(x+sprite.getWidth()/2, y+sprite.getHeight()/2);
		location.set(x,y);
		this.name = name;
	}
	
	public PhysicsEntity(MapLayer collisionLayer, float x, float y, Texture texture, World world, String name) {
		this(collisionLayer, x, y, texture, name);
		createBody(x, y, world);
	}
	
	public void createBody(float x, float y, World world){
		this.world = world;
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.DynamicBody;
		body = LevelScreen.physicsBodies.createBody(name, world, bd, 1f/PPM, 1f/PPM);
		body.setUserData(this);
	    body.setTransform(x, y, 0);
	    body.setFixedRotation(true);
	    
		this.setOrigin(0f, 0f);
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
	    direction.set(worldDestination.x-sprite.getX()-sprite.getWidth()/2, worldDestination.y-sprite.getY()-sprite.getHeight()/2);
	    moveAngle  = direction.angle();

	}
}