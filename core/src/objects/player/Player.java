package objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.PPM;

public class Player extends GameEntity {
    private Body body;
    private int jumpCount;
    private int maxJumpCount;
    private Assets assets;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> attackAnimation;
    private float jumpAnimationTime;
    private float attackAnimationTime;

    private int numFootContacts;
    private PlayerState playerState;
    private boolean isTurnedRight;

    public Player(float x, float y, float width, float height, GameScreen gameScreen) {
        super(x / PPM, y / PPM, width, height, gameScreen);
        this.assets = gameScreen.getAssets();
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        this.speed = 5.5f;
        this.jumpCount = 0;
        this.maxJumpCount = 2;
        this.isTurnedRight = true;
        this.numFootContacts = 0;
        this.jumpAnimationTime = 0;
        this.attackAnimationTime = 999;

        this.playerState = PlayerState.IDLE;
        createAnimations();
    }

    private void createAnimations() {
        this.idleAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.playerIdleSheet), 64, 64)[0]);
        this.runAnimation = new Animation<>(1 / 8f, TextureRegion.split(assets.manager.get(assets.playerRunSheet), 80, 64)[0]);
        this.jumpAnimation = new Animation<>(1 / 15f, TextureRegion.split(assets.manager.get(assets.playerJumpSheet), 64, 64)[0]);
        this.attackAnimation = new Animation<>(1 / 32f, TextureRegion.split(assets.manager.get(assets.playerAttackSheet), 96, 64)[0]);

    }

    private Body createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);
        //main hitbox
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef).setUserData(this);
        //foot sensor
        shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM - 0.01f, 0.05f, new Vector2(0, -height / 2 / PPM), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");
        //right attack sensor
        shape = new PolygonShape();
        shape.setAsBox(width / PPM / 2 * 1.25f, height / 2 / PPM * 1.35f, new Vector2(width / 2 / PPM - 0.05f, -0.25f), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("rightAttack");
        //left attack sensor
        shape = new PolygonShape();
        shape.setAsBox(width / PPM / 2 * 1.25f, height / 2 / PPM * 1.35f, new Vector2(-width / 2 / PPM + 0.05f, -0.25f), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("leftAttack");

        shape.dispose();
        return body;
    }

    @Override
    public void update() {
        this.x = body.getPosition().x * PPM;
        this.y = body.getPosition().y * PPM;

        checkUserInput();

        jumpAnimationTime += Gdx.graphics.getDeltaTime();
        attackAnimationTime += Gdx.graphics.getDeltaTime();
    }

    private void checkUserInput() {
        playerState = PlayerState.IDLE;
        velocityX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityX = 1;
            isTurnedRight = true;
            if (numFootContacts > 0) {
                playerState = PlayerState.RUNNING;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityX = -1;
            isTurnedRight = false;
            if (numFootContacts > 0) {
                playerState = PlayerState.RUNNING;
            }
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            attackAnimationTime = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && (numFootContacts > 0 || jumpCount < maxJumpCount - 1)) {
            float impulse = body.getMass() * 9;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, impulse), body.getPosition(), true);
            jumpCount++;
            jumpAnimationTime = 0;
        }

        if (numFootContacts > 0) {
            jumpCount = 0;
        } else if (body.getLinearVelocity().y < 0) {
            playerState = PlayerState.FALLING;
        } else {
            playerState = PlayerState.JUMPING;
        }

        if (!attackAnimation.isAnimationFinished(attackAnimationTime)) {
            playerState = PlayerState.ATTACKING;
        }

        body.setLinearVelocity(velocityX * speed, body.getLinearVelocity().y);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
//        batch.draw(texture,
//                getX() - getWidth() / 2,
//                getY() - getHeight() / 2,
//                texture.getWidth() * (getHeight() / texture.getHeight()),
//                getHeight());
        if (playerState == PlayerState.IDLE) {
            batch.draw(idleAnimation.getKeyFrame(gameScreen.getElapsedTime(), true),
                    getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 30),
                    getY() - getHeight() / 2 - 12,
                    (isTurnedRight ? 1 : -1) * (getWidth() + 60),
                    getHeight() + 18);
        } else if (playerState == PlayerState.RUNNING) {
            batch.draw(runAnimation.getKeyFrame(gameScreen.getElapsedTime(), true),
                    getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 48),
                    getY() - getHeight() / 2 - 12,
                    (isTurnedRight ? 1 : -1) * (getWidth() + 80),
                    getHeight() + 19);
        } else if (playerState == PlayerState.JUMPING || playerState == PlayerState.FALLING) {
            batch.draw(jumpAnimation.getKeyFrame(jumpAnimationTime, false),
                    getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 20),
                    getY() - getHeight() / 2 - 11,
                    (isTurnedRight ? 1 : -1) * (getWidth() + 60),
                    getHeight() + 18);
        } else if (playerState == PlayerState.ATTACKING) {
            batch.draw(attackAnimation.getKeyFrame(attackAnimationTime, false),
                    getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 44),
                    getY() - getHeight() / 2 - 15,
                    (isTurnedRight ? 1 : -1) * (getWidth() + 80),
                    getHeight() + 18);
        }
        batch.end();
    }

    public void setMaxJumpCount(int maxJumpCount) {
        this.maxJumpCount = maxJumpCount;
    }

    public Body getBody() {
        return body;
    }

    public int getNumFootContacts() {
        return numFootContacts;
    }

    public void setNumFootContacts(int numFootContacts) {
        this.numFootContacts = numFootContacts;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public enum PlayerState {
        IDLE,
        RUNNING,
        JUMPING,
        FALLING,
        DEAD,
        ATTACKING
    }
}
