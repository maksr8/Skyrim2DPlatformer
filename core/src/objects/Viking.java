package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.*;

public class Viking extends GameEntity {
    private static final float HIT_ANIMATION_DURATION = 1f;
    private static final float DEAD_ANIMATION_DURATION = 1f;
    private static final float IDLE_ANIMATION_DURATION = 4f;
    private static final float WALK_ANIMATION_DURATION = 4f;
    private static final float ATTACK_DELAY = 0.4f;
    public static final float ATTACK_COOLDOWN = 1.2f;
    private static Assets assets;
    private static Animation<TextureRegion> idleAnimation;
    private static Animation<TextureRegion> walkAnimation;
    private static Animation<TextureRegion> hitAnimation;
    private static Animation<TextureRegion> deadAnimation;
    private static Animation<TextureRegion> attackAnimation;
    private Body body;
    private Animation<TextureRegion> currentAnimation;
    private EntityState vikingState;
    private boolean isTurnedRight;
    private float generalAnimationTimer;
    private float hitAnimationTimer;
    private float deadAnimationTimer;
    private float attackAnimationTimer;
    private float attackTimer;
    private int numOfLeftSensorContacts;
    private int numOfRightSensorContacts;
    private boolean isHitTowardsRight;
    private boolean isTriggered;
    private int hp;

    public Viking(float x, float y, float width, float height, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen);
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        if (assets == null)
            assets = gameScreen.getAssets();
        speed = 1.9f;
        hp = 6;
        vikingState = EntityState.IDLE;
        isTurnedRight = true;
        generalAnimationTimer = 0;
        hitAnimationTimer = 0;
        deadAnimationTimer = 0;
        attackTimer = 999;
        attackAnimationTimer = 0;
        numOfLeftSensorContacts = 0;
        numOfRightSensorContacts = 0;
        if (idleAnimation == null)
            createAnimations();
        currentAnimation = idleAnimation;
    }

    private void createAnimations() {
        idleAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.vikingIdleSheet), 21, 31)[0]);
        walkAnimation = new Animation<>(1 / 8f, TextureRegion.split(assets.manager.get(assets.vikingWalkSheet), 21, 32)[0]);
        hitAnimation = new Animation<>(1 / 8f, TextureRegion.split(assets.manager.get(assets.vikingHitSheet), 26, 30)[0]);
        deadAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.vikingDeadSheet), 40, 30)[0]);
        attackAnimation = new Animation<>(1 / 16f, TextureRegion.split(assets.manager.get(assets.vikingAttackSheet), 67, 34)[0]);
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
        fixtureDef.filter.categoryBits = BIT_ENEMY;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData(this);

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

        // detection for player to attack
        shape.setAsBox(width / PPM / 2 * 5f, height / PPM / 2 * 1.05f, new Vector2(0, 0), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);

        // attack hitbox (fixture 5)
        shape.setAsBox(width / PPM / 2 * 3.2f, height / PPM / 2 * 1.05f, new Vector2(0, 0), 0);
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
        attackTimer += Gdx.graphics.getDeltaTime();
        if (isTriggered && vikingState != EntityState.ATTACKING && vikingState != EntityState.HIT && vikingState != EntityState.DEAD) {
            attack();
        }
        switch (vikingState) {
            case IDLE:
                if (generalAnimationTimer > IDLE_ANIMATION_DURATION) {
                    vikingState = EntityState.WALKING;
                    isTurnedRight = !isTurnedRight;
                }
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                currentAnimation = idleAnimation;
                break;
            case WALKING:
                if (generalAnimationTimer > WALK_ANIMATION_DURATION + IDLE_ANIMATION_DURATION) {
                    vikingState = EntityState.IDLE;
                    generalAnimationTimer = 0;
                }
                currentAnimation = walkAnimation;

                if (isTurnedRight) {
                    if (numOfRightSensorContacts == 0) {
                        isTurnedRight = false;
                    } else {
                        body.setLinearVelocity(speed, body.getLinearVelocity().y);
                    }
                } else if (numOfLeftSensorContacts == 0) {
                    isTurnedRight = true;
                } else {
                    body.setLinearVelocity(-speed, body.getLinearVelocity().y);
                }
                break;
            case HIT:
                if (hitAnimationTimer > HIT_ANIMATION_DURATION) {
                    if (isTriggered) {
                        vikingState = EntityState.ATTACKING;
                        attackAnimationTimer = 0;
                    } else {
                        vikingState = EntityState.IDLE;
                    }
                    hitAnimationTimer = 0;
                }
                currentAnimation = hitAnimation;
                hitAnimationTimer += Gdx.graphics.getDeltaTime();
                body.setLinearVelocity(body.getLinearVelocity().x - body.getLinearVelocity().x * 0.05f, body.getLinearVelocity().y);

                break;
            case DEAD:
                body.getFixtureList().first().setFilterData(new Filter() {{
                    categoryBits = BIT_ENEMY;
                    maskBits = BIT_GROUND;
                }});
                if (deadAnimationTimer > DEAD_ANIMATION_DURATION) {
                    gameScreen.removeViking(this);
                }
                currentAnimation = deadAnimation;
                deadAnimationTimer += Gdx.graphics.getDeltaTime();
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                break;
            case TRIGGERED:
                if (attackTimer > ATTACK_DELAY) {
                    vikingState = EntityState.ATTACKING;
                    attackAnimationTimer = 0;
                    gameScreen.getGame().playSound(assets.manager.get(assets.soundSwordSwing));
                }
                if (isTurnedRight) {
                    body.setLinearVelocity(speed * 0.4f, body.getLinearVelocity().y);
                } else {
                    body.setLinearVelocity(-speed * 0.4f, body.getLinearVelocity().y);
                }
                break;
            case ATTACKING:
                if (attackAnimation.isAnimationFinished(attackAnimationTimer)) {
                    vikingState = EntityState.IDLE;
                    attackAnimationTimer = 0;
                } else {
                    attackAnimationTimer += Gdx.graphics.getDeltaTime();
                }
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (vikingState == EntityState.DEAD) {
            batch.draw(currentAnimation.getKeyFrame(deadAnimationTimer, false),
                    x + (isTurnedRight ? -1 : 1) * (width / 2 + 14),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width + 27), height);
            batch.end();
            return;
        } else if (vikingState == EntityState.ATTACKING) {
            batch.draw(attackAnimation.getKeyFrame(attackAnimationTimer, false),
                    x + (isTurnedRight ? -1 : 1) * (width / 2 + 30),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width + 80), height);
        }else if (vikingState == EntityState.TRIGGERED) {
            batch.draw(attackAnimation.getKeyFrame(0.07f, false),
                    x + (isTurnedRight ? -1 : 1) * (width / 2 + 30),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width + 80), height);
        } else {
            batch.draw(currentAnimation.getKeyFrame(generalAnimationTimer, true),
                    x + (isTurnedRight ? -1 : 1) * (width / 2),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width), height);
        }
        batch.end();
    }

    public void hit(int atk) {
        hp -= atk;
        if (hp <= 0) {
            vikingState = EntityState.DEAD;
            gameScreen.getGame().playSound(assets.manager.get(assets.soundVikingDeath));
            return;
        }
        gameScreen.getGame().playSound(assets.manager.get(assets.soundVikingHit));
        vikingState = EntityState.HIT;
        body.applyLinearImpulse(new Vector2(isHitTowardsRight ? 5f : -5f, 6f), body.getWorldCenter(), true);
    }

    public void attack() {
        if (attackTimer < ATTACK_COOLDOWN) {
            return;
        }
        vikingState = EntityState.TRIGGERED;
        attackTimer = 0;
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        turnTowardsPlayer();
    }

    private void turnTowardsPlayer() {
        isTurnedRight = gameScreen.getPlayer().x > x;
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

    public EntityState getVikingState() {
        return vikingState;
    }

    public void setTriggered(boolean triggered) {
        isTriggered = triggered;
    }
}
