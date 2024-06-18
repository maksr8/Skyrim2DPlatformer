package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import helper.Assets;

public class VictoryScreen extends ScreenAdapter {
    private final Skyrim2DGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Assets assets;
    private boolean isSelectingLevel;

    public VictoryScreen(final Skyrim2DGame game) {
        this.game = game;
        this.camera = game.getOrthographicCamera();
        this.batch = game.getBatch();
        this.assets = game.getAssets();
        this.isSelectingLevel = false;
        this.font = new BitmapFont();
        game.setBackgroundMusic(assets.manager.get(assets.musicVictory));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.5f, 0.34f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (isSelectingLevel) {

        } else {
            batch.draw(assets.manager.get(assets.victoryLabel), game.getGameWidth() / 2 - 400, game.getGameHeight() / 2 + 100, 800, 250);
            font.draw(batch, "Tap anywhere to continue", 20, 20);
        }
        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {
        camera.zoom = 1;
        camera.position.set(game.getGameWidth() / 2, game.getGameHeight() / 2, 0);
    }
}
