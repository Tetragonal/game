package com.mygdx.time.manager;

import java.util.ArrayList;

import com.mygdx.time.entities.CollidableEntity;
import com.mygdx.time.map.Game;

public class CollisionHandler {
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
		/*
		for(CollidableEntity e : allyMobList){
			if((e.mask&Game.CATEGORY_ENEMY_ATTACK) == Game.CATEGORY_ENEMY_ATTACK){
				for(CollidableEntity e2 : enemyAttackList){
					if(e.boundingBox.overlaps(e2.boundingBox)){
						System.out.println(e.boundingBox + " " + e2.boundingBox);
					}
				}
			}
		}
		*/
	}
	
	public void clearLists(){
		groundedTerrainList.clear();
		aerialTerrainList.clear();
		allyMobList.clear();
		allyAttackList.clear();
		enemyMobList.clear();
		enemyAttackList.clear();
	}
	
}
