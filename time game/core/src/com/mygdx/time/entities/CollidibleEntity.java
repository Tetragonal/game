package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CollidibleEntity extends Entity{

	protected Vector2 location = new Vector2();
	protected Vector2 direction = new Vector2();
	protected Vector2 worldDestination = new Vector2();
	protected float moveAngle;
	
	protected String blockedKey = "blocked";
	protected Cell cell;
	protected TiledMapTileLayer collisionLayer;
	
	protected float maxSpeed = 60;
	
	public CollidibleEntity(TiledMapTileLayer collisionLayer, float x, float y, Texture texture) {
		super(x, y, texture);
		this.collisionLayer = collisionLayer;
		worldDestination.set(x+sprite.getWidth()/2, y+sprite.getHeight()/2);
		location.set(x,y);

	}
	
	public void act(float delta){
		move(delta);
	}
	
	public void move(float delta){
	    direction.set(worldDestination.x-sprite.getX()-sprite.getWidth()/2, worldDestination.y-sprite.getY()-sprite.getHeight()/2);
	    moveAngle  = direction.angle();
	    
        moveX((float) (maxSpeed * MathUtils.cosDeg(moveAngle)), delta);
		moveY((float) (maxSpeed * MathUtils.sinDeg(moveAngle) * delta));
        if(collidesRight() || collidesLeft()){
        	setX((float)Math.floor(location.x));
        }else{
        	setX(location.x);
        }
        
        if(collidesTop() || collidesBottom()){
        	setY((float)Math.floor(location.y));
        }else{
        	setY(location.y);
        }
	}
	
	public void moveX(float velocity, float delta){
		float distance = velocity*delta;
		if(distance > 0 && Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) >= 1){
			float i=0;
			for(; i < distance; i++){
				if(collidesRight() || Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) < 1){
					break;
				}
				location.set(location.x+1, location.y);
			}
			if(!collidesRight() && Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) > 1){
				location.set(location.x+(distance-i), location.y);
			}
		}else if(distance < 0 && Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) >= 1){
			float i=distance;
			for(; i < 0; i++){
				if(!(collidesLeft() || Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) < 1)){
					location.set(location.x-1, location.y);
				}
			}
			if(!collidesLeft() && Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) >= 1){
				location.set(location.x+i, location.y);
			}
		}
	}
	
	public void moveY(float distance){
		if(distance > 0 && Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1){
			float i = 0;
			for(; i < distance; i++){
				if(collidesTop() || Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) < 1){
					break;
				}
				location.set(location.x, location.y+1);
			}
			if(!collidesTop() && Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1){
				location.set(location.x, location.y+(distance-i));
			}
		}else if(distance < 0 && Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1){
			float i=distance;
			for(; i < 0; i++){
				if(collidesBottom() || Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) < 1){
					break;
				}
				location.set(location.x, location.y-1);
			}
			if(!collidesBottom() && Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1){
				location.set(location.x, location.y+i);
			}
		}
	}
	
	public boolean isCellBlocked(float x, float y){
		cell = collisionLayer.getCell((int)(x/collisionLayer.getTileWidth()), (int)(y/collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
	}
	
	public boolean collidesRight(){
		for(float step = 0; step < getWidth(); step ++){
			if(isCellBlocked(location.x+getWidth(), location.y+step)){
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesLeft(){
		for(float step = 0; step < getWidth(); step ++){
			if(isCellBlocked(location.x-1, location.y+step)){
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesTop(){
		for(float step = 0; step < getHeight(); step ++){
			if(isCellBlocked(location.x+step, location.y+getHeight())){
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesBottom(){
		for(float step = 0; step < getHeight(); step ++){
			if(isCellBlocked(location.x+step, location.y-1)){
				return true;
			}
		}
		return false;
	}
	
}
