package helper;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public final AssetManager manager = new AssetManager();

    public final AssetDescriptor<Texture> player = new AssetDescriptor<>("entities/Idle-Sheet.png", Texture.class);

    public void load() {
        manager.load(player);
    }

    public void dispose() {
        manager.dispose();
    }
}
