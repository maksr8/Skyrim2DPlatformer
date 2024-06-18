package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class SplashScreen extends ScreenAdapter {
    private final Skyrim2DGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private float timer;

    public SplashScreen(final Skyrim2DGame game) {
        this.game = game;
        this.camera = game.getOrthographicCamera();
        this.batch = game.getBatch();
        this.font = new BitmapFont();
        this.timer = 0;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 10);

        timer += delta;

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Created by: Maksym Rozumiei, Vlad Semchuk, Ivan Bahriantsev", game.getGameWidth() / 2 - 200, game.getGameHeight() / 2);
        batch.end();

        if (timer > 3) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {
        camera.zoom = 1;
        camera.position.set(game.getGameWidth() / 2, game.getGameHeight() / 2, 0);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
