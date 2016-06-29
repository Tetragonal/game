package com.mygdx.time.entities;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerHealthBar extends Actor{
	
	//doesnt work atm
	
	private Player player;
	private float healthProportion;
	
	public PlayerHealthBar(Player player, int width, int height){
		this.setWidth(width);
		this.setHeight(height);
		this.player = player;
		
		healthProportion = player.getHealth()/player.getMaxHealth();
	}
	
	
	public void act(float delta){
		healthProportion = player.getHealth()/player.getMaxHealth();
	}
	
	public float getHealthProportion(){
		return healthProportion;
	}
}
