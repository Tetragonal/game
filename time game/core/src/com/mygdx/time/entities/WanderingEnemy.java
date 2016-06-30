package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;

public class WanderingEnemy extends Mob{

	public WanderingEnemy(TiledMapTileLayer collisionLayer, float x, float y, Texture texture) {
		super(collisionLayer, x, y, texture);
	}

	public void act(float delta){
		if(MathUtils.random(1000) > 992){
			worldDestination.set(sprite.getX()+sprite.getWidth()/2+MathUtils.random(-50, 50), sprite.getY()+sprite.getHeight()/2+MathUtils.random(-50,50));
		}
		super.act(delta);
	}
	
}
