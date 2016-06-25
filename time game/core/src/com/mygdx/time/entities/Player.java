package com.mygdx.time.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.time.screens.LevelScreen;

public class Player extends Actor{
	
	Sprite sprite = new Sprite(new Texture("img/kittenTransparent3.png")); //even x even pixels or the collision bugs out

	private float maxSpeed = 120*2f; //60*2
	private String blockedKey = "blocked";
	private String warpKey = "warp";
	private TiledMapTileLayer collisionLayer;
	public Music walkSound = Gdx.audio.newMusic(Gdx.files.internal("sound/walksound.mp3"));
	private Sound tpSound = Gdx.audio.newSound(Gdx.files.internal("sound/warp2.ogg"));
	
	private Vector3 worldCoordinates = new Vector3();
	private Vector2 location = new Vector2();
	private Vector2 centerPosition = new Vector2();
	private Vector2 worldDestination = new Vector2();
	private Vector2 direction = new Vector2();

	private boolean lmbHeldDown = false;
	private float inputTimer = 0;
	public float secondTimer = 0;
	
	public float ghostX;
	public float ghostY;
	private int rewindSpot;
	public float cameraX = 0;
	public float cameraY = 0;
	private Cell cell;
	
	public final int WARP_SECONDS = 3;
	
	private boolean tp = false;
	
	public String warpDestination;
	
	ArrayList<float[]> positionLog = new ArrayList<float[]>();
	
	private float mouseAngle;
	
	public Player(TiledMapTileLayer collisionLayer, float x, float y){
		setBounds(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
		setTouchable(Touchable.enabled);
		location.set(x,y);
		
		worldDestination.set(x+sprite.getWidth()/2, y+sprite.getHeight()/2);
		
		walkSound.setVolume(0.2f);
		
		this.collisionLayer = collisionLayer;
		walkSound.setLooping(true);
		
		addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode){
				if(keycode == Input.Keys.Z && secondTimer > WARP_SECONDS){
					tp = true;
				}
				return true;
			}
		});
		
		ghostX = x+sprite.getWidth()/2;
		ghostY = y+sprite.getHeight()/2;
	}
	
	public void act(float delta){
		secondTimer += delta;
		handleInput(delta);
		playWalkSound();
		move(delta);
		handlePositionLog(delta);
		checkWarp(location.x+sprite.getWidth(), location.y+sprite.getHeight());
		checkWarp(location.x, location.y+sprite.getHeight());
		checkWarp(location.x+sprite.getWidth(), location.y);
		checkWarp(location.x-1, location.y-1);
	}
	
	//-------------------------------------------------------------------
	
	public void checkWarp(float x, float y){
		cell = collisionLayer.getCell((int)(x/collisionLayer.getTileWidth()), (int)(y/collisionLayer.getTileHeight()));
		if(cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(warpKey)){
			warpDestination = (String) cell.getTile().getProperties().get(warpKey);
		}
	}
	
	public void disposeAssets(){
		walkSound.dispose();
		tpSound.dispose();
		sprite.getTexture().dispose();
	}
	
	public void handlePositionLog(float delta){
		if(secondTimer > WARP_SECONDS){
			for(int i=0; i< positionLog.size(); i++){
				if(Math.abs(positionLog.get(i)[4]-secondTimer+WARP_SECONDS) < .02){
					ghostX = positionLog.get(i)[0]+sprite.getWidth()/2;
					ghostY = positionLog.get(i)[1]+sprite.getHeight()/2;	
					rewindSpot = i;
					if(tp == true){
						tpSound.play();
						location.x = positionLog.get(rewindSpot)[0];
						location.y = positionLog.get(rewindSpot)[1];							
						setX(positionLog.get(rewindSpot)[2]);
						setY(positionLog.get(rewindSpot)[3]);
						worldDestination.x = location.x+sprite.getWidth()/2;
						worldDestination.y = location.y+sprite.getHeight()/2;
						positionLog.clear();
						secondTimer = 0;
						tp = false;
					}
				}else if(Math.abs(positionLog.get(i)[4]-secondTimer+.5) < .02){
					cameraX = positionLog.get(i)[0]+sprite.getWidth()/2;
					cameraY = positionLog.get(i)[1]+sprite.getHeight()/2;
				}
			}
		}
		positionLog.add(new float[]{location.x,location.y,getX(),getY(),secondTimer});
		while(positionLog.get(0)[4] < secondTimer-10){
			positionLog.remove(0);
		}
	}
	
	@Override
	protected void positionChanged(){
		sprite.setPosition(getX(),getY());
		super.positionChanged();
		
	}
	
	public void draw(Batch batch, float parentAlpha){
		sprite.draw(batch);
	}
	
	public void handleInput(float delta){
		inputTimer+= delta;
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
        	if((Gdx.input.getX() < Gdx.graphics.getWidth()-LevelScreen.CAMERA_OFFSET_X || lmbHeldDown == true)){
	        	if(lmbHeldDown == false || inputTimer > .2 || Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) < 1  || Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) < 1 || collidesRight() || collidesTop() || collidesBottom() || collidesLeft()){
	        		if(inputTimer > .2)
	        		{
	        			inputTimer = 0;
	        		}
	            	lmbHeldDown = true;
	            	centerPosition.set(getX()+sprite.getWidth()/2, getY()+sprite.getHeight()/2);
	
	        	    worldCoordinates.set(Gdx.input.getX(), Gdx.input.getY(),0);
	        	    getStage().getCamera().unproject(worldCoordinates);
	
	        	    worldDestination.set(worldCoordinates.x, worldCoordinates.y);
	        	    direction.set(worldDestination.x-centerPosition.x, worldDestination.y-centerPosition.y);
	        	    mouseAngle  = direction.angle();
	        	}
        	}
        }else lmbHeldDown = false;
	}
	
	public void move(float delta){
	    direction.set(worldDestination.x-sprite.getX()-sprite.getWidth()/2, worldDestination.y-sprite.getY()-sprite.getHeight()/2);
	    mouseAngle  = direction.angle();
	    
        moveX((float) (maxSpeed * MathUtils.cosDeg(mouseAngle)), delta);
		moveY((float) (maxSpeed * MathUtils.sinDeg(mouseAngle) * delta));
        if(collidesRight() || collidesLeft()){
        	setX((float)Math.floor(location.x));
        }else{
        	setX(location.x);
        }
        
        if(collidesTop() || collidesBottom()){
        	setY((float)Math.floor(location.y));
        }else{
        	setY(location.y);
        }
	}
	
	private void playWalkSound(){
		if(((Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1 && !(collidesTop() || collidesBottom())) || (Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) >= 1) && !(collidesLeft() || collidesRight()) && !walkSound.isPlaying())){
			walkSound.play();
		}else if((Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) < 1 || collidesTop() || collidesBottom()) && (Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) < 1 || collidesLeft() || collidesRight()) && walkSound.isPlaying() ){
			walkSound.pause();
		}
	}
	
	public void moveX(float velocity, float delta){
		float distance = velocity*delta;
		if(distance > 0 && Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) >= 1){
			float i=0;
			for(; i < distance; i++){
				if(collidesRight() || Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) < 1){
					break;
				}
				location.set(location.x+1, location.y);
			}
			if(!collidesRight() && Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) > 1){
				location.set(location.x+(distance-i), location.y);
			}
		}else if(distance < 0 && Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) >= 1){
			float i=distance;
			for(; i < 0; i++){
				if(!(collidesLeft() || Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) < 1)){
					location.set(location.x-1, location.y);
				}
			}
			if(!collidesLeft() && Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) >= 1){
				location.set(location.x+i, location.y);
			}
		}
	}
	
	public void moveY(float distance){
		if(distance > 0 && Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1){
			float i = 0;
			for(; i < distance; i++){
				if(collidesTop() || Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) < 1){
					break;
				}
				location.set(location.x, location.y+1);
			}
			if(!collidesTop() && Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1){
				location.set(location.x, location.y+(distance-i));
			}
		}else if(distance < 0 && Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1){
			float i=distance;
			for(; i < 0; i++){
				if(collidesBottom() || Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) < 1){
					break;
				}
				location.set(location.x, location.y-1);
			}
			if(!collidesBottom() && Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1){
				location.set(location.x, location.y+i);
			}
		}
	}
	
	public boolean isCellBlocked(float x, float y){
		cell = collisionLayer.getCell((int)(x/collisionLayer.getTileWidth()), (int)(y/collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
	}
	
	public boolean collidesRight(){
		for(float step = 0; step < getWidth(); step ++){
			if(isCellBlocked(location.x+getWidth(), location.y+step)){
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesLeft(){
		for(float step = 0; step < getWidth(); step ++){
			if(isCellBlocked(location.x-1, location.y+step)){
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesTop(){
		for(float step = 0; step < getHeight(); step ++){
			if(isCellBlocked(location.x+step, location.y+getHeight())){
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesBottom(){
		for(float step = 0; step < getHeight(); step ++){
			if(isCellBlocked(location.x+step, location.y-1)){
				return true;
			}
		}
		return false;
	}
}