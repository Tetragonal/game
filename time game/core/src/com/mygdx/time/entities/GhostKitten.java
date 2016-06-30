package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class GhostKitten extends Mob{

	private Player attachedPlayer;
	
	public GhostKitten(TiledMapTileLayer collisionLayer, float x, float y, Texture texture, Player attachedPlayer) {
		super(collisionLayer, x, y, texture);
		this.attachedPlayer = attachedPlayer;
	}
	
	public void act(float delta){
		this.setPosition(attachedPlayer.ghostX-this.getWidth()/2, attachedPlayer.ghostY-this.getHeight()/2);
	}

}
