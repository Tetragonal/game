package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Filter;
import com.mygdx.time.map.Game;
import com.mygdx.time.screens.GameStage;

public class GhostKitten extends KinematicEntity{

	private Player parentPlayer;
	
	public GhostKitten(float x, float y, Texture texture, Player attachedPlayer, GameStage gameStage, String name) {
		super(x, y, texture, gameStage, name);
		this.parentPlayer = attachedPlayer;
		
		
		 Filter f = new Filter();
	     f.categoryBits = Game.CATEGORY_NONCOLLIDABLE;
	      f.maskBits = Game.CATEGORY_NONCOLLIDABLE;
		
		for(int i=0; i<body.getFixtureList().size; i++){
			body.getFixtureList().get(i).setFilterData(f);
		}
	}
	
	public void act(float delta){
		super.act(delta);
		this.setPosition(parentPlayer.ghostX-this.getWidth()/2, parentPlayer.ghostY-this.getHeight()/2);
		body.setTransform(parentPlayer.ghostX-this.getWidth()/2, parentPlayer.ghostY-this.getHeight()/2 , 0);
		if(parentPlayer.getHealth() <= 0){
			this.isFlaggedForDelete = true;
		}
		setVisible(parentPlayer.secondTimer > parentPlayer.WARP_SECONDS);
	}
}
