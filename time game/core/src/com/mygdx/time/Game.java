package com.mygdx.time;

public class Game {
	
	public static final int WARP_LAYER = 3, GROUND_LAYER = 4, AERIAL_LAYER = 5, MOB_LAYER = 6;
	public static final int MOB = 0, ATTACK = 1, PROJECTILE = 2;
	
	public static final float CAMERA_OFFSET_X = 0;	
	public static final float ENGINE_FPS = 60;
	public static final float PPM = 16;

	public static final short
		CATEGORY_TERRAIN = -1,
		CATEGORY_NONCOLLIDABLE = 0,
		CATEGORY_TERRAIN_GROUND = 1,
		CATEGORY_TERRAIN_AERIAL = 2,

		CATEGORY_ALLY = 4,
		CATEGORY_ENEMY = 8,
		CATEGORY_ALLY_ATTACK = 16,
		CATEGORY_ENEMY_ATTACK = 32,
		
		CATEGORY_WARP = 64,
		
		CATEGORY_AIRBORNE = 128,
		CATEGORY_GROUNDED = 256;
	
	public static final short
		MASK_NONCOLLIDABLE = 0,
		MASK_TERRAIN = -1,
		
		MASK_ALLY = CATEGORY_ENEMY_ATTACK,
		MASK_ENEMY = CATEGORY_ALLY_ATTACK,
		MASK_ALLY_ATTACK = CATEGORY_ENEMY,
		MASK_ENEMY_ATTACK = CATEGORY_ALLY,
		
		MASK_AIRBORNE = CATEGORY_AIRBORNE | CATEGORY_TERRAIN_AERIAL,
		MASK_GROUNDED = CATEGORY_GROUNDED | CATEGORY_TERRAIN_GROUND;
	
	public static int gameTick = 0;
	public static String console = "";
	
}
