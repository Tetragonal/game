package com.mygdx.time.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.TimeGame;
import com.mygdx.time.map.Game;

public class MobHealthBar extends Entity{
	
	private Mob parentMob;
	private Sprite bgSprite;

	
	public MobHealthBar(Mob parentMob) {
		super(parentMob.getX(), parentMob.getY(), (Texture)TimeGame.assets.get("img/whitePixel.png"));
		this.parentMob = parentMob;
		parentMob.attachedEntities.add(this);
		parentMob.getStage().addActor(this);
		
		Texture texture = TimeGame.assets.get("img/whitePixel.png");
		bgSprite = new Sprite(texture);
		bgSprite.setBounds(parentMob.getX(), parentMob.getY(), sprite.getWidth()/Game.PPM, sprite.getHeight()/Game.PPM);
		bgSprite.setColor(Color.RED);

		sprite.setColor(Color.GREEN);
		this.setSize(parentMob.sprite.getWidth(), parentMob.sprite.getWidth()/10);
		
		float spriteSize = parentMob.sprite.getWidth()*parentMob.getHealthPercent();
		MathUtils.clamp(spriteSize, 0, parentMob.sprite.getWidth());
		sprite.setSize(spriteSize, parentMob.sprite.getWidth()/10);
		bgSprite.setSize(parentMob.sprite.getWidth()*parentMob.getHealthPercent(), parentMob.sprite.getWidth()/10);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		bgSprite.draw(batch);
		sprite.draw(batch);
	}
	
	
	@Override
	protected void positionChanged(){
		if(bgSprite != null){
			bgSprite.setPosition(getX(), getY());
		}
		super.positionChanged();
	}
	
	
	public void act(float delta){
		super.act(delta);
		this.setPosition(parentMob.getBody().getPosition().x, parentMob.getBody().getPosition().y+parentMob.sprite.getHeight()*1.1f);
		float spriteSize = MathUtils.clamp(parentMob.sprite.getWidth()*parentMob.getHealthPercent(), 0, parentMob.sprite.getWidth());
		sprite.setSize(spriteSize, parentMob.sprite.getWidth()/10);
		
		if(parentMob.getHealth() <= 0){
			this.isFlaggedForDelete = true;
		}
	}
	
}
