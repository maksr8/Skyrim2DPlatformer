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
import com.badlogic.gdx.utils.ScreenUtils;
import helper.Assets;
import helper.TileMapHelper;
import objects.*;

import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {
    private final Skyrim2DGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Texture currBackground;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Hud hud;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    private Assets assets;

    private Player player;
    private ObjectSet<MovingPlatform> movingPlatforms;
    private ObjectSet<Platform> platforms;
    private ObjectSet<FallingPlatform> fallingPlatforms;
    private ObjectSet<FallingPlatform> fallingPlatformsToRemove;
    private ObjectSet<Rat> rats;
    private ObjectSet<Rat> ratsToRemove;
    private ObjectSet<Viking> vikings;
    private ObjectSet<Viking> vikingsToRemove;
    private ObjectSet<Fireball> fireballs;
    private ObjectSet<Fireball> fireballsToRemove;
    private Dragon dragon;
    private DragonPlatform dragonPlatform;

    private float cameraZoom;
    private boolean isBossfightStarted = false;
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

        this.hud = new Hud(this);
        tileMapHelper = new TileMapHelper(this);
        game.setBackgroundMusic(assets.manager.get(assets.musicLevel1));
        loadLevel(level);
    }

    public void loadLevel(int level) {
        elapsedTime = 0;
        cameraZoom = 0.35f;
        world = new World(new Vector2(0, -35f), true);
        world.setContactListener(new GameContactListener(this));
        initObjectSets();
        orthogonalTiledMapRenderer = tileMapHelper.setupMap("maps/map" + level + ".tmx");

        switch (level) {
            case 1:
                game.setBackgroundMusic(assets.manager.get(assets.musicLevel1));
                currBackground = assets.manager.get(assets.background1);
                player = new Player(32, 128, 20, 40, this);
                break;
            case 2:
                game.setBackgroundMusic(assets.manager.get(assets.musicLevel2));
                currBackground = assets.manager.get(assets.background2);
                player = new Player(1240, 128, 20, 40, this);
                break;
            case 3:
                game.setBackgroundMusic(assets.manager.get(assets.musicLevel3));
                isBossfightStarted = false;
                dragon = null;
                currBackground = assets.manager.get(assets.background3);
                player = new Player(40, 106, 20, 40, this);
                //player = new Player(900, 350, 20, 40, this);
                break;
        }
    }

    public void loadNextLevel() {
        if (level < 3) {
            level++;
            loadLevel(level);
        } else {
            game.setScreen(new VictoryScreen(game));
        }
    }

    private void initObjectSets() {
        movingPlatforms = new ObjectSet<>();
        platforms = new ObjectSet<>();
        fallingPlatforms = new ObjectSet<>();
        fallingPlatformsToRemove = new ObjectSet<>();
        rats = new ObjectSet<>();
        ratsToRemove = new ObjectSet<>();
        vikings = new ObjectSet<>();
        vikingsToRemove = new ObjectSet<>();
        fireballs = new ObjectSet<>();
        fireballsToRemove = new ObjectSet<>();
    }

    @Override
    public void render(float delta) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        update();

        batch.begin();
        batch.draw(currBackground, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();

        orthogonalTiledMapRenderer.render();
        for (Platform platform : platforms) {
            platform.render(batch);
        }
        for (MovingPlatform movingPlatform : movingPlatforms) {
            movingPlatform.render(batch);
        }
        for (FallingPlatform fallingPlatform : fallingPlatforms) {
            fallingPlatform.render(batch);
        }
        for (Rat rat : rats) {
            rat.render(batch);
        }
        for (Viking viking : vikings) {
            viking.render(batch);
        }
        if (dragon != null) {
            dragon.render(batch);
        }
        for (Fireball fireball : fireballs) {
            fireball.render(batch);
        }
        player.render(batch);

        //box2DDebugRenderer.render(world, camera.combined.scl(PPM));
        hud.render();
    }

    private void update() {
        removeObjects();
        world.step(1 / 60f, 8, 3);
        cameraUpdate();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        for (MovingPlatform movingPlatform : movingPlatforms) {
            movingPlatform.update();
        }
        for (FallingPlatform fallingPlatform : fallingPlatforms) {
            fallingPlatform.update();
        }
        for (Rat rat : rats) {
            rat.update();
        }
        for (Viking viking : vikings) {
            viking.update();
        }
        for (Fireball fireball : fireballs) {
            fireball.update();
        }
        player.update();
        if (dragon == null && isBossfightStarted) {
            game.setBackgroundMusic(assets.manager.get(assets.musicBoss));
            this.dragon = new Dragon(1090, 330, 80, 55, this);
            cameraZoom = 0.6f;
            player.setInitialPosition(new Vector2(1191 / PPM, 200 / PPM));
        }
        if (dragon != null) {
            dragon.update();
        }
        hud.update();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            loadLevel(3);
        }

    }

    private void removeObjects() {
        removeFallingPlatforms();
        removeRats();
        removeVikings();
        removeFireballs();
    }

    private void removeFireballs() {
        for (Fireball fireball : fireballsToRemove) {
            fireballs.remove(fireball);
            world.destroyBody(fireball.getBody());
        }
        fireballsToRemove.clear();
    }

    private void removeVikings() {
        for (Viking viking : vikingsToRemove) {
            vikings.remove(viking);
            world.destroyBody(viking.getBody());
        }
        vikingsToRemove.clear();
    }

    private void removeRats() {
        for (Rat rat : ratsToRemove) {
            rats.remove(rat);
            world.destroyBody(rat.getBody());
        }
        ratsToRemove.clear();
    }

    private void removeFallingPlatforms() {
        for (FallingPlatform fallingPlatform : fallingPlatformsToRemove) {
            fallingPlatforms.remove(fallingPlatform);
            world.destroyBody(fallingPlatform.getBody());
        }
        fallingPlatformsToRemove.clear();
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

    public Skyrim2DGame getGame() {
        return game;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Dragon getDragon() {
        return dragon;
    }

    public DragonPlatform getDragonPlatform() {
        return dragonPlatform;
    }

    public void setDragonPlatform(DragonPlatform dragonPlatform) {
        this.dragonPlatform = dragonPlatform;
    }

    public void addMovingPlatform(MovingPlatform movingPlatform) {
        movingPlatforms.add(movingPlatform);
    }

    public void addPlatform(Platform platform) {
        platforms.add(platform);
    }

    public void addFallingPlatform(FallingPlatform fallingPlatform) {
        fallingPlatforms.add(fallingPlatform);
    }

    public void removeFallingPlatform(FallingPlatform fallingPlatform) {
        fallingPlatformsToRemove.add(fallingPlatform);
    }

    public void addRat(Rat rat) {
        rats.add(rat);
    }

    public void removeRat(Rat rat) {
        ratsToRemove.add(rat);
    }

    public void addViking(Viking viking) {
        vikings.add(viking);
    }

    public void removeViking(Viking viking) {
        vikingsToRemove.add(viking);
    }

    public void addFireball(Fireball fireball) {
        fireballs.add(fireball);
    }

    public void removeFireball(Fireball fireball) {
        fireballsToRemove.add(fireball);
    }

    public void startBossfight() {
        isBossfightStarted = true;
    }

    public boolean isBossfightStarted() {
        return isBossfightStarted;
    }

    @Override
    public void dispose() {
        orthogonalTiledMapRenderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
    }

    @Override
    public void pause() {
        System.out.println("Game paused");
    }

    @Override
    public void resume() {
        System.out.println("Game resumed");
    }

    @Override
    public void resize(int width, int height) {

    }
}
