package com.mygdx.time.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.time.TimeGame;
import com.mygdx.time.combat.Buff;
import com.mygdx.time.manager.CollisionHandler;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

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
	public final int WARP_SECONDS = 2;
	private boolean canTeleport = false;
	private ArrayList<float[]> positionLog = new ArrayList<float[]>();
	private float testProjSpawn = 0;
	private float testProj2Refill = 0;
	private float testProj2Count = 5;
	private float testProj2Delay = 0;
	private float testProj2FireAngle;
	private boolean testProj2Boolean = false;
	
	public Player(float x, float y, GameStage gameStage, String entityName){
		super(x, y, gameStage, entityName, false);
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
		baseMovementSpeed = 8;
		
		//test
		CollisionHandler.getInstance().allyMobList.add(this);
		System.out.println("added");
		Buff healthRegenBuff = new Buff();
		healthRegenBuff.damagePerTick=-20/Game.ENGINE_FPS;
		this.addBuff(healthRegenBuff);
	}
	
	public void act(float delta){
		secondTimer += delta;
		if(acceptInput == true){
			handleInput(delta);
		}
		super.act(delta);
		playWalkSound();
		handlePositionLog(delta);
		
		testProj2Refill += delta;
		while(!testProj2Boolean == true && testProj2Refill > .2){
			testProj2Refill -= .2;
			testProj2Count++;
			testProj2Count = MathUtils.clamp(testProj2Count, 0, 5);
		}

 	    if(testProj2Boolean == true && testProj2Count > 0){
 	    	testProj2Delay += delta;
 	    	if(testProj2Delay > .075){
 	    		fireProjectile(0,0,20,50,testProj2FireAngle,3,gameStage,"PLAYER_LASER", true);
 	    		testProj2Delay = 0;
 				testProj2Count--;
 	    	}
 	    }else{
 	    	testProj2Boolean = false;
 	    }
	}
	
	public void handlePositionLog(float delta){
		if(secondTimer > WARP_SECONDS){
			for(int i=0; i< positionLog.size(); i++){
				if(Math.abs(positionLog.get(i)[2]-secondTimer+WARP_SECONDS) < .02){
					ghostX = positionLog.get(i)[0]+sprite.getWidth()/2;
					ghostY = positionLog.get(i)[1]+sprite.getHeight()/2;	
					rewindSpot = i;
					if(canTeleport == true){
						tpSound.play();
						setX(positionLog.get(rewindSpot)[0]);
						setY(positionLog.get(rewindSpot)[1]);
						worldDestination.x = getX() + sprite.getWidth()/2;
						worldDestination.y = getY() + sprite.getHeight()/2;
						positionLog.clear();
						secondTimer = 0;
						canTeleport = false;
					}
				}
			}
		}
		positionLog.add(new float[]{getX(),getY(),secondTimer});
		while(positionLog.get(0)[2] < secondTimer-10){
			positionLog.remove(0);
		}
	}
	
	@Override
	protected void positionChanged(){
		sprite.setPosition(getX(),getY());
		super.positionChanged();
		
	}
	
	public void handleInput(float delta){
		inputTimer+= delta;
		testProjSpawn += delta;
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
        	if((Gdx.input.getX() < Gdx.graphics.getWidth()-Game.CAMERA_OFFSET_X || lmbHeldDown == true)){
	        	if(lmbHeldDown == false || inputTimer > .2 || Math.abs(getY()-worldDestination.y+sprite.getHeight()/2) < 1  || Math.abs(getX()-worldDestination.x+sprite.getWidth()/2) < 1){
	        		if(inputTimer > .2)
	        		{
	        			inputTimer = 0;
	        		}
	            	lmbHeldDown = true;
	
	        	    worldCoordinates.set(Gdx.input.getX(), Gdx.input.getY(),0);
	        	    getStage().getCamera().unproject(worldCoordinates);
	
	        	    worldDestination.set(worldCoordinates.x, worldCoordinates.y);
	        	}
        	}
        }else lmbHeldDown = false;
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
    	    worldCoordinates.set(Gdx.input.getX(), Gdx.input.getY(),0);
    	    getStage().getCamera().unproject(worldCoordinates);
    	    float fireAngle = new Vector2(worldCoordinates.x-(sprite.getX()+sprite.getWidth()/2), worldCoordinates.y-(sprite.getY()+sprite.getHeight()/2)).angle();
    		if(testProjSpawn > .25){
    			for(int i=0; i<5; i++){
        			fireProjectile(0, 0, 15, 10, fireAngle-12+i*6, 3,gameStage, "PLAYER_LASER", true);
    			}
    			testProjSpawn = 0;
    		}
    	}
        if(Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)){
 			if(testProj2Count >= 5){
 				worldCoordinates.set(Gdx.input.getX(), Gdx.input.getY(),0);
 		 	    getStage().getCamera().unproject(worldCoordinates);
 		 	    testProj2FireAngle = new Vector2(worldCoordinates.x-(sprite.getX()+sprite.getWidth()/2), worldCoordinates.y-(sprite.getY()+sprite.getHeight()/2)).angle();
				testProj2Boolean = true;
			}
    			
    	}
	}
	
	private void playWalkSound(){
		/*
		if(body.getLinearVelocity().x != 0 && body.getLinearVelocity().y != 0 && !walkSound.isPlaying()){
			walkSound.play();
		}else if (body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0 && walkSound.isPlaying()){
			walkSound.pause();
		}
		*/
	}
}