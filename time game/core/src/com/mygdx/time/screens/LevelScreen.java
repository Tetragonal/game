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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
import com.hi5dev.box2d_pexml.PEXML;
import com.mygdx.time.TimeGame;
import com.mygdx.time.entities.EnemyEnum;
import com.mygdx.time.entities.Entity;
import com.mygdx.time.entities.PhysicsEntity;
import com.mygdx.time.entities.Player;
import com.mygdx.time.entities.PlayerHealthBar;
import com.mygdx.time.inventory.Inventory;
import com.mygdx.time.inventory.InventoryWindow;
import com.mygdx.time.manager.LevelScreenManager;
import com.mygdx.time.manager.MusicManager;
import com.mygdx.time.map.MapLoader;

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
	public static final int WARP_LAYER = 3, GROUND_LAYER = 4, AERIAL_LAYER = 5;
	private String currentLevel, previousLevel;
	
	private PauseWindow pauseWindow;
	private InventoryWindow inventoryWindow;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private FitViewport viewport;
	
	private BitmapFont font = new BitmapFont(); //temporary
	private ShapeRenderer shapeRenderer = new ShapeRenderer(); //temporary
	private Matrix4 uiMatrix = new Matrix4();
	private Player player;
	private PlayerHealthBar playerHealthBar;
	
	private float gameTimer = 0;
	
	private World world;
	Box2DDebugRenderer debugRenderer;
	public static PEXML physicsBodies;
	Body ground;
	
	public static final int CAMERA_OFFSET_X = 0;	
	public static final float ENGINE_FPS = 60;
	public static final int PPM = 16;
	
	public LevelScreen(String previousLevel, String currentLevel){
		this.previousLevel = previousLevel;
		this.currentLevel = currentLevel; 
	}
	
	@Override
	public void show() {
		
		Box2D.init();
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0,0), true);
		physicsBodies = new PEXML(Gdx.files.internal("img/catPhysics2.xml").file());
	    
		loadAssets();
		
		Skin menuSkin = TimeGame.assets.get("ui/menuSkin.json");
		Skin uiSkin = TimeGame.assets.get("temp inventory resources/skins/uiskin.json");
		
		map = TimeGame.assets.get(mapFile);
		renderer = new OrthogonalTiledMapRenderer(map, 1f/PPM);
		camera = new OrthographicCamera();
		viewport = new FitViewport(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM, camera);
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
		DragAndDrop dragAndDrop = new DragAndDrop();
		dragAndDrop.setDragActorPosition(200,50);
		inventoryWindow = new InventoryWindow(new Inventory(), dragAndDrop, uiSkin, uiStage);
		uiStage.addActor(inventoryWindow);
		
		//doesnt actually display atm
		playerHealthBar = new PlayerHealthBar(player, (int)camera.viewportWidth, 50);
		uiStage.addActor(playerHealthBar);
		
		
		new MapLoader(map, GROUND_LAYER, world);
		
	}
	
	@Override
	public void render(float delta) { //called every frame
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	    float frameTime = delta;// = Math.min(delta, 0.25f);
		gameTimer += frameTime;
		while(gameTimer >= 1/ENGINE_FPS){
			act();
			gameTimer -= 1/ENGINE_FPS;
		}
		
		//center camera on player
			//Camera centered on you only
//			camera.position.set(CAMERA_OFFSET_X/2*camera.zoom + player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2, 0);
			//Camera follows you
			camera.position.set((float) (camera.position.x + .05*(CAMERA_OFFSET_X/2*camera.zoom-camera.position.x + player.getX() + player.getWidth()/2)), (float) (camera.position.y + .05*(-camera.position.y + player.getY() + player.getHeight()/2)), 0);
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
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.rect(Gdx.graphics.getWidth()-CAMERA_OFFSET_X, 0, CAMERA_OFFSET_X, Gdx.graphics.getHeight());
		shapeRenderer.rect(0, Gdx.graphics.getHeight()-25, Gdx.graphics.getWidth(), 25);
		shapeRenderer.setColor(0, 1, 0, 1);
		shapeRenderer.rect(0, Gdx.graphics.getHeight()-25, Gdx.graphics.getWidth()*playerHealthBar.getHealthProportion(), 25);
		shapeRenderer.end();
		
		debugRenderer.render(world, camera.combined);
	}

	public void act(){
		
		player.acceptInput = !inventoryWindow.isVisible();
		if(!pauseWindow.isVisible()){
			handleInput();
			world.step(1/ENGINE_FPS, 6, 2);
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
		
		
		if(player.warpDestination != null){
			LevelScreenManager.getInstance().setScreen(player.warpDestination, currentLevel);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		//update camera height to resized height
		camera.viewportHeight = height/PPM;
		camera.viewportWidth = width/PPM;
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
		TimeGame.assets.unload(EnemyEnum.valueOf("BLUE_SLIME").getTextureFile()); //temp sprite loading
		TimeGame.assets.unload(EnemyEnum.valueOf("GHOST_KITTEN").getTextureFile());
		
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
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth/PPM);
	}

	private void addEntities(){
		MapObjects objects = map.getLayers().get(WARP_LAYER).getObjects();
		for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) { //load spawn
			if(rectangleObject.getProperties().get("previousMap") != null && rectangleObject.getProperties().get("previousMap").equals(previousLevel)){
				startX = rectangleObject.getRectangle().x;
				startY = rectangleObject.getRectangle().y;
			}
		}
		player = gameStage.addPlayer();
		gameStage.addGhostKitten(player);
		gameStage.addWanderingEntity(2, 2, "BLUE_SLIME", "bubly");
		gameStage.addAggroEnemyTest(4, 4, "BLUE_SLIME", "bubly");
		gameStage.setKeyboardFocus(player);	
	}
	
	private void loadAssets(){
		TimeGame.assets.load("temp inventory resources/skins/uiskin.json", Skin.class); //temporary inventory
		TimeGame.assets.load("temp inventory resources/icons/icons.atlas", TextureAtlas.class); //temporary inventory
		TimeGame.assets.load("ui/menuSkin.json", Skin.class, new SkinLoader.SkinParameter("ui/atlas.pack")); //ui config
		TimeGame.assets.load("img/kittenTransparent3.png", Texture.class); //player
		TimeGame.assets.load("sound/walksound.mp3", Music.class);
		TimeGame.assets.load("sound/warp2.ogg", Sound.class);
		TimeGame.assets.load(EnemyEnum.valueOf("BLUE_SLIME").getTextureFile(), Texture.class); //temp sprite loading
		TimeGame.assets.load(EnemyEnum.valueOf("GHOST_KITTEN").getTextureFile(), Texture.class);
		TimeGame.assets.load("img/laser.png", Texture.class); //projectile
		
		TimeGame.assets.load("img/kittenTransparentBlue.png", Texture.class); //ghost texture
		
		TimeGame.assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		TimeGame.assets.load(mapFile, TiledMap.class);
		TimeGame.assets.finishLoading();
	}
	
}
