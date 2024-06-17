package helper;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public final AssetManager manager = new AssetManager();

    public final AssetDescriptor<Texture> playerIdleSheet = new AssetDescriptor<>("entities/player_idle.png", Texture.class);
    public final AssetDescriptor<Texture> playerRunSheet = new AssetDescriptor<>("entities/player_run.png", Texture.class);
    public final AssetDescriptor<Texture> playerJumpSheet = new AssetDescriptor<>("entities/player_jump.png", Texture.class);
    public final AssetDescriptor<Texture> playerAttackSheet = new AssetDescriptor<>("entities/player_attack.png", Texture.class);
    public final AssetDescriptor<Texture> playerKnockBackSheet = new AssetDescriptor<>("entities/player_knockback.png", Texture.class);
    public final AssetDescriptor<Texture> playerDeadSheet = new AssetDescriptor<>("entities/player_dead.png", Texture.class);
    public final AssetDescriptor<Texture> background1 = new AssetDescriptor<>("backgrounds/background1.png", Texture.class);
    public final AssetDescriptor<Texture> background2 = new AssetDescriptor<>("backgrounds/background2.png", Texture.class);
    public final AssetDescriptor<Texture> background3 = new AssetDescriptor<>("backgrounds/background3.png", Texture.class);
    public final AssetDescriptor<Texture> movingPlatform1 = new AssetDescriptor<>("objects/moving_platform1.png", Texture.class);
    public final AssetDescriptor<Texture> platformGrass = new AssetDescriptor<>("objects/platformGrass.png", Texture.class);
    public final AssetDescriptor<Texture> platformSnow = new AssetDescriptor<>("objects/platformSnow.png", Texture.class);
    public final AssetDescriptor<Texture> platformNull = new AssetDescriptor<>("objects/platformNull.png", Texture.class);
    public final AssetDescriptor<Texture> fallingPlatform1 = new AssetDescriptor<>("objects/falling_platform1.png", Texture.class);
    public final AssetDescriptor<Texture> fallingPlatform1Sheet = new AssetDescriptor<>("objects/falling_platform1_sheet.png", Texture.class);
    public final AssetDescriptor<Texture> ratIdleSheet = new AssetDescriptor<>("entities/rat_idle.png", Texture.class);
    public final AssetDescriptor<Texture> ratWalkSheet = new AssetDescriptor<>("entities/rat_walk.png", Texture.class);
    public final AssetDescriptor<Texture> ratHitSheet = new AssetDescriptor<>("entities/rat_hit.png", Texture.class);
    public final AssetDescriptor<Texture> ratDeadSheet = new AssetDescriptor<>("entities/rat_dead.png", Texture.class);
    public final AssetDescriptor<Texture> redScreen = new AssetDescriptor<>("backgrounds/red_screen.png", Texture.class);
    public final AssetDescriptor<Texture> heart = new AssetDescriptor<>("hud/heart.png", Texture.class);
    public final AssetDescriptor<Texture> heartBorder = new AssetDescriptor<>("hud/heart_border.png", Texture.class);
    public final AssetDescriptor<Texture> vikingIdleSheet = new AssetDescriptor<>("entities/viking_idle.png", Texture.class);
    public final AssetDescriptor<Texture> vikingWalkSheet = new AssetDescriptor<>("entities/viking_walk.png", Texture.class);
    public final AssetDescriptor<Texture> vikingHitSheet = new AssetDescriptor<>("entities/viking_hit.png", Texture.class);
    public final AssetDescriptor<Texture> vikingDeadSheet = new AssetDescriptor<>("entities/viking_dead.png", Texture.class);
    public final AssetDescriptor<Texture> vikingAttackSheet = new AssetDescriptor<>("entities/viking_attack.png", Texture.class);

    public void load() {
        manager.load(playerIdleSheet);
        manager.load(playerRunSheet);
        manager.load(playerJumpSheet);
        manager.load(playerAttackSheet);
        manager.load(background1);
        manager.load(background2);
        manager.load(background3);
        manager.load(movingPlatform1);
        manager.load(platformGrass);
        manager.load(platformSnow);
        manager.load(platformNull);
        manager.load(fallingPlatform1);
        manager.load(fallingPlatform1Sheet);
        manager.load(ratIdleSheet);
        manager.load(ratWalkSheet);
        manager.load(ratHitSheet);
        manager.load(ratDeadSheet);
        manager.load(playerKnockBackSheet);
        manager.load(playerDeadSheet);
        manager.load(redScreen);
        manager.load(heart);
        manager.load(heartBorder);
        manager.load(vikingIdleSheet);
        manager.load(vikingWalkSheet);
        manager.load(vikingHitSheet);
        manager.load(vikingDeadSheet);
        manager.load(vikingAttackSheet);
    }

    public void dispose() {
        manager.dispose();
    }
}
