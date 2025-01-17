package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MainMenuScreen;
import helper.Assets;

import static helper.Constants.*;

public class Player extends GameEntity {
    private static float attackCooldown = 0.4f;
    private static float hitCooldown = 2f;
    private static float knockbackDuration = 1f;
    private final ObjectSet<GameEntity> entitiesToHitTowardsRight;
    private final ObjectSet<GameEntity> entitiesToHitTowardsLeft;
    private final Array<Fixture> fixturesToBeHitBy;
    private Body body;
    private Vector2 initialPosition;
    private int jumpCount;
    private int maxJumpCount;
    private int hp;
    private int maxHp;
    private int atk;
    private Assets assets;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> hitAnimation;
    private Animation<TextureRegion> deadAnimation;
    private float jumpAnimationTimer;
    private float attackAnimationTimer;
    private float hitAnimationTimer;
    private float deadAnimationTimer;
    private int numFootContacts;
    private EntityState playerState;
    private boolean isTurnedRight;
    private boolean isTouchingEntrance;

    public Player(float x, float y, float width, float height, GameScreen gameScreen) {
        super(x / PPM, y / PPM, width, height, gameScreen);
        this.assets = gameScreen.getAssets();
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        this.initialPosition = new Vector2(x / PPM, y / PPM);
        this.speed = 5.5f;
        this.hp = 5;
        this.maxHp = 5;
        this.atk = 1;
        this.jumpCount = 0;
        this.maxJumpCount = 200;
        this.isTurnedRight = true;
        this.numFootContacts = 0;
        this.jumpAnimationTimer = 0;
        this.attackAnimationTimer = 999;
        this.hitAnimationTimer = 999;
        this.deadAnimationTimer = 999;
        this.entitiesToHitTowardsRight = new ObjectSet<>();
        this.entitiesToHitTowardsLeft = new ObjectSet<>();
        this.fixturesToBeHitBy = new Array<>();

        this.playerState = EntityState.IDLE;
        createAnimations();
    }

    private void createAnimations() {
        this.idleAnimation = new Animation<>(1 / 4f, TextureRegion.split(assets.manager.get(assets.playerIdleSheet), 64, 64)[0]);
        this.runAnimation = new Animation<>(1 / 8f, TextureRegion.split(assets.manager.get(assets.playerRunSheet), 80, 64)[0]);
        this.jumpAnimation = new Animation<>(1 / 15f, TextureRegion.split(assets.manager.get(assets.playerJumpSheet), 64, 64)[0]);
        this.attackAnimation = new Animation<>(1 / 32f, TextureRegion.split(assets.manager.get(assets.playerAttackSheet), 96, 64)[0]);
        this.hitAnimation = new Animation<>(1 / 16f, TextureRegion.split(assets.manager.get(assets.playerKnockBackSheet), 64, 64)[0]);
        this.deadAnimation = new Animation<>(1 / 6f, TextureRegion.split(assets.manager.get(assets.playerDeadSheet), 80, 64)[0]);
    }

    private Body createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        //main fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef).setUserData(this);

        //main hitbox
        shape.setAsBox(width / 2 / PPM - width / 2 / PPM * 0.4f, height / 2 / PPM);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData(this);

        //foot sensor
        shape.setAsBox(width / 2 / PPM - 0.01f, 0.05f, new Vector2(0, -height / 2 / PPM), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");

        //right attack sensor
        fixtureDef.filter.maskBits = BIT_ENEMY;
        shape.setAsBox(width / PPM / 2 * 2f, height / 2 / PPM * 1.6f, new Vector2(width / 2 / PPM - 0.05f, -0.25f), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("rightAttack");

        //left attack sensor
        shape.setAsBox(width / PPM / 2 * 2f, height / 2 / PPM * 1.6f, new Vector2(-width / 2 / PPM + 0.05f, -0.25f), 0);
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

        if (hitAnimationTimer > hitCooldown && playerState != EntityState.DEAD) {
            handleBeingHit();
        }

        if (playerState == EntityState.DEAD) {
            deadAnimationTimer += Gdx.graphics.getDeltaTime();
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            if (deadAnimation.isAnimationFinished(deadAnimationTimer)) {
                gameScreen.getGame().setScreen(new MainMenuScreen(gameScreen.getGame()));
            }
            return;
        }

        if (hitAnimationTimer > knockbackDuration) {
            checkUserInput();
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x - body.getLinearVelocity().x * 0.05f, body.getLinearVelocity().y);
        }
        if ( y < -100 ) {
            hit(true);
            body.setLinearVelocity(0, 0);
            body.setTransform(initialPosition, 0);
        }
        updateAnimationTimers();
    }

    private void updateAnimationTimers() {
        if (playerState == EntityState.JUMPING || playerState == EntityState.FALLING) {
            jumpAnimationTimer += Gdx.graphics.getDeltaTime();
        }
        if (hitAnimationTimer < hitCooldown) {
            hitAnimationTimer += Gdx.graphics.getDeltaTime();
        }
        if (attackAnimationTimer < attackCooldown) {
            attackAnimationTimer += Gdx.graphics.getDeltaTime();
        }
    }

    private void handleBeingHit() {
        if (fixturesToBeHitBy.notEmpty()) {
            removeNullFixtures();
            Fixture fixture = fixturesToBeHitBy.get(0);
            boolean isHitTowardsRight = body.getWorldCenter().x > fixture.getBody().getWorldCenter().x;
            if (fixture.getUserData() instanceof Viking
                    && ((Viking) fixture.getUserData()).getBody().getFixtureList().get(4) == fixture) {
                if(((Viking) fixture.getUserData()).getVikingState() == EntityState.ATTACKING) {
                    hit(isHitTowardsRight);
                }
            } else {
                hit(isHitTowardsRight);
            }
        }
    }

    private void removeNullFixtures() {
        for (Fixture ignored : fixturesToBeHitBy) {
            fixturesToBeHitBy.removeValue(null, true);
        }
    }

    private void checkUserInput() {
        playerState = EntityState.IDLE;
        velocityX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (isTouchingEntrance) {
                gameScreen.loadNextLevel();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityX = 1;
            isTurnedRight = true;
            if (numFootContacts > 0) {
                playerState = EntityState.RUNNING;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityX = -1;
            isTurnedRight = false;
            if (numFootContacts > 0) {
                playerState = EntityState.RUNNING;
            }
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (attackAnimationTimer > attackCooldown) {
                attackAnimationTimer = 0;
                hitEntities();
                gameScreen.getGame().playSound(assets.manager.get(assets.soundSwordSwing));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && (numFootContacts > 0 || jumpCount < maxJumpCount - 1)) {
            float impulse = body.getMass() * 11;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, impulse), body.getPosition(), true);
            jumpCount++;
            jumpAnimationTimer = 0;
        }

        if (numFootContacts > 0) {
            jumpCount = 0;
            //leave idle or running state
        } else if (body.getLinearVelocity().y < 0) {
            playerState = EntityState.FALLING;
        } else {
            playerState = EntityState.JUMPING;
        }
        if (!attackAnimation.isAnimationFinished(attackAnimationTimer)) {
            playerState = EntityState.ATTACKING;
        }
        if (hitAnimationTimer < knockbackDuration) {
            playerState = EntityState.HIT;
        }

        body.setLinearVelocity(velocityX * speed, body.getLinearVelocity().y);
    }

    private void hitEntities() {
        if (isTurnedRight) {
            for (GameEntity entity : entitiesToHitTowardsRight) {
                gameScreen.getGame().playSound(assets.manager.get(assets.soundSwordCut));
                if (entity instanceof Rat) {
                    ((Rat) entity).setHitTowardsRight(true);
                    ((Rat) entity).hit(atk);
                }
                if (entity instanceof Viking) {
                    ((Viking) entity).setHitTowardsRight(true);
                    ((Viking) entity).hit(atk);
                }
                if (entity instanceof Dragon) {
                    ((Dragon) entity).hit(atk);
                }
            }
        } else {
            for (GameEntity entity : entitiesToHitTowardsLeft) {
                gameScreen.getGame().playSound(assets.manager.get(assets.soundSwordCut));
                if (entity instanceof Rat) {
                    ((Rat) entity).setHitTowardsRight(false);
                    ((Rat) entity).hit(atk);
                }
                if (entity instanceof Viking) {
                    ((Viking) entity).setHitTowardsRight(false);
                    ((Viking) entity).hit(atk);
                }
                if (entity instanceof Dragon) {
                    ((Dragon) entity).hit(atk);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (hitAnimationTimer > hitCooldown || playerState == EntityState.DEAD || hitAnimationTimer % 0.2f > 0.1f) {
            if (playerState == EntityState.IDLE) {
                batch.draw(idleAnimation.getKeyFrame(gameScreen.getElapsedTime(), true),
                        getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 30),
                        getY() - getHeight() / 2 - 8,
                        (isTurnedRight ? 1 : -1) * (getWidth() + 60),
                        getHeight() + 15);
            } else if (playerState == EntityState.RUNNING) {
                batch.draw(runAnimation.getKeyFrame(gameScreen.getElapsedTime(), true),
                        getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 48),
                        getY() - getHeight() / 2 - 8,
                        (isTurnedRight ? 1 : -1) * (getWidth() + 80),
                        getHeight() + 16);
            } else if (playerState == EntityState.JUMPING || playerState == EntityState.FALLING) {
                batch.draw(jumpAnimation.getKeyFrame(jumpAnimationTimer, false),
                        getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 20),
                        getY() - getHeight() / 2 - 3,
                        (isTurnedRight ? 1 : -1) * (getWidth() + 60),
                        getHeight() + 17);
            } else if (playerState == EntityState.ATTACKING) {
                batch.draw(attackAnimation.getKeyFrame(attackAnimationTimer, false),
                        getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 44),
                        getY() - getHeight() / 2 - 10,
                        (isTurnedRight ? 1 : -1) * (getWidth() + 80),
                        getHeight() + 15);
            } else if (playerState == EntityState.HIT) {
                batch.draw(hitAnimation.getKeyFrame(hitAnimationTimer, false),
                        getX() + (isTurnedRight ? 1 : -1) * (getWidth() / 2 + 40),
                        getY() - getHeight() / 2 - 1,
                        (isTurnedRight ? -1 : 1) * (getWidth() + 60),
                        getHeight() + 18);
            } else if (playerState == EntityState.DEAD) {
                batch.draw(deadAnimation.getKeyFrame(deadAnimationTimer, false),
                        getX() + (isTurnedRight ? -1 : 1) * (getWidth() / 2 + 30),
                        getY() - getHeight() / 2 - 16,
                        (isTurnedRight ? 1 : -1) * (getWidth() + 60),
                        getHeight() + 18);
            }
        }
        batch.end();
    }

    public void hit(boolean isHitTowardsRight) {
        isTurnedRight = !isHitTowardsRight;
        hp -= 1;
        if (hp <= 0) {
            playerState = EntityState.DEAD;
            deadAnimationTimer = 0;
            gameScreen.getGame().playSound(assets.manager.get(assets.soundPlayerDeath));
        } else {
            gameScreen.getGame().playSound(assets.manager.get(assets.soundPlayerHit));
            playerState = EntityState.HIT;
            hitAnimationTimer = 0;
            body.setLinearVelocity(0, 0);
            body.applyLinearImpulse(new Vector2(isHitTowardsRight ? 5f : -5f, 10f), body.getWorldCenter(), true);
        }
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

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public EntityState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(EntityState playerState) {
        this.playerState = playerState;
    }

    public void addEntityToHitTowardsRight(GameEntity entity) {
        entitiesToHitTowardsRight.add(entity);
    }

    public void removeEntityToHitTowardsRight(GameEntity entity) {
        entitiesToHitTowardsRight.remove(entity);
    }

    public void addEntityToHitTowardsLeft(GameEntity entity) {
        entitiesToHitTowardsLeft.add(entity);
    }

    public void removeEntityToHitTowardsLeft(GameEntity entity) {
        entitiesToHitTowardsLeft.remove(entity);
    }

    public void addFixtureToBeHitBy(Fixture fixture) {
        fixturesToBeHitBy.add(fixture);
    }

    public void removeFixtureToBeHitBy(Fixture fixture) {
        fixturesToBeHitBy.removeValue(fixture, true);
    }

    public void setTouchingEntrance(boolean touchingEntrance) {
        isTouchingEntrance = touchingEntrance;
    }

    public void setInitialPosition(Vector2 initialPosition) {
        this.initialPosition = initialPosition;
    }
}
