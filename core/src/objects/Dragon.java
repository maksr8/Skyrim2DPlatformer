package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.*;
import static helper.Constants.BIT_GROUND;

public class Dragon extends GameEntity{
    private static final float HIT_ANIMATION_DURATION = 0.15f;
    private static final float DEAD_ANIMATION_DURATION = 1f;
    private static final float FLYING_PHASE_DURATION = 15f;
    private static final float STANDING_PHASE_DURATION = 4.6f;
    private static final float FLY_UP_ANIMATION_DURATION = 6f;
    private static final float FLY_DOWN_ANIMATION_DURATION = 2f;
    private static final float ATTACK_COOLDOWN = 1f;
    private static Assets assets;
    private static Texture idleTexture;
    private static Animation<TextureRegion> flyAnimation;
    private static Animation<TextureRegion> hitAnimation;
    private static Animation<TextureRegion> deadAnimation;
    private static Animation<TextureRegion> flyUpAnimation;
    private static Animation<TextureRegion> flyDownAnimation;
    private Body body;
    private Animation<TextureRegion> currentAnimation;
    private EntityState dragonState;
    private boolean isTurnedRight;
    private float generalAnimationTimer;
    private float hitAnimationTimer;
    private float deadAnimationTimer;
    private float attackTimer;
    private int numOfLeftSensorContacts;
    private int numOfRightSensorContacts;
    private boolean isInCenter;
    private int hp;

    public Dragon(float x, float y, float width, float height, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen);
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        if (assets == null)
            assets = gameScreen.getAssets();
        speed = 1.5f;
        hp = 30;
        dragonState = EntityState.FLYING;
        isTurnedRight = true;
        generalAnimationTimer = 0;
        hitAnimationTimer = 0;
        deadAnimationTimer = 0;
        attackTimer = 0;
        numOfLeftSensorContacts = 0;
        numOfRightSensorContacts = 0;
        if (idleTexture == null)
            createAnimations();
        currentAnimation = flyAnimation;
    }

    private void createAnimations() {
        flyAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.dragonFlySheet), 191, 124)[0]);
        flyUpAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.dragonFlyUpSheet), 191, 131)[0]);
        flyDownAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.dragonFlyDownSheet), 191, 118)[0]);
        hitAnimation = new Animation<>(1 / 7f, TextureRegion.split(assets.manager.get(assets.dragonHitSheet), 191, 83)[0]);
        deadAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.dragonDeadSheet), 191, 83)[0]);
        idleTexture = assets.manager.get(assets.dragonIdle);
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
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_PLAYER | BIT_DRAGON_PLATFORM;
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

        shape.dispose();
        return body;
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;
        generalAnimationTimer += Gdx.graphics.getDeltaTime();

        switch (dragonState) {
            case IDLE:
                if (generalAnimationTimer > STANDING_PHASE_DURATION) {
                    dragonState = EntityState.FLYING_UP;
                    generalAnimationTimer = 0;
                }
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                break;
            case FLYING:
                attackTimer += Gdx.graphics.getDeltaTime();
                attack();
                if (generalAnimationTimer > FLYING_PHASE_DURATION && isInCenter) {
                    dragonState = EntityState.FLYING_DOWN;
                    generalAnimationTimer = 0;
                }
                currentAnimation = flyAnimation;

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
                    dragonState = EntityState.IDLE;
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
                    System.out.println("Dragon removed");
                }
                currentAnimation = deadAnimation;
                deadAnimationTimer += Gdx.graphics.getDeltaTime();
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                break;
            case FLYING_UP:
                if (generalAnimationTimer > FLY_UP_ANIMATION_DURATION) {
                    gameScreen.getDragonPlatform().getBody().getFixtureList().get(0).setSensor(false);
                    dragonState = EntityState.FLYING;
                    generalAnimationTimer = 0;
                }
                currentAnimation = flyUpAnimation;
                body.setLinearVelocity(body.getLinearVelocity().x, speed);
                break;
            case FLYING_DOWN:
                gameScreen.getDragonPlatform().getBody().getFixtureList().get(0).setSensor(true);
                if (generalAnimationTimer > FLY_DOWN_ANIMATION_DURATION) {
                    dragonState = EntityState.IDLE;
                    generalAnimationTimer = 0;
                }
                currentAnimation = flyDownAnimation;
                body.setLinearVelocity(body.getLinearVelocity().x, -speed);
                break;
        }
    }

    private void attack() {
        if (attackTimer < ATTACK_COOLDOWN) {
            return;
        }
        gameScreen.addFireball(new Fireball(x, y, 16, 16, gameScreen, gameScreen.getPlayer().getX(), gameScreen.getPlayer().getY()));
        attackTimer = 0;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (dragonState == EntityState.DEAD) {
            batch.draw(currentAnimation.getKeyFrame(deadAnimationTimer, false),
                    x + (isTurnedRight ? -1 : 1) * (width / 2 + 8),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width + 16), height);
            batch.end();
            return;
        } else if (dragonState == EntityState.IDLE){
            batch.draw(idleTexture, x + (isTurnedRight ? -1 : 1) * (width / 2 + 16),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width + 32), height);
        } else {
            batch.draw(currentAnimation.getKeyFrame(generalAnimationTimer, true),
                    x + (isTurnedRight ? -1 : 1) * (width / 2 + 16),
                    y - height / 2, (isTurnedRight ? 1 : -1) * (width + 32), height);
        }
        batch.end();
    }

    public Body getBody() {
        return body;
    }

    public int getHp() {
        return hp;
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

    public void setInCenter(boolean inCenter) {
        isInCenter = inCenter;
    }

    public void hit(int atk) {
        if (dragonState != EntityState.IDLE) {
            return;
        }
        hp -= atk;
        if (hp <= 0) {
            dragonState = EntityState.DEAD;
            return;
        }
        dragonState = EntityState.HIT;
    }
}
