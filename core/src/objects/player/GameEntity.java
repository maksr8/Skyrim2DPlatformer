package objects.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameEntity {
    protected float x, y, velocityX, velocityY, width, height, speed;

    public GameEntity(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.speed = 0;
    }

    public abstract void update();

    public abstract void render(SpriteBatch batch, Texture texture);


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }
}
