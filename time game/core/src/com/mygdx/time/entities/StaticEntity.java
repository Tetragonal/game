package com.mygdx.time.entities;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.time.Game;
import com.mygdx.time.screens.GameStage;

public class StaticEntity extends CollidableEntity {
	
	public StaticEntity(float x, float y, GameStage gameStage, String name) {
		super(x, y, gameStage, name, Game.MOB, false);
		body.setType(BodyType.StaticBody);
	}

}
