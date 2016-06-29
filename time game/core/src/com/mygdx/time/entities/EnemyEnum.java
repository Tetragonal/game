package com.mygdx.time.entities;

public enum EnemyEnum {

	BLUE_SLIME("Blue slime", "img/bubly.png"),
	GHOST_KITTEN("Ghost", "img/kittenTransparentBlue.png");
	
	private String enemyName;
	private String textureFile;
	
	EnemyEnum(String enemyName, String textureFile){
		this.enemyName = enemyName;
		this.textureFile = textureFile;
	}
	
	public String getEnemyName(){
		return enemyName;
	}
	
	public String getTextureFile(){
		return textureFile;
	}
}
