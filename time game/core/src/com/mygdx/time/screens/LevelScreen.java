package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.time.TimeGame;
import com.mygdx.time.entities.Player;
import com.mygdx.time.manager.LevelScreenEnum;
import com.mygdx.time.manager.LevelScreenManager;
import com.mygdx.time.manager.MusicManager;

public abstract class LevelScreen implements Screen{
	
	private Group entities = new Group();
	private Window pauseWindow;
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();
	
	protected Stage stage, uiStage;
	protected TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private TiledMapTileLayer layer;
	
	protected String mapFile;
	protected String musicFile;
	
	private BitmapFont font = new BitmapFont();
	private Batch spriteBatch;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private FitViewport viewport;
	private Skin skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));
	
	private Player player;
	protected float startX, startY;
	protected float musicVolume;
	
	private int[] backgroundLayers = {0,1};
	private int[] foregroundLayers = {2};
	
	private Texture ghost = new Texture(Gdx.files.internal("img/kittenTransparentBlue.png"));
	private Matrix4 uiMatrix = new Matrix4();
	
	private TimeGame game;
	
	public static final int CAMERA_OFFSET_X = 0;	
	
	@Override
	public void show() { //create()
		
		game = (TimeGame) Gdx.app.getApplicationListener();
		spriteBatch = game.batch;
		
		map = new TmxMapLoader().load(mapFile);
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();
		
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		stage = new Stage(viewport, spriteBatch);
		uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), spriteBatch);
		stage.addActor(entities);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(uiStage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		layer = (TiledMapTileLayer) map.getLayers().get(2);
		player = new Player(layer, startX*layer.getTileWidth(), (layer.getHeight()-startY)*layer.getTileHeight());
		entities.addActor(player);
		stage.setKeyboardFocus(player);	
		
		//set music
		if(musicFile != null){
			MusicManager.getInstance().setTransitionMusic(Gdx.files.internal(musicFile), 6, musicVolume);
		}  
		
		//independent pause menu
		skin.getFont("black").getData().setScale(0.5f);
		pauseWindow = new Window("", skin);
		TextButton resumeButton = new TextButton("Resume", skin);
		TextButton exitButton = new TextButton("Quit", skin);
		TextButton menuButton = new TextButton("Menu", skin);
		Label titleLabel = new Label("Paused", skin);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				pauseWindow.setVisible(false);
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.exit();
			}
		});
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new MainMenu());
				
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.exit();
			}
		});
		pauseWindow.align(Align.top);
		pauseWindow.add(titleLabel).padBottom(120).row();
		pauseWindow.add(resumeButton).size(250, 75).padBottom(15).row();
		pauseWindow.add(menuButton).size(200, 75).padBottom(15).row();
		pauseWindow.add(exitButton).size(200, 75);
		pauseWindow.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/1.5f);
		pauseWindow.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/6f);
		uiStage.addActor(pauseWindow);
		pauseWindow.setVisible(false);
		
		stage.addCaptureListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode){
				if(keycode == Keys.ESCAPE){
					pauseWindow.setVisible(!pauseWindow.isVisible());
				}
				return true;
			}
		});
	}

	@Override
	public void render(float delta) { //called every frame
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();
		if(!pauseWindow.isVisible()){
			stage.act(Gdx.graphics.getDeltaTime());
		}else {
			player.walkSound.pause();;
		}
		MusicManager.getInstance().update(delta);
		
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
		
		//draw cat
		spriteBatch.begin();
		if(player.secondTimer > player.WARP_SECONDS){
			spriteBatch.draw(ghost, player.ghostX-ghost.getWidth()/2,player.ghostY-ghost.getHeight()/2);
		}
		
		//draw actors
		entities.draw(spriteBatch, 1);
		spriteBatch.end();
		
		//draw foreground layers
		renderer.render(foregroundLayers);
		
		//draw ui
		uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.setProjectionMatrix(uiMatrix);
		spriteBatch.begin();
			font.draw(spriteBatch, Gdx.graphics.getFramesPerSecond() + "          Controls: Z-time warp, -/+ to zoom camera", 10, 15);
		spriteBatch.end();
		uiStage.draw();
		spriteBatch.setProjectionMatrix(camera.combined);
		
		
		//draw rectangle
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.rect(camera.viewportWidth-CAMERA_OFFSET_X, 0, CAMERA_OFFSET_X, camera.viewportHeight);
		shapeRenderer.end();
		
//		Vector2 test = new Vector2((Gdx.graphics.getWidth()/3) ,Gdx.graphics.getHeight()-((Gdx.graphics.getHeight()-pauseWindow.getHeight())/4));
//		test.set(viewport.unproject(test));
//		pauseWindow.setSize(Gdx.graphics.getWidth()/3f*camera.zoom, Gdx.graphics.getHeight()/1.5f*camera.zoom);
//		skin.getFont("black").getData().setScale(0.5f*camera.zoom);
//		pauseWindow.setPosition(test.x, test.y);
//		spriteBatch.begin();
//		ui.draw(spriteBatch, 1);
//		spriteBatch.end();
		
		
		if(player.warpDestination != null)
		{
			LevelScreenManager.getInstance().setScreen(LevelScreenEnum.valueOf(player.warpDestination));
		}
	}

	@Override
	public void resize(int width, int height) {
		//update camera height to resized height
		//kinda useless since resizing is disabled
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
		map.dispose();
		renderer.dispose();
		ghost.dispose();
		uiStage.dispose();
		skin.dispose();
		stage.dispose();
		font.dispose();
		shapeRenderer.dispose();
		
	}
	
	private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            camera.zoom += 0.005;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            camera.zoom -= 0.005;
        }
        
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth);
	}
	

}
