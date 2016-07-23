package com.mygdx.time.entities;

import com.mygdx.time.map.Game;

public enum EntityEnum {

	PLAYER("Player", "img/kittenTransparent3.png", Game.CATEGORY_ALLY, Game.MASK_ALLY | Game.CATEGORY_WARP, false),
	BLUE_SLIME("Bubly", "img/bubly.png", Game.CATEGORY_ENEMY, Game.MASK_ENEMY, false),
	GHOST_KITTEN("GhostKitten", "img/kittenTransparentBlue.png", Game.CATEGORY_ALLY, Game.MASK_ALLY, false),
	PLAYER_LASER("AllyLaser", "img/laser2.png", Game.CATEGORY_ALLY_ATTACK, Game.MASK_ALLY_ATTACK, true),
	ENEMY_LASER("EnemyLaser", "img/laser.png", Game.CATEGORY_ENEMY_ATTACK, Game.MASK_ENEMY_ATTACK, true);
	
	private String physicsName;
	private String textureFile;
	private short category;
	private short mask;
	private boolean isAirborne;
	
	EntityEnum(String physicsName, String textureFile, int category, int mask, boolean isAirborne){
		this.physicsName = physicsName;
		this.textureFile = textureFile;
		this.category = (short)category;
		this.mask = (short)mask;
		this.isAirborne = isAirborne;
	}
	
	public String getPhysicsName(){
		return physicsName;
	}
	
	public String getTextureFile(){
		return textureFile;
	}
	
	public short getCategory(){
		return category;
	}
	
	public short getMask(){
		return mask;
	}
	
	public boolean isAirborne(){
		return isAirborne;
	}
}
