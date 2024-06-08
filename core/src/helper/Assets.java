package helper;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public final AssetManager manager = new AssetManager();

    public final AssetDescriptor<Texture> playerIdle = new AssetDescriptor<>("entities/Idle.png", Texture.class);
    public final AssetDescriptor<Texture> playerIdleSheet = new AssetDescriptor<>("entities/Idle-Sheet.png", Texture.class);


    public void load() {
        manager.load(playerIdle);
        manager.load(playerIdleSheet);
    }

    public void dispose() {
        manager.dispose();
    }
}
