package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.time.TimeGame;
import com.mygdx.time.manager.MusicManager;
import com.mygdx.time.tween.SpriteAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class Splash implements Screen{
	
	private Sprite splash;
	private float waitTimer = 0;
	private TweenManager tweenManager = new TweenManager();
	
	@Override
	public void show() {
		//apply preferences
		MusicManager.isMuted = SettingsMenu.mute();
		Gdx.graphics.setVSync(SettingsMenu.vSync());
		
		//load assets
		TimeGame.assets.load("img/splash.png", Texture.class);
		TimeGame.assets.load("sound/Cat meow 23.wav", Sound.class);
		TimeGame.assets.finishLoading();
		
		Texture splashTexture = TimeGame.assets.get("img/splash.png");
		splash = new Sprite(splashTexture);
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		((Sound) TimeGame.assets.get("sound/Cat meow 23.wav")).play();
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 1f).target(1).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 2f).target(0).delay(1).start(tweenManager);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		TimeGame.assets.update();
		tweenManager.update(delta);
		waitTimer += delta;
		
		TimeGame.batch.begin();
		splash.draw(TimeGame.batch);
		TimeGame.batch.end();
		
		if(waitTimer > 3){
			((TimeGame)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
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
		TimeGame.assets.unload("img/splash.png");
		TimeGame.assets.unload("sound/Cat meow 23.wav");
	}

}
