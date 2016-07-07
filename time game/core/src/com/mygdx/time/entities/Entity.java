package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.time.screens.LevelScreen;

public abstract class Entity extends Actor{

	protected Sprite sprite;
	protected World world;
	protected int health;
	protected boolean isFlaggedForDelete = false;
	
	public Entity(float x, float y, Texture texture){ //, Sprite sprite
		this.sprite = new Sprite(texture);
		sprite.setBounds(x/LevelScreen.PPM, y/LevelScreen.PPM, sprite.getWidth()/LevelScreen.PPM, sprite.getHeight()/LevelScreen.PPM);
		setBounds(x,y,sprite.getWidth(),sprite.getHeight());
		setTouchable(Touchable.enabled);
	}
	
	public Entity(float x, float y, Texture texture, World world){
		this(x,y,texture);
		this.world = world;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		sprite.draw(batch);
	}
	
	@Override
	protected void positionChanged(){
		sprite.setPosition(getX(),getY());
		super.positionChanged();
		
	}
	
	public boolean isFlaggedForDelete() {
		return isFlaggedForDelete;
	}
}
