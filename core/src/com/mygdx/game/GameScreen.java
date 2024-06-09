package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectSet;
import helper.Assets;
import helper.TileMapHelper;
import objects.player.MovingPlatform;
import objects.player.Player;

import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {
    private final Skyrim2DGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Texture currBackground;
    private Box2DDebugRenderer box2DDebugRenderer;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    private Assets assets;

    private Player player;
    private ObjectSet<MovingPlatform> movingPlatforms;

    private float cameraZoom = 0.35f;
    private boolean paused = false;
    private float elapsedTime = 0;
    private int level;

    public GameScreen(final Skyrim2DGame game, int level) {
        this.game = game;
        this.level = level;
        this.camera = game.getOrthographicCamera();
        this.batch = game.getBatch();
        this.world = new World(new Vector2(0, -35f), true);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.assets = game.getAssets();

        tileMapHelper = new TileMapHelper(this);
        loadLevel(level);
    }

    private void loadLevel(int level) {
        switch (level) {
            case 1:
                currBackground = assets.manager.get(assets.background1);
                break;
            case 2:
                currBackground = assets.manager.get(assets.background2);
                break;
            case 3:
                currBackground = assets.manager.get(assets.background3);
                break;
            default:
                currBackground = assets.manager.get(assets.background1);
                break;
        }
        elapsedTime = 0;
        world = new World(new Vector2(0, -35f), true);
        world.setContactListener(new GameContactListener(this));
        movingPlatforms = new ObjectSet<>();
        orthogonalTiledMapRenderer = tileMapHelper.setupMap("maps/map" + level + ".tmx");
        loadPlayer();
    }

    private void loadPlayer() {
        if (player != null) {
            // save player stats

            ///
        }
        player = new Player(32,128, 32, 64, this);
        // apply saved stats

        ///
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            elapsedTime += Gdx.graphics.getDeltaTime();
        }

        //ScreenUtils.clear(0.4f, 0.6f, 1f, 1f);
        batch.begin();
        batch.draw(currBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        update();
        orthogonalTiledMapRenderer.render();
        for (MovingPlatform movingPlatform : movingPlatforms) {
            movingPlatform.render(batch);
        }
        player.render(batch);

        box2DDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    private void update() {
        world.step(1 / 60f, 8, 3);
        cameraUpdate();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();
        for (MovingPlatform movingPlatform : movingPlatforms) {
            movingPlatform.update();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            pause();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            loadLevel(1);
        }

    }


    private void cameraUpdate() {
        Vector3 position = new Vector3();
        position.x = Math.round(player.getBody().getPosition().x * PPM * 10) / 10f;
        position.y = Math.round(player.getBody().getPosition().y * PPM * 10) / 10f;

        if (position.x < camera.viewportWidth * cameraZoom / 2) {
            position.x = camera.viewportWidth * cameraZoom / 2;
        }
        if (position.y < camera.viewportHeight * cameraZoom / 2) {
            position.y = camera.viewportHeight * cameraZoom / 2;
        }
        if (position.x > camera.viewportWidth - camera.viewportWidth * cameraZoom / 2) {
            position.x = camera.viewportWidth - camera.viewportWidth * cameraZoom / 2;
        }
        if (position.y > camera.viewportHeight - camera.viewportHeight * cameraZoom / 2) {
            position.y = camera.viewportHeight - camera.viewportHeight * cameraZoom / 2;
        }

        camera.position.set(position);
        camera.zoom = cameraZoom;
        camera.update();
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public Assets getAssets() {
        return assets;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void addMovingPlatform(MovingPlatform movingPlatform) {
        movingPlatforms.add(movingPlatform);
    }

    @Override
    public void dispose() {
        orthogonalTiledMapRenderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
    }

    @Override
    public void pause() {
        paused = true;
        System.out.println("Game paused");
    }

    @Override
    public void resume() {
        paused = false;
        System.out.println("Game resumed");
    }
}
