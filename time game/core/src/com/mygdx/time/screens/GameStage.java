package com.mygdx.time.screens;

import static com.mygdx.time.screens.LevelScreen.GROUND_LAYER;
import static com.mygdx.time.screens.LevelScreen.WARP_LAYER;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.time.TimeGame;
import com.mygdx.time.entities.AggroEnemyTest;
import com.mygdx.time.entities.EnemyEnum;
import com.mygdx.time.entities.GhostKitten;
import com.mygdx.time.entities.Player;
import com.mygdx.time.entities.WanderingEnemy;

public class GameStage extends Stage{
	private MapLayer collisionLayer;
	private LevelScreen levelScreen;
	private World world;
	
	public GameStage(Viewport viewport, TiledMap map, World world, LevelScreen levelScreen){
		super(viewport, TimeGame.batch);
		this.setDebugAll(true);
		collisionLayer = map.getLayers().get(GROUND_LAYER);
		this.levelScreen = levelScreen;
		this.world = world;
	}
	
	public Player addPlayer(){
		Player player = new Player(collisionLayer, levelScreen.startX/LevelScreen.PPM, levelScreen.startY/LevelScreen.PPM, TimeGame.assets.get("img/kittenTransparent3.png"), world, "kittenTransparent3");
		addActor(player);
		return player;
	}
	
	public GhostKitten addGhostKitten(Player attachedPlayer){
		GhostKitten ghostKitten = new GhostKitten(collisionLayer,levelScreen.startX/LevelScreen.PPM, levelScreen.startY/LevelScreen.PPM, TimeGame.assets.get(EnemyEnum.valueOf("GHOST_KITTEN").getTextureFile()), attachedPlayer, world, "kittenTransparentBlue");
		addActor(ghostKitten);
		return ghostKitten;
	}
	
	public WanderingEnemy addWanderingEntity(float x, float y, String mobName, String physicsName){
		WanderingEnemy wanderingEntity = new WanderingEnemy(collisionLayer, x, y, TimeGame.assets.get(EnemyEnum.valueOf(mobName).getTextureFile()), world, physicsName);
		addActor(wanderingEntity);
		return wanderingEntity;
	}

	public AggroEnemyTest addAggroEnemyTest(float x, float y, String mobName, String physicsName) {
		AggroEnemyTest aggroEnemyTest = new AggroEnemyTest(collisionLayer, x, y, TimeGame.assets.get(EnemyEnum.valueOf(mobName).getTextureFile()), world, physicsName);
		addActor(aggroEnemyTest);
		return aggroEnemyTest;
		
	}
	
}
