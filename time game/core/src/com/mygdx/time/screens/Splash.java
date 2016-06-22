package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.time.TimeGame;
import com.mygdx.time.tween.SpriteAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class Splash implements Screen{
	
	private Texture splashTexture = new Texture(Gdx.files.internal("img/menu.png"));
	private Sprite splash = new Sprite(splashTexture);
	private float waitTimer = 0;
	private TimeGame game;
	private TweenManager tweenManager = new TweenManager();
	
	
	private Sound meowSound = Gdx.audio.newSound(Gdx.files.internal("sound/Cat meow 23.wav"));

	@Override
	public void show() {
		game = (TimeGame) Gdx.app.getApplicationListener();
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		meowSound.play();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 1f).target(1).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 2f).target(0).delay(1).start(tweenManager);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tweenManager.update(delta);
		waitTimer += delta;
		
		game.batch.begin();
		splash.draw(game.batch);
		game.batch.end();
		
		if(waitTimer > 3){
			game.setScreen(new MainMenu());
		}
	}

	@Override
	public void resize(int width, int height) {
		splash.setSize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		dispose();
		
	}

	@Override
	public void dispose() {
		splashTexture.dispose();
		meowSound.dispose();
	}

}
