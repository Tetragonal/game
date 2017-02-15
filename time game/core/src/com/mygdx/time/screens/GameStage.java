package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.time.TimeGame;
import com.mygdx.time.entities.AggroEnemyTest;
import com.mygdx.time.entities.EntityEnum;
import com.mygdx.time.entities.GhostKitten;
import com.mygdx.time.entities.Player;
import com.mygdx.time.entities.WanderingEnemy;
import com.mygdx.time.map.BodyEditorLoader;
import com.mygdx.time.map.Game;

public class GameStage extends Stage{
	
	private BodyEditorLoader loader;
	private LevelScreen levelScreen;
	private World world;
	
	public GameStage(Viewport viewport, TiledMap map, World world, LevelScreen levelScreen){
		super(viewport, TimeGame.batch);
//		this.setDebugAll(true);
		this.levelScreen = levelScreen;
		this.world = world;
		
		loader = new BodyEditorLoader(Gdx.files.internal("physics/CatPhysics.json"));
	}
	
	public Player addPlayer(){
		Player player = new Player(levelScreen.startX/Game.PPM, levelScreen.startY/Game.PPM, this, "PLAYER");
		return player;
	}
	
	public GhostKitten addGhostKitten(Player attachedPlayer){
		GhostKitten ghostKitten = new GhostKitten(levelScreen.startX/Game.PPM, levelScreen.startY/Game.PPM, attachedPlayer);
		addActor(ghostKitten);
		return ghostKitten;
	}
	
	public WanderingEnemy addWanderingEntity(float x, float y, String entityName){
		WanderingEnemy wanderingEntity = new WanderingEnemy(x, y, this, entityName, isAirborne(entityName));
		addActor(wanderingEntity);
		return wanderingEntity;
	}

	public AggroEnemyTest addAggroEnemyTest(float x, float y, String entityName) {
		AggroEnemyTest aggroEnemyTest = new AggroEnemyTest(x, y, this, entityName, isAirborne(entityName));
		addActor(aggroEnemyTest);
		return aggroEnemyTest;
		
	}

	public BodyEditorLoader getLoader(){
		return loader;
	}
	
	public World getWorld(){
		return world;
	}
	
	public boolean isAirborne(String entityName){
		return EntityEnum.valueOf(entityName).isAirborne();
	}
	
}
