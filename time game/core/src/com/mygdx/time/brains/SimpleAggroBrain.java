package com.mygdx.time.brains;

public class SimpleAggroBrain extends WanderingBrain{
	double range;
	public SimpleAggroBrain(String args[]){
		super(args);
		range = Double.parseDouble(args[2]);
	}
	public void act(){
		float distance = parentEntity.gameStage.player.getSpriteCenter().dst(parentEntity.getSpriteCenter());
		boolean playerInRange = (range-distance>0); 
		
		if(!playerInRange){
			super.act();
		}else{
			parentEntity.worldDestination.set(parentEntity.gameStage.player.getSpriteCenter());
		}
	}
}
