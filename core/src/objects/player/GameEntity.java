package objects.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameScreen;

public abstract class GameEntity {
    protected float x, y, velocityX, velocityY, width, height, speed;
    protected GameScreen gameScreen;

    public GameEntity(float x, float y, float width, float height, GameScreen gameScreen) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.gameScreen = gameScreen;
        this.velocityX = 0;
        this.velocityY = 0;
        this.speed = 0;
    }

    public abstract void update();

    public abstract void render(SpriteBatch batch);


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
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
