package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import helper.Assets;

public class MainMenuScreen extends ScreenAdapter {
    private final Skyrim2DGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Assets assets;
    private Stage stage;
    private TextButton.TextButtonStyle textButtonStyle;

    public MainMenuScreen(final Skyrim2DGame game) {
        this.game = game;
        this.camera = game.getOrthographicCamera();
        this.batch = game.getBatch();
        this.assets = game.getAssets();
        this.font = new BitmapFont();
        game.setBackgroundMusic(assets.manager.get(assets.musicMainMenu));


        stage = new Stage(new ScreenViewport());


        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = createDrawable("backgrounds/button_up.png"); // Make sure you have this texture
        textButtonStyle.down = createDrawable("backgrounds/button_down.png"); // Make sure you have this texture


        TextButton startButton = new TextButton("", textButtonStyle);
        startButton.setSize(200, 60);
        startButton.setPosition((game.getGameWidth() - startButton.getWidth()) / 2, game.getGameHeight() / 2 + 20);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 1));
                dispose();
            }
        });

        // Create Exit button
        TextButton exitButton = new TextButton("", textButtonStyle);
        exitButton.setSize(200, 60);
        exitButton.setPosition((game.getGameWidth() - exitButton.getWidth()) / 2, game.getGameHeight() / 2 - 60);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(startButton);
        stage.addActor(exitButton);


        Gdx.input.setInputProcessor(stage);
    }

    private Drawable createDrawable(String filePath) {
        Texture texture = new Texture(Gdx.files.internal(filePath));
        return new TextureRegionDrawable(texture);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0f, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(assets.manager.get(assets.gameNameLabel), game.getGameWidth() / 2 - 400, game.getGameHeight() / 2 + 100, 800, 250);
        batch.end();


        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void show() {
        camera.zoom = 1;
        camera.position.set(game.getGameWidth() / 2, game.getGameHeight() / 2, 0);
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
