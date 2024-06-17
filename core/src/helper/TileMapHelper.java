package helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameScreen;
import objects.*;

import static helper.Constants.PPM;

public class TileMapHelper {
    private TiledMap tiledMap;
    private GameScreen gameScreen;

    public TileMapHelper(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public OrthogonalTiledMapRenderer setupMap(String pathToMap) {
        tiledMap = new TmxMapLoader().load(pathToMap);
        parseMapObjects(tiledMap.getLayers().get("objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void parseMapObjects(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof PolygonMapObject) {
                createStaticBody((PolygonMapObject) mapObject);
            }

            if (mapObject instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                String rectangleName = mapObject.getName();
                if (rectangleName != null) {
                    if (rectangleName.equals("movingPlatform")) {
                        float destinationY = mapObject.getProperties().get("destinationY", Float.class);
                        // Invert the y-coordinate
                        destinationY = (gameScreen.getGame().getGameHeight() - destinationY);
                        gameScreen.addMovingPlatform(new MovingPlatform(rectangle.x + rectangle.width / 2,
                                rectangle.y + rectangle.height / 2,
                                rectangle.width,
                                rectangle.height,
                                gameScreen,
                                mapObject.getProperties().get("destinationX", Float.class),
                                destinationY));
                    } else if (rectangleName.matches("platform.*")) {
                        gameScreen.addPlatform(new Platform(rectangle.x + rectangle.width / 2,
                                rectangle.y + rectangle.height / 2,
                                rectangle.width,
                                rectangle.height,
                                gameScreen,
                                rectangleName.substring(8)));
                    } else if (rectangleName.equals("fallingPlatform")) {
                        gameScreen.addFallingPlatform(new FallingPlatform(rectangle.x + rectangle.width / 2,
                                rectangle.y + rectangle.height / 2,
                                rectangle.width,
                                rectangle.height,
                                gameScreen));
                    } else if (rectangleName.equals("rat")) {
                        gameScreen.addRat(new Rat(rectangle.x + rectangle.width / 2,
                                rectangle.y + rectangle.height / 2,
                                rectangle.width,
                                rectangle.height,
                                gameScreen));
                    } else if (rectangleName.equals("viking")) {
                        gameScreen.addViking(new Viking(rectangle.x + rectangle.width / 2,
                                rectangle.y + rectangle.height / 2,
                                rectangle.width,
                                rectangle.height,
                                gameScreen));
                    } else if (rectangleName.equals("entrance")) {
                        new Entrance(rectangle.x + rectangle.width / 2,
                                rectangle.y + rectangle.height / 2,
                                rectangle.width,
                                rectangle.height,
                                gameScreen);
                    }
                }
            }
        }
    }

    private void createStaticBody(PolygonMapObject polygonMapObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        Shape shape = createPolygonShape(polygonMapObject);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = Constants.BIT_GROUND;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++) {
            Vector2 current = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
            worldVertices[i] = current;
        }

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(worldVertices);
        return polygonShape;
    }
}
