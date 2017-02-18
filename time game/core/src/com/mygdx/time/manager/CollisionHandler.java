package com.mygdx.time.manager;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
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
			System.out.println("allyMob: " + allyMobList.size());
			System.out.println("allyAttack: " + allyAttackList.size());
			System.out.println("enemyMob: " + enemyMobList.size());
			System.out.println("enemyAttack: " + enemyAttackList.size());
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
			System.out.println("# bullets: " + enemyAttackList.size() + "\n# collision checks: " + collisionChecks);
			System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond() + "\n");
		}
	}
	
	public boolean containsMask(CollidableEntity e, int mask){
		return (e.mask&mask) == mask;
	}
	
	public void collideList(CollidableEntity e, ArrayList<CollidableEntity> list){
		for(CollidableEntity e2 : list){
			collisionChecks++;
			if(e.boundingBox.overlaps(e2.boundingBox)){
				if(collides(e, e2)){
					//System.out.println(e.getName() + " collided with " + e2.getName());
					e.collideWith(e2);
					e2.collideWith(e);
				}
			}
		}
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
