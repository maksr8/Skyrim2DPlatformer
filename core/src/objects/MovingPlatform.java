package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.BIT_GROUND;
import static helper.Constants.PPM;

public class MovingPlatform extends GameEntity {
    private Body body;
    private Assets assets;

    private float destinationX;
    private float destinationY;
    private float initialX;
    private float initialY;
    private boolean isInitMovingRight;
    private boolean isInitMovingUp;
    private float speedY;
    private float speedX;

    public MovingPlatform(float x, float y, float width, float height, GameScreen gameScreen, float destinationX, float destinationY) {
        super(x / PPM, y / PPM, width, height, gameScreen);
        this.destinationX = destinationX + width / 2;
        this.destinationY = destinationY - height / 2;
        this.initialX = x;
        this.initialY = y;
        this.assets = gameScreen.getAssets();
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        this.speedY = 1.5f;
        this.speedX = Math.abs(this.destinationX - initialX) / (Math.abs(this.destinationY - initialY) / speedY);
        setInitialVelocity();
    }

    private void setInitialVelocity() {
        if (initialX < destinationX) {
            body.setLinearVelocity(speedX, 0);
            isInitMovingRight = true;
        } else if (initialX > destinationX) {
            body.setLinearVelocity(-speedX, 0);
            isInitMovingRight = false;
        }

        if (initialY < destinationY) {
            body.setLinearVelocity(body.getLinearVelocity().x, speedY);
            isInitMovingUp = false;
        } else if (initialY > destinationY) {
            body.setLinearVelocity(body.getLinearVelocity().x, -speedY);
            isInitMovingUp = true;
        }
    }

    private Body createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        body.createFixture(fixtureDef);

        shape.dispose();
        return body;
    }

    @Override
    public void update() {
        this.x = body.getPosition().x * PPM;
        this.y = body.getPosition().y * PPM;

        float tolerance = 0.01f;

        if (isInitMovingRight && x > destinationX - tolerance || !isInitMovingRight && x < destinationX + tolerance) {
            body.setLinearVelocity(-body.getLinearVelocity().x, body.getLinearVelocity().y);
        } else if (isInitMovingRight && x < initialX + tolerance || !isInitMovingRight && x > initialX - tolerance) {
            body.setLinearVelocity(-body.getLinearVelocity().x, body.getLinearVelocity().y);
        }
        if (isInitMovingUp && y < destinationY + tolerance || !isInitMovingUp && y > destinationY - tolerance) {
            body.setLinearVelocity(body.getLinearVelocity().x, -body.getLinearVelocity().y);
        } else if (isInitMovingUp && y > initialY - tolerance || !isInitMovingUp && y < initialY + tolerance) {
            body.setLinearVelocity(body.getLinearVelocity().x, -body.getLinearVelocity().y);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(assets.manager.get(assets.movingPlatform1), x - width / 2, y - height / 2, width, height);
        batch.end();
    }
}
