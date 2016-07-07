package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.time.screens.KinematicEntity;

public class GhostKitten extends KinematicEntity{

	private Player attachedPlayer;
	Vector2 position = new Vector2();
	
	public GhostKitten(MapLayer collisionLayer, float x, float y, Texture texture, Player attachedPlayer, World world, String name) {
		super(collisionLayer, x, y, texture, world, name);
		this.attachedPlayer = attachedPlayer;
	}
	
	public void act(float delta){
		super.act(delta);
		this.setPosition(attachedPlayer.ghostX-this.getWidth()/2, attachedPlayer.ghostY-this.getHeight()/2);
		position.set(attachedPlayer.ghostX-this.getWidth()/2, attachedPlayer.ghostY-this.getHeight()/2);
		body.setTransform(attachedPlayer.ghostX-this.getWidth()/2, attachedPlayer.ghostY-this.getHeight()/2 , 0);
		if(attachedPlayer.getHealth() <= 0){
			this.isFlaggedForDelete = true;
		}
		setVisible(attachedPlayer.secondTimer > attachedPlayer.WARP_SECONDS);
	}

}
