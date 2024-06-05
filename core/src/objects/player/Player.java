package objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static helper.Constants.PPM;

public class Player extends GameEntity{
    private int jumpCount;
    private int maxJumpCount;
    private boolean fallen = false;

    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 4f;
        this.jumpCount = 0;
        this.maxJumpCount = 2;
        body.getFixtureList().get(0).setFriction(1f);
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
    public void render(SpriteBatch batch) {

    }

    public void setMaxJumpCount(int maxJumpCount) {
        this.maxJumpCount = maxJumpCount;
    }
}
