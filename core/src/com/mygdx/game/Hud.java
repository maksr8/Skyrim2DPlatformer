package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import helper.Assets;
import objects.EntityState;

public class Hud {
    private Stage stage;
    private OrthographicCamera camera;
    private GameScreen gameScreen;
    private Skyrim2DGame game;
    private Viewport viewport;
    private SpriteBatch batch;
    private Assets assets;
    private Label timeLabel;

    public Hud(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.game = gameScreen.getGame();
        this.batch = game.getBatch();
        this.assets = game.getAssets();
        this.camera = new OrthographicCamera(game.getGameWidth(), game.getGameHeight());
        //camera.setToOrtho(false);
        this.viewport = new StretchViewport(game.getGameWidth(), game.getGameHeight(), camera);
        this.stage = new Stage(viewport, batch);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        timeLabel = new Label("1", labelStyle);
        timeLabel.setPosition(0, 0);
        stage.addActor(timeLabel);
    }

    public void update() {
        timeLabel.setText(String.format("Time: %06d", (int) gameScreen.getElapsedTime()));
    }

    public void render() {
        //stage.act();
        stage.draw();
        batch.begin();
        renderHearts();
        if (gameScreen.getPlayer().getPlayerState() == EntityState.HIT) {
            batch.draw(assets.manager.get(assets.redScreen), 0, 0, game.getGameWidth(), game.getGameHeight());
        }
        batch.end();
    }

    private void renderHearts() {
        for (int i = 0; i < gameScreen.getPlayer().getMaxHp(); i++) {
            batch.draw(assets.manager.get(assets.heartBorder), 10 + i * 60, game.getGameHeight() - 60, 50, 50);
        }
        for (int i = 0; i < gameScreen.getPlayer().getHp(); i++) {
            batch.draw(assets.manager.get(assets.heart), 10 + i * 60, game.getGameHeight() - 60, 50, 50);
        }
    }

    public void dispose() {
        stage.dispose();
    }
}
