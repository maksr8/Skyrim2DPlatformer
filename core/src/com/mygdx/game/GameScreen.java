package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import helper.Assets;
import helper.TileMapHelper;
import objects.player.Player;

import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {
    private final Skyrim2DGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    private Assets assets;

    private Player player;
    private float cameraZoom = 0.25f;
    
    public GameScreen(final Skyrim2DGame game) {
        this.game = game;
        this.camera = game.getOrthographicCamera();
        this.batch = game.getBatch();
        this.world = new World(new Vector2(0,-25f), true);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.assets = game.getAssets();

        tileMapHelper = new TileMapHelper(this);
        orthogonalTiledMapRenderer = tileMapHelper.setupMap("maps/map3.tmx");
        world.setContactListener(new GameContactListener());
    }
    
    @Override
    public void render(float delta) {
        // blue and alpha component in the range [0,1]
        ScreenUtils.clear(0.4f, 0.6f, 1f, 1f);

        update();
        orthogonalTiledMapRenderer.render();
        player.render(batch, assets.manager.get(assets.player));
        batch.begin();

        batch.end();
        box2DDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    private void update() {
        world.step(1/60f, 8, 3);
        cameraUpdate();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            pause();
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.N)) {
            world = new World(new Vector2(0,-25f), true);
            orthogonalTiledMapRenderer = tileMapHelper.setupMap("maps/map2.tmx");
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void dispose() {
        orthogonalTiledMapRenderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
    }
}
