package objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static helper.Constants.PPM;

public class Player extends GameEntity{
    private int jumpCount;
    private int maxJumpCount;
    private boolean fallen = false;
    protected Body body;

    public Player(float x, float y, float width, float height, World world) {
        super(x / PPM, y /PPM, width, height);
        this.body = createBody(this.x, this.y, width, height, world);
        this.speed = 4f;
        this.jumpCount = 0;
        this.maxJumpCount = 2;
    }

    private Body createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef);

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && jumpCount < maxJumpCount) {
            float impulse = body.getMass() * 9;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, impulse), body.getPosition(), true);
            jumpCount++;
            fallen = false;
        }
        if (body.getLinearVelocity().y < 0) {
            fallen = true;
        }
        if (body.getLinearVelocity().y == 0 && fallen) {
            jumpCount = 0;
        }

        body.setLinearVelocity(velocityX * speed, body.getLinearVelocity().y);
    }

    @Override
    public void render(SpriteBatch batch, Texture texture) {
        batch.begin();
        batch.draw(texture,
                getX() - getWidth() / 2,
                getY() - getHeight() / 2,
                getWidth(), getHeight());
        batch.end();
    }

    public void setMaxJumpCount(int maxJumpCount) {
        this.maxJumpCount = maxJumpCount;
    }

    public Body getBody() {
        return body;
    }
}
