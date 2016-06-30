package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Entity extends Actor{

	protected Sprite sprite;
	
	public Entity(float x, float y, Texture texture){ //, Sprite sprite
		this.sprite = new Sprite(texture);
		setBounds(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
		setTouchable(Touchable.enabled);
		this.setX(x);
		this.setY(y);
	
	}
	
	public void draw(Batch batch, float parentAlpha){
		sprite.draw(batch);
	}
	
	@Override
	protected void positionChanged(){
		sprite.setPosition(getX(),getY());
		super.positionChanged();
		
	}

	public Rectangle getRectangle() { //idk if useful
		return new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
	}
}
