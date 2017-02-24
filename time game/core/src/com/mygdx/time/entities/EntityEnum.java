package com.mygdx.time.entities;

import com.mygdx.time.map.Game;

//replace with json in future

public enum EntityEnum {

	PLAYER("Player", "img/kittenTransparent3.png", Game.CATEGORY_ALLY, Game.MASK_ALLY | Game.CATEGORY_WARP, false),
	BLUE_SLIME("Bubly", "img/bubly.png", Game.CATEGORY_ENEMY, Game.MASK_ENEMY, false),
	FRIENDLY_SLIME("Bubly", "img/bubly.png", Game.CATEGORY_ALLY, Game.MASK_ALLY, false),
	GHOST_KITTEN("GhostKitten", "img/kittenTransparentBlue.png", Game.CATEGORY_ALLY, Game.MASK_ALLY, false),
	PLAYER_LASER("AllyLaser", "img/laser2.png", Game.CATEGORY_ALLY_ATTACK, Game.MASK_ALLY_ATTACK, true),
	ENEMY_LASER("EnemyLaser", "img/laser.png", Game.CATEGORY_ENEMY_ATTACK, Game.MASK_ENEMY_ATTACK, true),
	BLIZZARD("Blizzard", "img/blizzard.png", Game.CATEGORY_ENEMY_ATTACK, Game.MASK_ENEMY_ATTACK, false),
	SLASH("Slash", "img/slash.png", Game.CATEGORY_ALLY_ATTACK, Game.MASK_ALLY_ATTACK, false),
	TEST_SWORD("TestSword", "img/blade.png", Game.CATEGORY_ALLY_ATTACK, Game.MASK_ALLY_ATTACK, false),
	MARK("Mark", "img/mark.png", Game.CATEGORY_NONCOLLIDABLE, Game.MASK_NONCOLLIDABLE, false),
	FROST_EXPLOSION("FrostExplosion", "img/frostExplosion.png", Game.CATEGORY_ALLY_ATTACK, Game.MASK_ALLY_ATTACK, false);
	
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
