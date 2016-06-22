package com.mygdx.time;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.time.manager.LevelScreenManager;
import com.mygdx.time.screens.Splash;

public class TimeGame extends Game {
	
	public SpriteBatch batch;
	
	@Override
	public void create() {
		LevelScreenManager.getInstance().initialize();
		
		batch = new SpriteBatch();
		setScreen(new Splash());
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();	
	}

	@Override
	public void dispose() {
		super.dispose();	
		batch.dispose();
	}

}
