package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import helper.Assets;

public class MainMenuScreen extends ScreenAdapter {
    private final Skyrim2DGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Assets assets;
    private boolean isSelectingLevel;

    public MainMenuScreen(final Skyrim2DGame game) {
        this.game = game;
        this.camera = game.getOrthographicCamera();
        this.batch = game.getBatch();
        this.assets = game.getAssets();
        this.isSelectingLevel = false;
        this.font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0f, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (isSelectingLevel) {
            
        } else {
            batch.draw(assets.manager.get(assets.gameNameLabel), game.getGameWidth() / 2 - 400, game.getGameHeight() / 2 + 100, 800, 250);
            font.draw(batch, "Tap anywhere to begin!", 20, 20);
        }
        batch.end();

        handleInput();
    }

    private void handleInput() {

    }

    @Override
    public void show() {
        camera.zoom = 1;
        camera.position.set(game.getGameWidth() / 2, game.getGameHeight() / 2, 0);
    }
}
