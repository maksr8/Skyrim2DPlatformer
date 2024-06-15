package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.PPM;

public class Rat extends GameEntity {
    private static final float HIT_ANIMATION_DURATION = 2f;
    private static final float DEAD_ANIMATION_DURATION = 1f;
    private static final float IDLE_ANIMATION_DURATION = 4f;
    private static final float WALK_ANIMATION_DURATION = 4f;
    private static Assets assets;
    private static Animation<TextureRegion> idleAnimation;
    private static Animation<TextureRegion> walkAnimation;
    private static Animation<TextureRegion> hitAnimation;
    private static Animation<TextureRegion> deadAnimation;
    private Body body;
    private Animation<TextureRegion> currentAnimation;
    private EntityState ratState;
    private boolean isTurnedRight;
    private float generalAnimationTimer;
    private float hitAnimationTimer;
    private float deadAnimationTimer;
    private int numOfLeftSensorContacts;
    private int numOfRightSensorContacts;
    private boolean isHitTowardsRight;
    private float hp;

    public Rat(float x, float y, float width, float height, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen);
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        if (assets == null)
            assets = gameScreen.getAssets();
        speed = 1.5f;
        hp = 5;
        ratState = EntityState.IDLE;
        isTurnedRight = true;
        generalAnimationTimer = 0;
        hitAnimationTimer = 0;
        deadAnimationTimer = 0;
        numOfLeftSensorContacts = 0;
        numOfRightSensorContacts = 0;
        if (idleAnimation == null)
            createAnimations();
        currentAnimation = idleAnimation;
    }

    private void createAnimations() {
        idleAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.ratIdleSheet), 32, 11)[0]);
        walkAnimation = new Animation<>(1 / 8f, TextureRegion.split(assets.manager.get(assets.ratWalkSheet), 32, 11)[0]);
        hitAnimation = new Animation<>(1 / 8f, TextureRegion.split(assets.manager.get(assets.ratHitSheet), 32, 11)[0]);
        deadAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.ratDeadSheet), 32, 11)[0]);
    }

    private Body createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 1f;
        body.createFixture(fixtureDef).setUserData(this);

        Filter filter = new Filter();
        filter.categoryBits = 0x0002; // Category for Rat
        filter.maskBits = 0x0001; // Can collide with everything except other Rats
        body.getFixtureList().get(0).setFilterData(filter);

        // left bottom edge sensor
        shape.setAsBox(0.01f, 0.02f, new Vector2(-width / 2 / PPM, -height / 2 / PPM), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);
        // right bottom edge sensor
        shape.setAsBox(0.01f, 0.02f, new Vector2(width / 2 / PPM, -height / 2 / PPM), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
        return body;
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;
        generalAnimationTimer += Gdx.graphics.getDeltaTime();

        switch (ratState) {
            case IDLE:
                if (generalAnimationTimer > IDLE_ANIMATION_DURATION) {
                    ratState = EntityState.WALKING;
                    isTurnedRight = !isTurnedRight;
                }
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                currentAnimation = idleAnimation;
                break;
            case WALKING:
                if (generalAnimationTimer > WALK_ANIMATION_DURATION + IDLE_ANIMATION_DURATION) {
                    ratState = EntityState.IDLE;
                    generalAnimationTimer = 0;
                }
                currentAnimation = walkAnimation;

                if (isTurnedRight)
                    if (numOfRightSensorContacts == 0) {
                        isTurnedRight = false;
                    } else {
                        body.setLinearVelocity(speed, body.getLinearVelocity().y);
                    }
                else if (numOfLeftSensorContacts == 0) {
                    isTurnedRight = true;
                } else {
                    body.setLinearVelocity(-speed, body.getLinearVelocity().y);
                }
                break;
            case HIT:
                if (hitAnimationTimer > HIT_ANIMATION_DURATION) {
                    ratState = EntityState.IDLE;
                    hitAnimationTimer = 0;
                }
                currentAnimation = hitAnimation;
                hitAnimationTimer += Gdx.graphics.getDeltaTime();
                body.setLinearVelocity(body.getLinearVelocity().x - body.getLinearVelocity().x * 0.05f, body.getLinearVelocity().y);

                break;
            case DEAD:
                if (deadAnimationTimer > DEAD_ANIMATION_DURATION) {
                    gameScreen.removeRat(this);
                }
                currentAnimation = deadAnimation;
                deadAnimationTimer += Gdx.graphics.getDeltaTime();
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (ratState == EntityState.DEAD) {
            batch.draw(currentAnimation.getKeyFrame(deadAnimationTimer, false),
                    x + (isTurnedRight ? -1 : 1) * (width / 2 + 8),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width + 16), height);
            batch.end();
            return;
        } else {
            batch.draw(currentAnimation.getKeyFrame(generalAnimationTimer, true),
                    x + (isTurnedRight ? -1 : 1) * (width / 2 + 8),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width + 16), height);
        }
        batch.end();
    }

    public Body getBody() {
        return body;
    }

    public void addLeftSensorContact() {
        numOfLeftSensorContacts++;
    }

    public void removeLeftSensorContact() {
        numOfLeftSensorContacts--;
    }

    public void addRightSensorContact() {
        numOfRightSensorContacts++;
    }

    public void removeRightSensorContact() {
        numOfRightSensorContacts--;
    }

    public void setHitTowardsRight(boolean hitTowardsRight) {
        isHitTowardsRight = hitTowardsRight;
    }

    public void hit() {
        hp -= gameScreen.getPlayer().getAtk();
        if (hp <= 0) {
            ratState = EntityState.DEAD;
            return;
        }
        ratState = EntityState.HIT;
        body.applyLinearImpulse(new Vector2(isHitTowardsRight ? 5f : -5f, 6f), body.getWorldCenter(), true);
    }
}
