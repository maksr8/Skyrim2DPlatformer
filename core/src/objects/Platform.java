package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import helper.Assets;

import static helper.Constants.BIT_GROUND;
import static helper.Constants.PPM;

public class Platform extends GameEntity {
    private Body body;
    private Texture texture;


    public Platform(float x, float y, float width, float height, GameScreen gameScreen, String type) {
        super(x, y, width, height, gameScreen);
        this.body = createBody(this.x, this.y, width, height, gameScreen.getWorld());
        Assets assets = gameScreen.getAssets();
        switch (type) {
            case "Grass":
                texture = assets.manager.get(assets.platformGrass);
                break;
            case "...":
                break;
        }
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
        body.createFixture(fixtureDef);

        shape.dispose();
        return body;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x - width / 2, y - height / 2, width, height);
        batch.end();
    }
}
