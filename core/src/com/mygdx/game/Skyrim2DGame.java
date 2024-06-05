package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Skyrim2DGame extends Game {
	private int screenWidth, screenHeight;
	private OrthographicCamera orthographicCamera;
	private SpriteBatch batch;

	public void create() {
		batch = new SpriteBatch();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public OrthographicCamera getOrthographicCamera() {
		return orthographicCamera;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
