package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen extends ScreenAdapter {
    private final Skyrim2DGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameOverScreen(final Skyrim2DGame game) {
        this.game = game;
        this.camera = game.getOrthographicCamera();
        this.batch = game.getBatch();
        this.font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0f, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Welcome to THE GAME!!! ", 100, 150);
        font.draw(batch, "Tap anywhere to begin!", 100, 100);
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game, 1));
            dispose();
        }
    }

    @Override
    public void show() {
        camera.zoom = 1;
        camera.position.set(0, 0, 0);
    }
}
