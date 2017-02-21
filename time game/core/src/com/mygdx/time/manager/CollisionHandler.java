package com.mygdx.time.manager;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.time.entities.CollidableEntity;
import com.mygdx.time.map.Game;

public class CollisionHandler {
	int collisionChecks = 0;
	// Singleton: unique instance
	private static CollisionHandler instance;
	
	// Singleton: private constructor
	private CollisionHandler() {
		super();
	}
	
	// Singleton: retrieve instance
	public static CollisionHandler getInstance() {
		if (instance == null) {
			instance = new CollisionHandler();
		}
		return instance;
	}
	
	public ArrayList<CollidableEntity> groundedTerrainList = new ArrayList<CollidableEntity>();
	public ArrayList<CollidableEntity> aerialTerrainList = new ArrayList<CollidableEntity>();
	public ArrayList<CollidableEntity> allyMobList = new ArrayList<CollidableEntity>();
	public ArrayList<CollidableEntity> allyAttackList = new ArrayList<CollidableEntity>();
	public ArrayList<CollidableEntity> enemyMobList = new ArrayList<CollidableEntity>();
	public ArrayList<CollidableEntity> enemyAttackList = new ArrayList<CollidableEntity>();
	
	
	public void act(){
		if(Game.gameTick%60==0){
			//System.out.println("allyMob: " + allyMobList.size());
			//System.out.println("allyAttack: " + allyAttackList.size());
			//System.out.println("enemyMob: " + enemyMobList.size());
			//System.out.println("enemyAttack: " + enemyAttackList.size());
		}
		collideAll();
		deleteFlaggedEntities();
	}
	
	public boolean collides(CollidableEntity e1, CollidableEntity e2){
		e1.updatePolygonHitbox();
		e2.updatePolygonHitbox();
		ArrayList<Polygon> hitbox1 = e1.hitbox;
		ArrayList<Polygon> hitbox2 = e2.hitbox;
		for(Polygon p1 : hitbox1){
			for (Polygon p2 : hitbox2){
				if(Intersector.overlapConvexPolygons(p1, p2)){
					return true;
				}
			}
		}
		return false;
	}
	
	public void collideAll(){
		collisionChecks = 0;
		for(CollidableEntity e : allyMobList){
			if(containsMask(e, Game.CATEGORY_ENEMY_ATTACK)){
				collideList(e, enemyAttackList);
			}
			if(containsMask(e, Game.CATEGORY_ENEMY)){
				collideList(e, enemyMobList);
			}
			if(containsMask(e, Game.CATEGORY_ALLY_ATTACK)){
				collideList(e, allyAttackList);
			}
			if(containsMask(e, Game.CATEGORY_ALLY)){
				collideList(e, allyMobList);
			}
		}
		for(CollidableEntity e : allyAttackList){
			if(containsMask(e, Game.CATEGORY_ENEMY_ATTACK)){
				collideList(e, enemyAttackList);
			}
			if(containsMask(e, Game.CATEGORY_ENEMY)){
				collideList(e, enemyMobList);
			}
			if(containsMask(e, Game.CATEGORY_ALLY_ATTACK)){
				collideList(e, allyAttackList);
			}
		}
		for(CollidableEntity e : enemyMobList){
			if(containsMask(e, Game.CATEGORY_ENEMY_ATTACK)){
				collideList(e, enemyAttackList);
			}
			if(containsMask(e, Game.CATEGORY_ENEMY)){
				collideList(e, enemyMobList);
			}
		}
		for(CollidableEntity e : enemyAttackList){
			if(containsMask(e, Game.CATEGORY_ENEMY_ATTACK)){
				collideList(e, enemyAttackList);
			}
		}
		if(Game.gameTick%60 == 0){
			//System.out.println("# bullets: " + enemyAttackList.size() + "\n# collision checks: " + collisionChecks);
			//System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond() + "\n");
		}
	}
	
	public boolean containsMask(CollidableEntity e, int mask){
		return (e.mask&mask) == mask;
	}
	
	public void collideList(CollidableEntity e, ArrayList<CollidableEntity> list){
		for(CollidableEntity e2 : list){
			collisionChecks++;
			if(e.boundingBox.overlaps(e2.boundingBox)){
				if(collides(e, e2) && e!=e2){
					
					//maybe move to separate method
					
					//some duplicate collisions occur, change later
					//System.out.println(e.getName() + " collided with " + e2.getName());
					if(e.isSolid && e2.isSolid && Math.abs(e.previousPosition.x-e.previousPosition.y) >1){
						float collisionAngle = getCollisionAngle(e, e2);
						//tp back to previous position
						//e.setPosition(e.previousPosition.x, e.previousPosition.y);
						//e2.setPosition(e2.previousPosition.x, e2.previousPosition.y);
						//
						while(collides(e,e2)){
							
							e.moveAngle  = e.direction.angle();
							e.moveBy(-e.modifiedMovementSpeed*MathUtils.cosDeg(e.moveAngle)/Game.ENGINE_FPS/5, -e.modifiedMovementSpeed*MathUtils.sinDeg(e.moveAngle)/Game.ENGINE_FPS/5);
							e2.moveAngle  = e2.direction.angle();
							e2.moveBy(-e2.modifiedMovementSpeed*MathUtils.cosDeg(e2.moveAngle)/Game.ENGINE_FPS/5, -e2.modifiedMovementSpeed*MathUtils.sinDeg(e2.moveAngle)/Game.ENGINE_FPS/5);
							
						}
						if(collisionAngle != -1){
							if(collisionAngle>=180){
								e.moveBy(-e.modifiedMovementSpeed*MathUtils.cosDeg(e.moveAngle)*MathUtils.cosDeg(collisionAngle)/Game.ENGINE_FPS,-e.modifiedMovementSpeed*MathUtils.sinDeg(e.moveAngle)*MathUtils.sinDeg(collisionAngle)/Game.ENGINE_FPS);
								e2.moveBy(-e2.modifiedMovementSpeed*MathUtils.cosDeg(e2.moveAngle)*MathUtils.cosDeg(collisionAngle)/Game.ENGINE_FPS,e2.modifiedMovementSpeed*MathUtils.sinDeg(e2.moveAngle)*MathUtils.sinDeg(collisionAngle)/Game.ENGINE_FPS);
							}else{
								e.moveBy(e.modifiedMovementSpeed*MathUtils.cosDeg(e.moveAngle)*MathUtils.cosDeg(collisionAngle)/Game.ENGINE_FPS,e.modifiedMovementSpeed*MathUtils.sinDeg(e.moveAngle)*MathUtils.sinDeg(collisionAngle)/Game.ENGINE_FPS);
								e2.moveBy(e2.modifiedMovementSpeed*MathUtils.cosDeg(e2.moveAngle)*MathUtils.cosDeg(collisionAngle)/Game.ENGINE_FPS,e2.modifiedMovementSpeed*MathUtils.sinDeg(e2.moveAngle)*MathUtils.sinDeg(collisionAngle)/Game.ENGINE_FPS);
							}
						}
						e.setPreviousPosition();
						e2.setPreviousPosition();
					}
					e.collideWith(e2);
					e2.collideWith(e);
				}
			}
		}
	}
	
	public float getCollisionAngle(CollidableEntity e, CollidableEntity e2){
		Float vertexX = null, vertexY = null;
		Polygon collidePolygon = null;
		Float previousPositionX = null, previousPositionY = null;
		//determine the vertex and the polygon which are intersecting
		//check whether a corner of e is inside e2
		for(Polygon p : e.hitbox){
			for(int i=0; i<p.getTransformedVertices().length; i+=2){
				for(int j=0; j<e2.hitbox.size(); j++){
					for(Polygon p2 : e2.hitbox){
						if(p2.contains(p.getTransformedVertices()[i], p.getTransformedVertices()[i+1])){
							vertexX = p.getTransformedVertices()[i];
							vertexY = p.getTransformedVertices()[i+1];
							collidePolygon = p2;
							previousPositionX = e.previousPosition.x+p.getVertices()[i];
							previousPositionY = e.previousPosition.y+p.getVertices()[i+1];
						}
					}
				}
			}
		}
		//check whether a corner of e2 is inside e
		if(vertexX == null && vertexY == null){
			for(Polygon p : e2.hitbox){
				for(int i=0; i<p.getTransformedVertices().length; i+= 2){
					for(int j=0; j<e.hitbox.size(); j++){
						for(Polygon p2 : e.hitbox){
							if(p2.contains(p.getTransformedVertices()[i], p.getTransformedVertices()[i+1])){
								vertexX = p.getTransformedVertices()[i];
								vertexY = p.getTransformedVertices()[i+1];
								collidePolygon = p2;
								previousPositionX = e2.previousPosition.x+p.getVertices()[i];
								previousPositionY = e2.previousPosition.y+p.getVertices()[i+1];
							}
						}
					}
				}
			}
		}
		//determine which edge of the polygon is intersecting
		float[] vertices = collidePolygon.getTransformedVertices();
		int length = collidePolygon.getTransformedVertices().length;
		for(int i=0; i<length; i+= 2){
			if(Intersector.intersectSegments(vertexX, vertexY, previousPositionX, previousPositionY, vertices[i%length], vertices[(i+1)%length], vertices[(i+2)%length], vertices[(i+3)%length], null)){
				return angle(vertices[i%length], vertices[(i+1)%length], vertices[(i+2)%length], vertices[(i+3)%length]);
			}
			System.out.println(vertexX + " " + vertexY + " " + previousPositionX + " " + previousPositionY + " " + vertices[i%length] + " " + vertices[(i+1)%length] + " " + vertices[(i+2)%length] + " " + vertices[(i+3)%length]);
		}
		System.out.println("-1");
		return -1;
	}
	
	private float angle(float x1, float y1, float x2, float y2){
		float dy = y2 - y1;
		float dx = x2 - x1;
		float theta = (float) Math.atan2(dy, dx);
		theta *= 180 / Math.PI;
		while(theta < 0){
			theta += 360;
		}
		return theta;
	}
	
	
	public void deleteFlaggedEntities(){
		for (Iterator<CollidableEntity> iterator = enemyAttackList.iterator(); iterator.hasNext();) {
			if(iterator.next().isFlaggedForDelete()){
				iterator.remove();
			}
		}
		for (Iterator<CollidableEntity> iterator = enemyMobList.iterator(); iterator.hasNext();) {
			if(iterator.next().isFlaggedForDelete()){
				iterator.remove();
			}
		}
		for (Iterator<CollidableEntity> iterator = allyAttackList.iterator(); iterator.hasNext();) {
			if(iterator.next().isFlaggedForDelete()){
				iterator.remove();
			}
		}
		for (Iterator<CollidableEntity> iterator = allyMobList.iterator(); iterator.hasNext();) {
			if(iterator.next().isFlaggedForDelete()){
				iterator.remove();
			}
		}
	}
	
	
	public void clearLists(){
		groundedTerrainList.clear();
		aerialTerrainList.clear();
		allyMobList.clear();
		allyAttackList.clear();
		enemyMobList.clear();
		enemyAttackList.clear();
	}
	
	public void addEntity(CollidableEntity e){
    	if((e.category & Game.CATEGORY_ALLY) == Game.CATEGORY_ALLY){
    		CollisionHandler.getInstance().allyMobList.add(e);
    	}
    	if((e.category & Game.CATEGORY_ENEMY) == Game.CATEGORY_ENEMY){
    		CollisionHandler.getInstance().enemyMobList.add(e);
    	}
    	if((e.category & Game.CATEGORY_ALLY_ATTACK) == Game.CATEGORY_ALLY_ATTACK){
    		CollisionHandler.getInstance().allyAttackList.add(e);
    	}
    	if((e.category & Game.CATEGORY_ENEMY_ATTACK) == Game.CATEGORY_ENEMY_ATTACK){
    		CollisionHandler.getInstance().enemyAttackList.add(e);
    	}

	}
}
