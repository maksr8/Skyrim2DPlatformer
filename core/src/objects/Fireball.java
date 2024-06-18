package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.*;

public class Fireball extends GameEntity {
    private static final float BURNING_DURATION = 2f;
    private Body body;
    private Assets assets;
    private float speed = 4f;
    private float destX;
    private float destY;
    private Animation<TextureRegion> fireballAnimation;
    private Animation<TextureRegion> fireAnimation;
    private float generalTimer;
    private boolean isFalling;

    public Fireball(float x, float y, float width, float height, GameScreen gameScreen, float destX, float destY) {
        super(x, y, width, height, gameScreen);
        this.destX = destX;
        this.destY = destY;
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        assets = gameScreen.getAssets();
        this.isFalling = true;
        this.generalTimer = 0;
        fireballAnimation = new Animation<>(1 / 5f, TextureRegion.split(assets.manager.get(assets.fireballSheet), 16, 16)[0]);
        fireAnimation = new Animation<>(1 / 5f, TextureRegion.split(assets.manager.get(assets.fireSheet), 16, 16)[0]);
    }

    private Body createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = BIT_ENEMY;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
        return body;
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        generalTimer += Gdx.graphics.getDeltaTime();
        if (isFalling) {
            float dx = destX - x;
            float dy = destY - y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            float vx = speed * dx / distance;
            float vy = speed * dy / distance;
            body.setLinearVelocity(vx, vy);
            if (distance < 4f) {
                isFalling = false;
                body.setLinearVelocity(0, 0);
                body.setTransform(destX / PPM, destY / PPM, 0);
                generalTimer = 0;
            }
        } else {
            if (generalTimer > BURNING_DURATION) {
                gameScreen.removeFireball(this);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (isFalling) {
            batch.draw(fireballAnimation.getKeyFrame(generalTimer, true), x - width / 2, y - height / 2, width, height);
        } else {
            batch.draw(fireAnimation.getKeyFrame(generalTimer, true), x - width / 2, y - height / 2, width, height);
        }
        batch.end();
    }

    public Body getBody() {
        return body;
    }
}
