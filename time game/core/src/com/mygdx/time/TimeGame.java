package com.mygdx.time;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.time.manager.LevelScreenManager;
import com.mygdx.time.manager.MusicManager;

public class TimeGame extends Game {
	
	public static SpriteBatch batch;
	public static final AssetManager assets = new AssetManager();
	
	@Override
	public void create() {
		LevelScreenManager.getInstance();
		MusicManager.getInstance();
		
		Texture.setAssetManager(assets);

		batch = new SpriteBatch();
		batch.enableBlending();
		
		//LevelScreenManager.getInstance().setScreen("map_1", "map_2");
		LevelScreenManager.getInstance().setScreen("SewersMap1", "map_1");
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
	}

	@Override
	public void render() {
		assets.update();
		super.render();
		MusicManager.getInstance().update(Gdx.graphics.getDeltaTime());
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
		MusicManager.getInstance().dispose();
		batch.dispose();
		assets.dispose();
		Gdx.app.exit(); //https://stackoverflow.com/questions/16161714/what-does-al-lib-alc-cleanup-1-device-not-closed-mean
	}	
}
