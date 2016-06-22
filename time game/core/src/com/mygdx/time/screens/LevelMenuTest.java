package com.mygdx.time.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.time.TimeGame;

public class LevelMenuTest implements Screen{
	private Stage stage;
	private Table table;
	private TextureAtlas atlas;
	private Skin skin;
	private List<String> list;
	private ScrollPane scrollPane;
	private TextButton play, back;
	
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		atlas = new TextureAtlas("ui/atlas.pack");
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);
		
		table = new Table(skin);
		table.setFillParent(true);
		
		list = new List<String>(skin);
		list.setItems(new String[] {"one", "two", "threeeee3", "and", "so", "on", "a", "b", "c", "d", "e", "f", "g"});
		
		scrollPane = new ScrollPane(list,skin);
		
		skin.getFont("black").getData().setScale(0.5f);
		skin.getFont("black2").getData().setScale(0.25f);
		skin.getFont("white2").getData().setScale(0.25f);
		
		play = new TextButton("PLAY", skin);
		play.pad(15);
		back = new TextButton("BACK", skin, "black2");
		back.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				((TimeGame) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		});
		back.pad(10);
		
		//putting stuff together
		table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		table.add("Select level", "white").expandX().colspan(3).row();
		table.add(scrollPane).uniformX().expandY().left();
		table.add(play).uniformX();
		table.add(back).uniformX().bottom().right();
		
		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
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
		dispose();
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		atlas.dispose();
		skin.dispose();
	}

}
