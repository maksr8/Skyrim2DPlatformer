package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import helper.Assets;

public class Skyrim2DGame extends Game {
	private int screenWidth, screenHeight;
	private OrthographicCamera orthographicCamera;
	private Viewport viewport;
	private SpriteBatch batch;
	private Assets assets;

	@Override
	public void create() {
		batch = new SpriteBatch();
		assets = new Assets();
		assets.load();
		assets.manager.finishLoading();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
		viewport = new FitViewport(screenWidth, screenHeight, orthographicCamera);

		
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		assets.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	public float getGameWidth() {
		return orthographicCamera.viewportWidth;
	}

	public float getGameHeight() {
		return orthographicCamera.viewportHeight;
	}

	public OrthographicCamera getOrthographicCamera() {
		return orthographicCamera;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Assets getAssets() {
		return assets;
	}

	public Viewport getViewport() {
		return viewport;
	}
}
