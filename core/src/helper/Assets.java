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
    public final AssetDescriptor<Texture> background1 = new AssetDescriptor<>("backgrounds/background1.png", Texture.class);
    public final AssetDescriptor<Texture> background2 = new AssetDescriptor<>("backgrounds/background2.png", Texture.class);
    public final AssetDescriptor<Texture> background3 = new AssetDescriptor<>("backgrounds/background3.png", Texture.class);
    public final AssetDescriptor<Texture> movingPlatform1 = new AssetDescriptor<>("objects/moving_platform1.png", Texture.class);

    public void load() {
        manager.load(playerIdle);
        manager.load(playerIdleSheet);
        manager.load(playerRunSheet);
        manager.load(playerJumpSheet);
        manager.load(playerAttackSheet);
        manager.load(background1);
        manager.load(background2);
        manager.load(background3);
        manager.load(movingPlatform1);
    }

    public void dispose() {
        manager.dispose();
    }
}
