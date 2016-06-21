package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.time.TimeGame;
import com.mygdx.time.tween.ActorAccessor;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class MainMenu implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton buttonPlay, buttonExit;
	private Label heading;
	private TweenManager tweenManager;
	private Texture catTexture = new Texture(Gdx.files.internal("img/kittenTransparent3.png"));
	private Texture settingsTexture = new Texture(Gdx.files.internal("img/gear.png"));
	private Image catImage = new Image(catTexture);
	private Image catImage2 = new Image(catTexture);
	private Image settingsImage = new Image(settingsTexture);
	private Music music;
	
	private boolean playClicked = false, exitClicked = false, settingsClicked = false;
	private float fadeTimer = 0;
	private float timer = 0;
	
	@Override
	public void show() {
		 stage = new Stage(new ScreenViewport());
		 
		 Gdx.input.setInputProcessor(stage);
		 
		 atlas = new TextureAtlas("ui/atlas.pack");
		 skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);
		 
		 music = Gdx.audio.newMusic(Gdx.files.internal("sound/castaway.mp3"));
	     music.setLooping(true);
	     music.setVolume(0.2f);
	     music.play();
				 
		 table = new Table(skin);
		 table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		 skin.getFont("black").getData().setScale(0.5f);

		 
		 //settings for header

		 heading = new Label("cat plant menu !", skin);
		 heading.setFontScale(0.7f);
		 
		 //settings for play button
		 buttonPlay = new TextButton("Play", skin);
		 buttonPlay.addListener(new ClickListener() {
			 @Override
			 public void clicked(InputEvent event, float x, float y){
				 if(timer > 3 && !exitClicked && !settingsClicked){
					 playClicked = true;
				 }
			 }
		 });
		 buttonPlay.pad(10, 40, 10, 40);
		 
		 //settings for exit button
		 buttonExit = new TextButton("Exit", skin);
		 buttonExit.addListener(new ClickListener() {
			 @Override
			 public void clicked(InputEvent event, float x, float y){
				 if(timer > 3.5){
					 exitClicked = true;
					 playClicked = false;
					 settingsClicked = false;
				 }
			 }
		 });
		 buttonExit.pad(10, 40, 10, 40);

		 //resizing cat
		 catImage.setSize(5*catImage.getWidth(), 5*catImage.getHeight());
		 catImage2.setSize(5*catImage.getWidth(), 5*catImage.getHeight());
		 
		 //resizing settings + settings for settings button
		 settingsImage.setSize(.2f*settingsImage.getWidth(), .2f*settingsImage.getHeight());
		 settingsImage.addListener(new ClickListener() {
			 @Override
			 public void clicked(InputEvent event, float x, float y){
				 if(timer > 3.5){
					 settingsClicked = true;
					 playClicked = false;
				 }
			 }
		 });
		 
		 
		 
		 //putting stuff together
		 table.add(catImage).right().width(catImage.getWidth()).height(catImage.getHeight()).padBottom(100).padRight(50).expandY().padTop(100);
		 table.add(heading).padBottom(100).padTop(100);
		 table.add(catImage2).left().width(catImage.getWidth()).height(catImage.getHeight()).padBottom(100).padLeft(50).padTop(100);
		 table.row();
		 table.add();
		 table.add(buttonPlay);
		 table.add();
		 table.getCell(buttonPlay).padBottom(20);
		 table.row();
		 table.add();
		 table.add(buttonExit).padBottom(50);
		 table.add();
		 table.row();

		 table.add(settingsImage).width(settingsImage.getWidth()).height(settingsImage.getHeight()).expandX().colspan(3).right().bottom();
		 
		 stage.addActor(table);
		 
		 //creating animations
		 tweenManager = new TweenManager();
		 Tween.registerAccessor(Actor.class, new ActorAccessor());

		 //cats fade in
		 Timeline.createSequence().beginSequence()
		 	.push(Tween.set(catImage, ActorAccessor.RGB).target(1,0,1))
		 	.push(Tween.to(catImage, ActorAccessor.RGB, 1f).target(0,0,1))
		 	.push(Tween.to(catImage, ActorAccessor.RGB, 1f).target(0,1,1))
		 	.push(Tween.to(catImage, ActorAccessor.RGB, 1f).target(0,1,0))
		 	.push(Tween.to(catImage, ActorAccessor.RGB, 1f).target(1,1,0))
		 	.push(Tween.to(catImage, ActorAccessor.RGB, 1f).target(1,0,0))
		 	.push(Tween.to(catImage, ActorAccessor.RGB, 1f).target(1,0,1))
		 	.end().repeat(Tween.INFINITY, 0).start(tweenManager);
		 
		 Timeline.createSequence().beginSequence()
		 	.push(Tween.set(catImage2, ActorAccessor.RGB).target(1,0,1))
		 	.push(Tween.to(catImage2, ActorAccessor.RGB, 1f).target(0,0,1))
		 	.push(Tween.to(catImage2, ActorAccessor.RGB, 1f).target(0,1,1))
		 	.push(Tween.to(catImage2, ActorAccessor.RGB, 1f).target(0,1,0))
		 	.push(Tween.to(catImage2, ActorAccessor.RGB, 1f).target(1,1,0))
		 	.push(Tween.to(catImage2, ActorAccessor.RGB, 1f).target(1,0,0))
		 	.push(Tween.to(catImage2, ActorAccessor.RGB, 1f).target(1,0,1))
		 	.end().repeat(Tween.INFINITY, 0).start(tweenManager);
		 
		 //buttons fade in
		 Timeline.createSequence().beginSequence()
		 	.push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
		 	.push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
		 	.push(Tween.set(settingsImage, ActorAccessor.ALPHA).target(0))
		 	.push( Timeline.createParallel().beginParallel()
			 	.push(Tween.from(heading, ActorAccessor.ALPHA, 3f).target(0))
			 	.push(Tween.from(catImage, ActorAccessor.ALPHA, 3f).target(0))
			 	.push(Tween.from(catImage2, ActorAccessor.ALPHA, 3f).target(0))
			 	.end())
		 	.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, .5f).target(1))
		 	.push(Tween.to(buttonExit, ActorAccessor.ALPHA, .25f).target(1))
		 	.push(Tween.to(settingsImage, ActorAccessor.ALPHA, .25f).target(1))
		 	.end().start(tweenManager);
		 
		 //table fade-in
		 Tween.from(table, ActorAccessor.ALPHA, .5f).target(0);
		 Tween.from(table, ActorAccessor.Y, 2.5f).target(Gdx.graphics.getHeight()*0.7f).start(tweenManager);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tweenManager.update(delta);
		
		stage.act(delta);
		stage.draw();
		
		timer += delta;
		
		if(playClicked){
			music.setVolume(0.2f/(1+2*fadeTimer*fadeTimer));
			
			if(fadeTimer == 0){
				Timeline.createParallel().beginParallel()
			 	.push(Tween.to(heading, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(catImage, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(catImage2, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(buttonExit, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(settingsImage, ActorAccessor.ALPHA, 2f).target(0))
			 	.end().start(tweenManager);
			}

			fadeTimer += delta;
			if(fadeTimer > 3){
				 ((TimeGame) Gdx.app.getApplicationListener()).setScreen(new MapOne());
				 dispose();
			}
		}
		
		if(exitClicked){
			music.setVolume(0.2f/(1+2*fadeTimer*fadeTimer));
			
			if(fadeTimer == 0){
				Timeline.createParallel().beginParallel()
			 	.push(Tween.to(heading, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(catImage, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(catImage2, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(buttonExit, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(settingsImage, ActorAccessor.ALPHA, 2f).target(0))
			 	.end().start(tweenManager);
			}

			fadeTimer += delta;
			 if(fadeTimer > 2){
				 Gdx.app.exit();
			 }
		}
		
		
		if(settingsClicked){
			music.setVolume(0.2f/(1+2*fadeTimer*fadeTimer));
			
			if(fadeTimer == 0){
				Timeline.createParallel().beginParallel()
			 	.push(Tween.to(heading, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(catImage, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(catImage2, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(buttonExit, ActorAccessor.ALPHA, 2f).target(0))
			 	.push(Tween.to(settingsImage, ActorAccessor.ALPHA, 2f).target(0))
			 	.end().start(tweenManager);
			}

			fadeTimer += delta;
			 if(fadeTimer > 2){
				 ((TimeGame)Gdx.app.getApplicationListener()).setScreen(new SettingsMenu());
				 dispose();
			 }
		}
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		table.setFillParent(true);
		table.invalidateHierarchy();
		table.setSize(width, height);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		atlas.dispose();
		skin.dispose();
		music.dispose();
		catTexture.dispose();
	}

}
