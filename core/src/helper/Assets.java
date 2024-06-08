package helper;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public final AssetManager manager = new AssetManager();

    public final AssetDescriptor<Texture> playerIdle = new AssetDescriptor<>("entities/Idle.png", Texture.class);
    public final AssetDescriptor<Texture> playerIdleSheet = new AssetDescriptor<>("entities/Idle-Sheet.png", Texture.class);
    public final AssetDescriptor<Texture> playerRunSheet = new AssetDescriptor<>("entities/Run-Sheet.png", Texture.class);
    public final AssetDescriptor<Texture> playerJumpSheet = new AssetDescriptor<>("entities/Jump-All-Sheet.png", Texture.class);
    public final AssetDescriptor<Texture> playerAttackSheet = new AssetDescriptor<>("entities/Attack-01-Sheet.png", Texture.class);

    public void load() {
        manager.load(playerIdle);
        manager.load(playerIdleSheet);
        manager.load(playerRunSheet);
        manager.load(playerJumpSheet);
        manager.load(playerAttackSheet);
    }

    public void dispose() {
        manager.dispose();
    }
}
