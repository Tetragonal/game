package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.time.TimeGame;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

public abstract class Entity extends Actor{

	protected Sprite sprite;
	protected GameStage gameStage;
	protected World world = null;
	protected int health;
	protected boolean isFlaggedForDelete = false;
	
	public Entity(float x, float y, Texture texture){
		this.sprite = new Sprite(texture);
		sprite.setBounds(x/Game.PPM, y/Game.PPM, sprite.getWidth()/Game.PPM, sprite.getHeight()/Game.PPM);
		setBounds(x,y,sprite.getWidth(),sprite.getHeight());
		setTouchable(Touchable.enabled);
	}
	
	public Entity(float x, float y, String entityName){
		Texture t = TimeGame.assets.get(EntityEnum.valueOf(entityName).getTextureFile());
		this.sprite = new Sprite(t);
		sprite.setBounds(x/Game.PPM, y/Game.PPM, sprite.getWidth()/Game.PPM, sprite.getHeight()/Game.PPM);
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
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public void flagForDelete(){
		isFlaggedForDelete = true;
	}
	public boolean isFlaggedForDelete() {
		return isFlaggedForDelete;
	}
}
