package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.time.TimeGame;
import com.mygdx.time.entities.BlizzardAoE;
import com.mygdx.time.entities.CollidableEntity;
import com.mygdx.time.entities.Entity;
import com.mygdx.time.entities.EntityEnum;
import com.mygdx.time.entities.Player;
import com.mygdx.time.inventory.InventoryWindow;
import com.mygdx.time.manager.LevelScreenManager;
import com.mygdx.time.manager.MusicManager;
import com.mygdx.time.map.Game;
import com.mygdx.time.map.MapLoader;
import com.mygdx.time.map.TestContactListener;


public class LevelScreen implements Screen{
	
	protected TiledMap map;
	protected GameStage gameStage;
	protected Stage uiStage;
	protected String mapFile, musicFile;
	protected float startX, startY;
	protected float musicVolume;
	
	//map information
	private final int[] backgroundLayers = {0,1};
	private final int[] foregroundLayers = {2};
	private String currentLevel, previousLevel;
	private String warpDestination;
	
	private PauseWindow pauseWindow;
	private InventoryWindow inventoryWindow;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private FitViewport viewport;
	
	private int cameraType = 1;
	
	private BitmapFont font = new BitmapFont(); //temporary
	private Matrix4 uiMatrix = new Matrix4();
	private Player player;
	
	private float gameTimer = 0;
	
	private World world;
	Box2DDebugRenderer debugRenderer;
	
	private Group groundedGroup = new Group(), aerialGroup = new Group(), miscGroup = new Group();
	
	public LevelScreen(String previousLevel, String currentLevel, String mapFile){
		this.previousLevel = previousLevel;
		this.currentLevel = currentLevel;
		this.mapFile = mapFile;
	}
	
	@Override
	public void show() {
		loadAssets();
		
		Box2D.init();
		
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new TestContactListener(this));

		Skin menuSkin = TimeGame.assets.get("ui/menuSkin.json");
//		Skin uiSkin = TimeGame.assets.get("temp inventory resources/skins/uiskin.json");
		
		map = TimeGame.assets.get(mapFile);
		renderer = new OrthogonalTiledMapRenderer(map, 1f/Game.PPM);
		camera = new OrthographicCamera();
		viewport = new FitViewport(Gdx.graphics.getWidth()/Game.PPM, Gdx.graphics.getHeight()/Game.PPM, camera);
		gameStage = new GameStage(viewport, map, world, this);
		uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), TimeGame.batch);
		Gdx.input.setInputProcessor(new InputMultiplexer(gameStage, uiStage));
		
		gameStage.addActor(groundedGroup);
		gameStage.addActor(aerialGroup);
		gameStage.addActor(miscGroup);
		addEntities();
		
		//set music
		if(musicFile != null){
			MusicManager.getInstance().setTransitionMusic(Gdx.files.internal(musicFile), 6, musicVolume);
		}  
		
		//independent pause menu
		pauseWindow = new PauseWindow(menuSkin);
		uiStage.addActor(pauseWindow);
		gameStage.addCaptureListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode){
				if(keycode == Keys.ESCAPE){
					pauseWindow.setVisible(!pauseWindow.isVisible());
				}
				else if(keycode == Keys.V && pauseWindow.isVisible() == false){
					//inventoryWindow.setVisible(!inventoryWindow.isVisible());
				}
				return true;
			}
		});
//		DragAndDrop dragAndDrop = new DragAndDrop();
//		dragAndDrop.setDragActorPosition(200,50);
//		inventoryWindow = new InventoryWindow(new Inventory(), dragAndDrop, uiSkin, uiStage);
//		uiStage.addActor(inventoryWindow);	
		
		MapLoader mapLoader = new MapLoader(map, world);
		mapLoader.loadLayer(Game.GROUND_LAYER, Game.CATEGORY_TERRAIN_GROUND);
		mapLoader.loadLayer(Game.AERIAL_LAYER, Game.CATEGORY_TERRAIN_AERIAL);
		mapLoader.loadLayer(Game.WARP_LAYER, Game.CATEGORY_WARP);
	}
	
	@Override
	public void render(float delta) { //called every frame
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	    float frameTime = delta;// = Math.min(delta, 0.25f);
		gameTimer += frameTime;
		if(gameTimer >= 1/Game.ENGINE_FPS){
			act();
			gameTimer -= 1/Game.ENGINE_FPS;
		}
		if(gameTimer >= 1/Game.ENGINE_FPS && delta<1/Game.ENGINE_FPS/2){
			act();
			gameTimer -= 1/Game.ENGINE_FPS;
		}
		if(Game.gameTick%60==0){
			System.out.println(gameStage.getActors().size);
		}
		
		updateCamera();
		drawScreen();
	}

	public void updateCamera(){
		switch(cameraType){
			case 0:
				//center camera on player
				camera.position.set(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2, 0);
				break;
			case 1:
				//Camera follows you
				camera.position.set((float) (camera.position.x + .05f*(Game.CAMERA_OFFSET_X/2f*camera.zoom-camera.position.x + player.getX() + player.getWidth()/2f)),
						(float) (camera.position.y + .05*(-camera.position.y + player.getY() + player.getHeight()/2f)),
						0);
				break;
			case 2:
				//Camera follows the midpoint between your cursor and your character
				camera.position.set((float) (camera.position.x + .05f*(Game.CAMERA_OFFSET_X/2f*camera.zoom-camera.position.x + player.getX() + player.getWidth()/2f)+0.01/Game.PPM*(Gdx.input.getX()-Gdx.graphics.getWidth()/2)),
						(float) (camera.position.y + .05*(-camera.position.y + player.getY() + player.getHeight()/2f)-0.01/Game.PPM*(Gdx.input.getY()-Gdx.graphics.getHeight()/2)),
						0);
				break;
			case 3:
				//TODO Camera follows the midpoint between your (pressed) cursor and your character
				break;
			default:
				break;
		}
		camera.update();
	}
	
	public void act(){
//		player.acceptInput = !inventoryWindow.isVisible();
		if(!pauseWindow.isVisible()){
			handleInput();
			world.step(1/Game.ENGINE_FPS, 6, 2);
			gameStage.act();
			uiStage.act();
			groupActors();
			Game.gameTick++;
		}else {
			player.walkSound.pause();
//			inventoryWindow.setVisible(false);
		}
		if(warpDestination != null){
			LevelScreenManager.getInstance().setScreen(warpDestination, currentLevel);
		}
	}
	
	public void groupActors(){
		//sort actors
		for(Actor actor : gameStage.getActors()){
			if(actor instanceof CollidableEntity){
				if(!((CollidableEntity) actor).isSolid){
					miscGroup.addActor(actor);
				}
				else if(((CollidableEntity)actor).isAirborne()){
					aerialGroup.addActor(actor);
				}else{
					groundedGroup.addActor(actor);
				}
			}else if (actor instanceof Entity){
				miscGroup.addActor(actor);
			}
		}
		
		//delete actors
		for(Actor actor : groundedGroup.getChildren()){
			if(actor instanceof Entity && ((Entity) actor).isFlaggedForDelete){
				if(actor instanceof CollidableEntity){
					world.destroyBody(((CollidableEntity) actor).getBody());
				}
				actor.remove();
			}
		}
		for(Actor actor : aerialGroup.getChildren()){
			if(actor instanceof Entity && ((Entity) actor).isFlaggedForDelete){
				if(actor instanceof CollidableEntity){
					world.destroyBody(((CollidableEntity) actor).getBody());
				}
				actor.remove();
			}
		}
		for(Actor actor : miscGroup.getChildren()){
			if(actor instanceof Entity && ((Entity) actor).isFlaggedForDelete){
				if(actor instanceof CollidableEntity){
					world.destroyBody(((CollidableEntity) actor).getBody());
				}
				actor.remove();
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		//update camera height to resized height
		camera.viewportHeight = height/Game.PPM;
		camera.viewportWidth = width/Game.PPM;
		camera.update(); 
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		TimeGame.assets.unload(mapFile);
		renderer.dispose();
		
		uiStage.dispose();
		gameStage.dispose();
		
		font.dispose(); //temporary
		
		TimeGame.assets.unload("sound/walksound.mp3");
		TimeGame.assets.unload("sound/warp2.ogg");
		TimeGame.assets.unload("img/laser.png"); //projectile
		TimeGame.assets.unload("img/kittenTransparent3.png"); //player
		TimeGame.assets.unload("ui/menuSkin.json"); //crappy ui
		TimeGame.assets.unload("temp inventory resources/skins/uiskin.json"); //temporary inventory
		TimeGame.assets.unload("temp inventory resources/icons/icons.atlas"); //temporary inventory
		TimeGame.assets.unload(EntityEnum.valueOf("BLUE_SLIME").getTextureFile()); //temp sprite loading
		TimeGame.assets.unload(EntityEnum.valueOf("GHOST_KITTEN").getTextureFile());
		TimeGame.assets.unload("img/laser2.png");
		TimeGame.assets.unload("img/whitePixel.png");
	}
	
	private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            camera.zoom += 0.01; //0.005
        }
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            camera.zoom -= 0.01;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.takeDamage(5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getHealth() > 0) {
            player.heal(5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cameraType = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        	cameraType = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
        	cameraType = 2;
        }
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth/Game.PPM);
	}

	private void addEntities(){
		MapObjects objects = map.getLayers().get(Game.WARP_LAYER).getObjects();
		for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) { //load spawn
			if(rectangleObject.getProperties().get("previousMap") != null && rectangleObject.getProperties().get("previousMap").equals(previousLevel)){
				startX = rectangleObject.getRectangle().x;
				startY = rectangleObject.getRectangle().y;
			}
		}
		player = gameStage.addPlayer();
		gameStage.addGhostKitten(player);
		gameStage.addWanderingEntity(2, 2, "BLUE_SLIME");
		for(int i=0; i<100; i++){
			gameStage.addWanderingEntity(24, 50, "FRIENDLY_SLIME");
		}
		gameStage.addAggroEnemyTest(4, 4, "BLUE_SLIME");
		gameStage.addActor(new BlizzardAoE(15, 10, gameStage, "BLIZZARD"));
		gameStage.addActor(player);
		
		gameStage.setKeyboardFocus(player);
	}
	
	private void loadAssets(){
		TimeGame.assets.load("temp inventory resources/skins/uiskin.json", Skin.class); //temporary inventory
		TimeGame.assets.load("temp inventory resources/icons/icons.atlas", TextureAtlas.class); //temporary inventory
		TimeGame.assets.load("ui/menuSkin.json", Skin.class, new SkinLoader.SkinParameter("ui/atlas.pack")); //ui config
		TimeGame.assets.load("img/kittenTransparent3.png", Texture.class); //player
		TimeGame.assets.load("sound/walksound.mp3", Music.class);
		TimeGame.assets.load("sound/warp2.ogg", Sound.class);
		TimeGame.assets.load(EntityEnum.valueOf("BLUE_SLIME").getTextureFile(), Texture.class); //temp sprite loading
		TimeGame.assets.load(EntityEnum.valueOf("GHOST_KITTEN").getTextureFile(), Texture.class);
		TimeGame.assets.load("img/laser.png", Texture.class); //projectile
		TimeGame.assets.load("img/kittenTransparentBlue.png", Texture.class); //ghost texture
		TimeGame.assets.load("img/laser2.png", Texture.class);
		TimeGame.assets.load("img/whitePixel.png", Texture.class);
		TimeGame.assets.load("img/blizzard.png", Texture.class);

		TimeGame.assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		TimeGame.assets.load(mapFile, TiledMap.class);
		TimeGame.assets.finishLoading();
	}
	
	public void setWarpDestination(String warpDestination){
		this.warpDestination = warpDestination;
	}
	
	public void drawScreen(){
		TimeGame.batch.setProjectionMatrix(camera.combined);
		renderer.setView(camera);
		
		//draw background layers
		renderer.render(backgroundLayers);
		TimeGame.batch.begin();
		//draw misc
		miscGroup.draw(TimeGame.batch, 1);
		//draw grounded 
		groundedGroup.draw(TimeGame.batch, 1);
		TimeGame.batch.end();
		//draw foreground layers
		renderer.render(foregroundLayers);
		//draw aerial
		TimeGame.batch.begin();
		aerialGroup.draw(TimeGame.batch, 1);
		TimeGame.batch.end();
		
		//draw ui
		uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		TimeGame.batch.setProjectionMatrix(uiMatrix);
		TimeGame.batch.begin();
			font.draw(TimeGame.batch, Gdx.graphics.getFramesPerSecond() + "          Controls: Z-time warp, V-inventory (copy pasted from somewhere),  -/+ to zoom camera, <- -> to change hp(temp), Right click - shotgun, Middle click - burst", 10, 15);
			font.draw(TimeGame.batch, Game.console, 10, 700);
			font.draw(TimeGame.batch, "Player has " + (int)Math.ceil(player.health) + "/" + (int)player.maxHealth + " HP", 10, 670);
			font.draw(TimeGame.batch, "Camera type: " + cameraType, 10, 640);
			TimeGame.batch.end();
		uiStage.draw();
		
		TimeGame.batch.setProjectionMatrix(camera.combined);
		debugRenderer.render(world, camera.combined);
	}
	
}