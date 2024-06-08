package objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.PPM;

public class Player extends GameEntity {
    protected Body body;
    private int jumpCount;
    private int maxJumpCount;
    private Assets assets;
    private TextureRegion[] idleFrames;
    private Animation<TextureRegion> idleAnimation;

    private int numFootContacts = 0;
    private PlayerState playerState;

    public enum PlayerState {
        IDLE,
        WALKING,
        JUMPING,
        FALLING,
        DEAD,
        ATTACKING
    }

    public Player(float x, float y, float width, float height, World world, GameScreen gameScreen) {
        super(x / PPM, y / PPM, width, height, gameScreen);
        this.assets = gameScreen.getAssets();
        this.body = createBody(this.x, this.y, width, height, world);
        this.speed = 5.5f;
        this.jumpCount = 0;
        this.maxJumpCount = 1;
        createAnimations();
    }

    private void createAnimations() {
        this.idleFrames = TextureRegion.split(assets.manager.get(assets.playerIdleSheet), 64, 64)[0];
        this.idleAnimation = new Animation<>(1 / 4f, idleFrames);
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
        body.createFixture(fixtureDef);
        //foot sensor
        shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM - 0.01f, 0.05f, new Vector2(0, -height / 2 / PPM), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");

        shape.dispose();
        return body;
    }


    @Override
    public void update() {
        this.x = body.getPosition().x * PPM;
        this.y = body.getPosition().y * PPM;

        checkUserInput();
    }

    private void checkUserInput() {
        velocityX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityX = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityX = -1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && ( numFootContacts > 0 || jumpCount < maxJumpCount-1)) {
            float impulse = body.getMass() * 9;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, impulse), body.getPosition(), true);
            jumpCount++;
        }

        if (numFootContacts > 0) {
            jumpCount = 0;
        }

        body.setLinearVelocity(velocityX * speed, body.getLinearVelocity().y);
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.begin();
//        batch.draw(texture,
//                getX() - getWidth() / 2,
//                getY() - getHeight() / 2,
//                texture.getWidth() * (getHeight() / texture.getHeight()),
//                getHeight());
        batch.draw(idleAnimation.getKeyFrame(gameScreen.getElapsedTime(), true),
                getX() - getWidth() / 2 - 30,
                getY() - getHeight() / 2 - 11,
                getWidth() + 60,
                getHeight() + 18);
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
}
