package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.time.TimeGame;

public class GhostKitten extends Entity{

	private Player parentPlayer;
	
	public GhostKitten(float x, float y, Player attachedPlayer) {
		super(x, y, (Texture)TimeGame.assets.get("img/kittenTransparentBlue.png"));
		this.parentPlayer = attachedPlayer;
		this.sprite.setAlpha(0.5f);
	}
	
	public void act(float delta){
		super.act(delta);
		this.setPosition(parentPlayer.ghostX-this.getWidth()/2, parentPlayer.ghostY-this.getHeight()/2);
		if(parentPlayer.getHealth() <= 0){
			this.isFlaggedForDelete = true;
		}
		setVisible(parentPlayer.secondTimer > parentPlayer.WARP_SECONDS);
	}
}
