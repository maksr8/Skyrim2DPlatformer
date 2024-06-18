package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.BIT_GROUND;
import static helper.Constants.PPM;

public class FallingPlatform extends GameEntity {
    private static final float RECOVER_DURATION = 20f;
    private Body body;
    private Assets assets;
    private Animation<TextureRegion> fallingAnimation;
    private boolean isFalling;
    private float fallingAnimationTime;


    public FallingPlatform(float x, float y, float width, float height, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen);
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        assets = gameScreen.getAssets();
        fallingAnimation = new Animation<>(1 / 5f, TextureRegion.split(assets.manager.get(assets.fallingPlatform1Sheet), 73, 20)[0]);
    }

    private Body createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PPM, y / PPM);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
        return body;
    }

    @Override
    public void update() {
        if (fallingAnimation.isAnimationFinished(fallingAnimationTime)){
            //gameScreen.removeFallingPlatform(this);
            Filter filter1 = new Filter();
            filter1.maskBits = 0;
            Filter filter2 = new Filter();
            filter2.categoryBits = BIT_GROUND;
            body.getFixtureList().get(0).setFilterData(filter1);
            isFalling = false;
            fallingAnimationTime += Gdx.graphics.getDeltaTime();
            if (fallingAnimationTime > RECOVER_DURATION) {
                body.getFixtureList().get(0).setFilterData(filter2);
                fallingAnimationTime = 0;
            }
        }
        if (isFalling) {
            fallingAnimationTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (isFalling) {
            batch.draw(fallingAnimation.getKeyFrame(fallingAnimationTime, false), x - width / 2, y - height / 2, width, height);
        } else {
            if (!fallingAnimation.isAnimationFinished(fallingAnimationTime)) {
                batch.draw(assets.manager.get(assets.fallingPlatform1), x - width / 2, y - height / 2, width, height);
            }
        }
        batch.end();
    }

    public void setFalling() {
        isFalling = true;
    }

    public Body getBody() {
        return body;
    }
}
