package com.mygdx.time.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.time.TimeGame;
import com.mygdx.time.screens.LevelScreen;

public class Player extends Mob{
	
	public Music walkSound;
	private Sound tpSound;
	
	private Vector3 worldCoordinates = new Vector3();

	public boolean acceptInput = true;
	public float secondTimer = 0;
	private boolean lmbHeldDown = false;
	private float inputTimer = 0;
	public float ghostX;
	public float ghostY;
	private int rewindSpot;
	public final int WARP_SECONDS = 3;
	private boolean canTeleport = false;
	private ArrayList<float[]> positionLog = new ArrayList<float[]>();
	
	public String warpDestination;
	TiledMapTileLayer warpLayer;
	
	public Player(MapLayer collisionLayer, float x, float y, Texture texture, World world, String name){
		super(collisionLayer, x, y, texture, world, name);
		
		walkSound = TimeGame.assets.get("sound/walksound.mp3");
		tpSound = TimeGame.assets.get("sound/warp2.ogg");
		walkSound.setVolume(0.2f);
		walkSound.setLooping(true);
		
		addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode){
				if(keycode == Input.Keys.Z && secondTimer > WARP_SECONDS){
					canTeleport = true;
				}
				return true;
			}
		});
		
		ghostX = x+sprite.getWidth()/2;
		ghostY = y+sprite.getHeight()/2;
		
	}
	
	public void act(float delta){
//		location.set(getX(), getY());
		secondTimer += delta;
		if(acceptInput == true){
			handleInput(delta);
		}
		super.act(delta);
		playWalkSound();
		handlePositionLog(delta);
//		checkWarp(location.x+sprite.getWidth(), location.y+sprite.getHeight());
//		checkWarp(location.x, location.y+sprite.getHeight());
//		checkWarp(location.x+sprite.getWidth(), location.y);
//		checkWarp(location.x-1, location.y-1);
	}

	public void checkWarp(float x, float y){ //TODO re-implement warp
//		cell = ((TiledMapTileLayer)collisionLayer).getCell((int)(x/((TiledMapTileLayer)collisionLayer).getTileWidth()), (int)(y/((TiledMapTileLayer)collisionLayer).getTileHeight()));
//		if(cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("warp")){
//			warpDestination = (String) cell.getTile().getProperties().get("warp");
//		}
	}
	
	public void handlePositionLog(float delta){
		if(secondTimer > WARP_SECONDS){
			for(int i=0; i< positionLog.size(); i++){
				if(Math.abs(positionLog.get(i)[4]-secondTimer+WARP_SECONDS) < .02){
					ghostX = positionLog.get(i)[0]+sprite.getWidth()/2;
					ghostY = positionLog.get(i)[1]+sprite.getHeight()/2;	
					rewindSpot = i;
					if(canTeleport == true){
						tpSound.play();
						location.set(positionLog.get(rewindSpot)[0], positionLog.get(rewindSpot)[1]);
						setX(positionLog.get(rewindSpot)[2]);
						setY(positionLog.get(rewindSpot)[3]);
						worldDestination.x = location.x+sprite.getWidth()/2;
						worldDestination.y = location.y+sprite.getHeight()/2;
						body.setTransform(positionLog.get(rewindSpot)[2], positionLog.get(rewindSpot)[3], 0);
						body.setLinearVelocity(0, 0);
						positionLog.clear();
						secondTimer = 0;
						canTeleport = false;
					}
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
	        	if(lmbHeldDown == false || inputTimer > .2 || Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) < 1  || Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) < 1){
	        		if(inputTimer > .2)
	        		{
	        			inputTimer = 0;
	        		}
	            	lmbHeldDown = true;
	
	        	    worldCoordinates.set(Gdx.input.getX(), Gdx.input.getY(),0);
	        	    getStage().getCamera().unproject(worldCoordinates);
	
	        	    worldDestination.set(worldCoordinates.x, worldCoordinates.y);
	        	    direction.set(worldDestination.x-(sprite.getX()+sprite.getWidth()/2), worldDestination.y-(sprite.getY()+sprite.getHeight()/2));
	        	    moveAngle  = direction.angle();
	        	    body.setLinearVelocity(maxSpeed*MathUtils.cosDeg(moveAngle), maxSpeed*MathUtils.sinDeg(moveAngle));
	        	}
        	}
        }else lmbHeldDown = false;
	}
	
	private void playWalkSound(){
//		if(((Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) >= 1)) || (Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) >= 1) && !walkSound.isPlaying()){
//			walkSound.play();
//		}else if((Math.abs(location.y-worldDestination.y+sprite.getHeight()/2) < 1) && (Math.abs(location.x-worldDestination.x+sprite.getWidth()/2) < 1  && walkSound.isPlaying())){
//			walkSound.pause();
//		}
		
		if(body.getLinearVelocity().x != 0 && body.getLinearVelocity().y != 0 && !walkSound.isPlaying()){
			walkSound.play();
		}else if (body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0 && walkSound.isPlaying()){
			walkSound.pause();
		}
		
	}
}