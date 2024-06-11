package helper;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public final AssetManager manager = new AssetManager();

    //public final AssetDescriptor<Texture> playerIdle = new AssetDescriptor<>("entities/Idle.png", Texture.class);
    public final AssetDescriptor<Texture> playerIdleSheet = new AssetDescriptor<>("entities/Idle-Sheet.png", Texture.class);
    public final AssetDescriptor<Texture> playerRunSheet = new AssetDescriptor<>("entities/Run-Sheet.png", Texture.class);
    public final AssetDescriptor<Texture> playerJumpSheet = new AssetDescriptor<>("entities/Jump-All-Sheet.png", Texture.class);
    public final AssetDescriptor<Texture> playerAttackSheet = new AssetDescriptor<>("entities/Attack-01-Sheet.png", Texture.class);
    public final AssetDescriptor<Texture> background1 = new AssetDescriptor<>("backgrounds/background1.png", Texture.class);
    public final AssetDescriptor<Texture> background2 = new AssetDescriptor<>("backgrounds/background2.png", Texture.class);
    public final AssetDescriptor<Texture> background3 = new AssetDescriptor<>("backgrounds/background3.png", Texture.class);
    public final AssetDescriptor<Texture> movingPlatform1 = new AssetDescriptor<>("objects/moving_platform1.png", Texture.class);
    public final AssetDescriptor<Texture> platformGrass = new AssetDescriptor<>("objects/platformGrass.png", Texture.class);
    public final AssetDescriptor<Texture> fallingPlatform1 = new AssetDescriptor<>("objects/falling_platform1.png", Texture.class);
    public final AssetDescriptor<Texture> fallingPlatform1Sheet = new AssetDescriptor<>("objects/falling_platform1_sheet.png", Texture.class);
    public final AssetDescriptor<Texture> ratIdleSheet = new AssetDescriptor<>("entities/rat_idle.png", Texture.class);
    public final AssetDescriptor<Texture> ratWalkSheet = new AssetDescriptor<>("entities/rat_walk.png", Texture.class);
    public final AssetDescriptor<Texture> ratHitSheet = new AssetDescriptor<>("entities/rat_hit.png", Texture.class);
    public final AssetDescriptor<Texture> ratDeadSheet = new AssetDescriptor<>("entities/rat_dead.png", Texture.class);

    public void load() {
        //manager.load(playerIdle);
        manager.load(playerIdleSheet);
        manager.load(playerRunSheet);
        manager.load(playerJumpSheet);
        manager.load(playerAttackSheet);
        manager.load(background1);
        manager.load(background2);
        manager.load(background3);
        manager.load(movingPlatform1);
        manager.load(platformGrass);
        manager.load(fallingPlatform1);
        manager.load(fallingPlatform1Sheet);
        manager.load(ratIdleSheet);
        manager.load(ratWalkSheet);
        manager.load(ratHitSheet);
        manager.load(ratDeadSheet);
    }

    public void dispose() {
        manager.dispose();
    }
}
