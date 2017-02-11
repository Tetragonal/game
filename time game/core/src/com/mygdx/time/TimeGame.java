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
//		setScreen(new Sandbox("map_1", "Sandbox"));
//		setScreen(new MapTwo("map_1", "map_2"));
		LevelScreenManager.getInstance().setScreen("map_1", "map_2");
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
	}	
}
