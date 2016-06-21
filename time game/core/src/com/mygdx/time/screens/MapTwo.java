package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.time.TimeGame;
import com.mygdx.time.entities.Player;

public class MapTwo implements Screen{
private Stage stage;
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Music music;
	
	private BitmapFont font = new BitmapFont();
	private Batch spriteBatch;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private FitViewport viewport;
	
	private Player player;
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
		//load map
		map = new TmxMapLoader().load("map/test map 2.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();
		
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		stage = new Stage(viewport, spriteBatch);
		Gdx.input.setInputProcessor(stage);
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(2);
		player = new Player(layer, 7.5f*layer.getTileWidth(), (layer.getHeight()-7.5f)*layer.getTileHeight());
		stage.addActor(player);
		stage.setKeyboardFocus(player);	
		
		//loop music
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/fourseasons.mp3"));
        music.setLooping(true);
        music.setVolume(0.2f);
        //music.play();	
        
        
	}

	@Override
	public void render(float delta) { //called every frame
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		handleInput();
		stage.act(Gdx.graphics.getDeltaTime());
		
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
		spriteBatch.end();
		
		//draw actors
		stage.draw();
		
		//draw foreground layers
		renderer.render(foregroundLayers);
		
		//draw fps counter
		uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.setProjectionMatrix(uiMatrix);
		spriteBatch.begin();
		font.draw(spriteBatch, Gdx.graphics.getFramesPerSecond() + "          Controls: Z-time warp, -/+ to zoom camera", 10, 15);
		spriteBatch.end();
		spriteBatch.setProjectionMatrix(camera.combined);
		
		//draw rectangle
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.rect(camera.viewportWidth-CAMERA_OFFSET_X, 0, CAMERA_OFFSET_X, camera.viewportHeight);
		shapeRenderer.end();
		
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
		map.dispose();
		renderer.dispose();
		music.dispose();
		stage.dispose();
		font.dispose();
		//spriteBatch.dispose();
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
