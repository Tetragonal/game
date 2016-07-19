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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.time.TimeGame;
import com.mygdx.time.entities.Entity;
import com.mygdx.time.entities.EntityEnum;
import com.mygdx.time.entities.PhysicsEntity;
import com.mygdx.time.entities.Player;
import com.mygdx.time.inventory.Inventory;
import com.mygdx.time.inventory.InventoryWindow;
import com.mygdx.time.manager.LevelScreenManager;
import com.mygdx.time.manager.MusicManager;
import com.mygdx.time.map.Game;
import com.mygdx.time.map.MapLoader;
import com.mygdx.time.map.TestContactListener;


public abstract class LevelScreen implements Screen{
	
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
	
	private BitmapFont font = new BitmapFont(); //temporary
	private ShapeRenderer shapeRenderer = new ShapeRenderer(); //temporary
	private Matrix4 uiMatrix = new Matrix4();
	private Player player;
	
	private float gameTimer = 0;
	
	private World world;
	Box2DDebugRenderer debugRenderer;
	
	public LevelScreen(String previousLevel, String currentLevel){
		this.previousLevel = previousLevel;
		this.currentLevel = currentLevel; 
	}
	
	@Override
	public void show() {
		loadAssets();
		
		Box2D.init();
		
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new TestContactListener(this));

		Skin menuSkin = TimeGame.assets.get("ui/menuSkin.json");
		Skin uiSkin = TimeGame.assets.get("temp inventory resources/skins/uiskin.json");
		
		map = TimeGame.assets.get(mapFile);
		renderer = new OrthogonalTiledMapRenderer(map, 1f/Game.PPM);
		camera = new OrthographicCamera();
		viewport = new FitViewport(Gdx.graphics.getWidth()/Game.PPM, Gdx.graphics.getHeight()/Game.PPM, camera);
		gameStage = new GameStage(viewport, map, world, this);
		uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), TimeGame.batch);
		Gdx.input.setInputProcessor(new InputMultiplexer(gameStage, uiStage));
		
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
					inventoryWindow.setVisible(!inventoryWindow.isVisible());
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
		while(gameTimer >= 1/Game.ENGINE_FPS){
			act();
			gameTimer -= 1/Game.ENGINE_FPS;
		}
		
		//center camera on player
			//Camera centered on you only
//			camera.position.set(CAMERA_OFFSET_X/2*camera.zoom + player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2, 0);
			//Camera follows you
			camera.position.set((float) (camera.position.x + .05f*(Game.CAMERA_OFFSET_X/2f*camera.zoom-camera.position.x + player.getX() + player.getWidth()/2f)), (float) (camera.position.y + .05*(-camera.position.y + player.getY() + player.getHeight()/2f)), 0);
			//TODO Camera follows the midpoint between your pressed cursor and your character
		camera.update();
		
		//draw background layers
		renderer.setView(camera);
		renderer.render(backgroundLayers);
		//draw actors
		gameStage.draw();
		//draw foreground layers
		renderer.render(foregroundLayers);
		
		//draw ui
		uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		TimeGame.batch.setProjectionMatrix(uiMatrix);
		TimeGame.batch.begin();
			font.draw(TimeGame.batch, Gdx.graphics.getFramesPerSecond() + "          Controls: Z-time warp, V-inventory (copy pasted from somewhere),  -/+ to zoom camera, <- -> to change hp(temp)", 10, 15);
		TimeGame.batch.end();
		uiStage.draw();
		TimeGame.batch.setProjectionMatrix(camera.combined);
		
		//draw rectangle
//		shapeRenderer.begin(ShapeType.Filled);
//		shapeRenderer.setColor(1, 0, 0, 1);
//		shapeRenderer.rect(Gdx.graphics.getWidth()-GameConstants.CAMERA_OFFSET_X, 0, GameConstants.CAMERA_OFFSET_X, Gdx.graphics.getHeight());
//		shapeRenderer.end();
		debugRenderer.render(world, camera.combined);

	}

	public void act(){
//		player.acceptInput = !inventoryWindow.isVisible();
		if(!pauseWindow.isVisible()){
			handleInput();
			world.step(1/Game.ENGINE_FPS, 6, 2);
			gameStage.act();
			uiStage.act();
			for(Actor actor : gameStage.getActors()){
				if(((Entity) actor).isFlaggedForDelete()){
					if(actor instanceof PhysicsEntity){
						world.destroyBody(((PhysicsEntity) actor).getBody());
					}
					actor.remove();
				}
			}
		}else {
			player.walkSound.pause();
			inventoryWindow.setVisible(false);
		}
		if(warpDestination != null){
			LevelScreenManager.getInstance().setScreen(warpDestination, currentLevel);
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
		shapeRenderer.dispose(); //temporary
		
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
            camera.zoom += 0.05; //0.005
        }
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            camera.zoom -= 0.05;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.takeDamage(5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getHealth() > 0) {
            player.heal(5);
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
		gameStage.addWanderingEntity(4, 5, "BLUE_SLIME");
		gameStage.addAggroEnemyTest(4, 4, "BLUE_SLIME");
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

		TimeGame.assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		TimeGame.assets.load(mapFile, TiledMap.class);
		TimeGame.assets.finishLoading();
	}
	
	public void setWarpDestination(String warpDestination){
		this.warpDestination = warpDestination;
	}
	
}