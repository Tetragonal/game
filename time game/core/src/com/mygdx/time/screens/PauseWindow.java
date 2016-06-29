package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.time.TimeGame;

public class PauseWindow extends Window {
	
	Skin skin;
	PauseWindow pauseWindow = this;
	
	public PauseWindow(Skin skin){
		super("", skin);
		this.skin = skin;
		
		TextButton resumeButton = new TextButton("Resume", skin);
		TextButton exitButton = new TextButton("Quit", skin);
		TextButton menuButton = new TextButton("Menu", skin);
		Label titleLabel = new Label("Paused", skin);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				pauseWindow.setVisible(false);
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.exit();
			}
		});
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				((TimeGame)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.exit();
			}
		});
		pauseWindow.align(Align.top);
		pauseWindow.add(titleLabel).padBottom(120).row();
		pauseWindow.add(resumeButton).size(250, 75).padBottom(15).row();
		pauseWindow.add(menuButton).size(200, 75).padBottom(15).row();
		pauseWindow.add(exitButton).size(200, 75);
		pauseWindow.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/1.5f);
		pauseWindow.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/6f);
		
		pauseWindow.setVisible(false);
		
	}

	

}
