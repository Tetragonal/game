package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.time.TimeGame;
import com.mygdx.time.entities.WanderingEnemy;
import com.mygdx.time.entities.AggroEnemyTest;
import com.mygdx.time.entities.EnemyEnum;
import com.mygdx.time.entities.Entity;
import com.mygdx.time.entities.GhostKitten;
import com.mygdx.time.entities.Player;
import com.mygdx.time.entities.PlayerHealthBar;
import com.mygdx.time.inventory.Inventory;
import com.mygdx.time.inventory.InventoryWindow;
import com.mygdx.time.manager.LevelScreenManager;
import com.mygdx.time.manager.MusicManager;

public abstract class LevelScreen implements Screen{
	
	protected TiledMap map;
	protected Stage stage, uiStage;
	protected String mapFile;
	protected String musicFile;
	
	//map information
	protected int[] backgroundLayers = {0,1};
	protected int[] foregroundLayers = {2};
	private String currentLevel, previousLevel;
	
	protected float startX, startY;
	protected float musicVolume;
	
	private PauseWindow pauseWindow;
	private InventoryWindow inventoryWindow;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private TiledMapTileLayer collisionLayer;
	private FitViewport viewport;
	
	private BitmapFont font = new BitmapFont(); //temporary
	private ShapeRenderer shapeRenderer = new ShapeRenderer(); //temporary
	private Matrix4 uiMatrix = new Matrix4();
	
	private Group entities = new Group();
	private Player player;
	private GhostKitten ghostKitten;
	
	public static final int CAMERA_OFFSET_X = 0;	
	
	private PlayerHealthBar playerHealthBar;
	
	public LevelScreen(String previousLevel, String currentLevel){
		this.previousLevel = previousLevel;
		this.currentLevel = currentLevel; 
	}
	
	@Override
	public void show() {
		
		//load assets
		TimeGame.assets.load("temp inventory resources/skins/uiskin.json", Skin.class); //temporary inventory
		TimeGame.assets.load("temp inventory resources/icons/icons.atlas", TextureAtlas.class); //temporary inventory
		TimeGame.assets.load("ui/menuSkin.json", Skin.class, new SkinLoader.SkinParameter("ui/atlas.pack")); //ui config

		TimeGame.assets.load("img/kittenTransparent3.png", Texture.class); //player
		TimeGame.assets.load(EnemyEnum.valueOf("BLUE_SLIME").getTextureFile(), Texture.class); //temp sprite loading
		TimeGame.assets.load(EnemyEnum.valueOf("GHOST_KITTEN").getTextureFile(), Texture.class);
		TimeGame.assets.load("img/laser.png", Texture.class); //projectile
		
		TimeGame.assets.load("img/kittenTransparentBlue.png", Texture.class); //ghost texture
		
		TimeGame.assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		TimeGame.assets.load(mapFile, TiledMap.class);
		TimeGame.assets.finishLoading();
		
		Skin menuSkin = TimeGame.assets.get("ui/menuSkin.json");
		Skin uiSkin = TimeGame.assets.get("temp inventory resources/skins/uiskin.json");
		
//		map = new TmxMapLoader().load(mapFile);
		map = TimeGame.assets.get(mapFile);
		
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();
		
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		stage = new Stage(viewport, TimeGame.batch);
		uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), TimeGame.batch);

		stage.addActor(entities);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, uiStage));
		collisionLayer = (TiledMapTileLayer) map.getLayers().get(2);
		MapObjects objects = map.getLayers().get(3).getObjects();
		for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
			if(rectangleObject.getProperties().get("previousMap").equals(previousLevel)){
				startX = rectangleObject.getRectangle().x;
				startY = rectangleObject.getRectangle().y;
			}
		}
		player = new Player(collisionLayer,startX, startY, TimeGame.assets.get("img/kittenTransparent3.png"));
		ghostKitten = new GhostKitten(collisionLayer,startX, startY, TimeGame.assets.get(EnemyEnum.valueOf("GHOST_KITTEN").getTextureFile()), player);
		WanderingEnemy testEnemy = new WanderingEnemy(collisionLayer,startX+50, startY, TimeGame.assets.get(EnemyEnum.valueOf("BLUE_SLIME").getTextureFile()));
		AggroEnemyTest testEnemy2 = new AggroEnemyTest(collisionLayer,startX-50, startY, TimeGame.assets.get(EnemyEnum.valueOf("BLUE_SLIME").getTextureFile()));
		entities.addActor(ghostKitten);
		entities.addActor(testEnemy);
		entities.addActor(testEnemy2);
		entities.addActor(player);
		stage.setKeyboardFocus(player);	
		
		//set music
		if(musicFile != null){
			MusicManager.getInstance().setTransitionMusic(Gdx.files.internal(musicFile), 6, musicVolume);
		}  
		
		//independent pause menu
		menuSkin.getFont("black").getData().setScale(0.5f);
		pauseWindow = new PauseWindow(menuSkin);
		uiStage.addActor(pauseWindow);
		stage.addCaptureListener(new InputListener() {
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

		inventoryWindow = new InventoryWindow(new Inventory(), new DragAndDrop(), uiSkin, uiStage);
		uiStage.addActor(inventoryWindow);
		
		//doesnt actually display atm
		playerHealthBar = new PlayerHealthBar(player, (int)camera.viewportWidth, 50);
		uiStage.addActor(playerHealthBar);
	}

	@Override
	public void render(float delta) { //called every frame
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		

		player.acceptInput = !inventoryWindow.isVisible();

		
		if(!pauseWindow.isVisible()){
			handleInput();
			stage.act(delta);
			uiStage.act(delta);
		}else {
			player.walkSound.pause();
			inventoryWindow.setVisible(false);
		}
		
		if(player.getHealth() <=0){
			player.disposeAssets();
			player.remove();
			ghostKitten.remove();
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
//		entities.draw(TimeGame.batch, 1);
//		TimeGame.batch.end();
		ghostKitten.setVisible(player.secondTimer > player.WARP_SECONDS);
		stage.draw();
		
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
		shapeRenderer.rect(camera.viewportWidth-CAMERA_OFFSET_X, 0, CAMERA_OFFSET_X, camera.viewportHeight);
		shapeRenderer.rect(0, camera.viewportHeight-25, camera.viewportWidth, 25);
		shapeRenderer.setColor(0, 1, 0, 1);
		shapeRenderer.rect(0, camera.viewportHeight-25, camera.viewportWidth*playerHealthBar.getHealthProportion(), 25);
		shapeRenderer.end();
		
		if(player.warpDestination != null)
		{
			LevelScreenManager.getInstance().setScreen(player.warpDestination, currentLevel);
		}
	}

	@Override
	public void resize(int width, int height) {
		//update camera height to resized height
		camera.viewportHeight = height;
		camera.viewportWidth = width;
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
		player.disposeAssets();
		TimeGame.assets.unload(mapFile);
		renderer.dispose();
		
		uiStage.dispose();
		stage.dispose();
		
		font.dispose(); //temporary
		shapeRenderer.dispose(); //temporary
		
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
            camera.zoom += 0.005;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            camera.zoom -= 0.005;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.takeDamage(5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getHealth() > 0) {
            player.heal(5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.rotateBy(1);
        }
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth);
	}
	
	public Stage getStage(){
		return stage;
	}

}
