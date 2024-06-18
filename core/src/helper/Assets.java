package helper;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    public final AssetDescriptor<Texture> gameNameLabel = new AssetDescriptor<>("hud/game_name_label.png", Texture.class);
    public final AssetDescriptor<Texture> victoryLabel = new AssetDescriptor<>("hud/victory_label.png", Texture.class);
    public final AssetDescriptor<Texture> dragonFlySheet = new AssetDescriptor<>("entities/dragon_fly.png", Texture.class);
    public final AssetDescriptor<Texture> dragonFlyUpSheet = new AssetDescriptor<>("entities/dragon_fly_up.png", Texture.class);
    public final AssetDescriptor<Texture> dragonFlyDownSheet = new AssetDescriptor<>("entities/dragon_fly_down.png", Texture.class);
    public final AssetDescriptor<Texture> dragonHitSheet = new AssetDescriptor<>("entities/dragon_hit.png", Texture.class);
    public final AssetDescriptor<Texture> dragonIdle = new AssetDescriptor<>("entities/dragon_idle.png", Texture.class);
    public final AssetDescriptor<Texture> dragonDeadSheet = new AssetDescriptor<>("entities/dragon_dead.png", Texture.class);
    public final AssetDescriptor<Texture> bossBorder = new AssetDescriptor<>("hud/boss_border.png", Texture.class);
    public final AssetDescriptor<Texture> bossHP = new AssetDescriptor<>("hud/boss_hp.png", Texture.class);
    public final AssetDescriptor<Texture> fireballSheet = new AssetDescriptor<>("entities/fireball.png", Texture.class);
    public final AssetDescriptor<Texture> fireSheet = new AssetDescriptor<>("entities/fire.png", Texture.class);
    public final AssetDescriptor<Music> musicLevel1 = new AssetDescriptor<>("music/the_jerall_mountains.mp3", Music.class);
    public final AssetDescriptor<Music> musicLevel2 = new AssetDescriptor<>("music/ancient_stones.mp3", Music.class);
    public final AssetDescriptor<Music> musicLevel3 = new AssetDescriptor<>("music/silent_footsteps.mp3", Music.class);
    public final AssetDescriptor<Music> musicMainMenu = new AssetDescriptor<>("music/dragonborn.mp3", Music.class);
    public final AssetDescriptor<Music> musicGaveOver = new AssetDescriptor<>("music/secunda.mp3", Music.class);
    public final AssetDescriptor<Music> musicVictory = new AssetDescriptor<>("music/around_the_fire.mp3", Music.class);
    public final AssetDescriptor<Music> musicBoss = new AssetDescriptor<>("music/death_or_sovngarde.mp3", Music.class);
    public final AssetDescriptor<Sound> soundSwordSwing = new AssetDescriptor<>("sound/sword_attack.wav", Sound.class);
    public final AssetDescriptor<Sound> soundSwordCut = new AssetDescriptor<>("sound/sword_cutting.wav", Sound.class);
    public final AssetDescriptor<Sound> soundVikingDeath = new AssetDescriptor<>("sound/viking_death.mp3", Sound.class);
    public final AssetDescriptor<Sound> soundVikingHit = new AssetDescriptor<>("sound/viking_hit.mp3", Sound.class);
    public final AssetDescriptor<Sound> soundPlayerDeath = new AssetDescriptor<>("sound/oof.mp3", Sound.class);
    public final AssetDescriptor<Sound> soundDragonDeath = new AssetDescriptor<>("sound/dragon_death.mp3", Sound.class);
    public final AssetDescriptor<Sound> soundPlayerHit = new AssetDescriptor<>("sound/player_hit.mp3", Sound.class);

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
        manager.load(gameNameLabel);
        manager.load(victoryLabel);
        manager.load(dragonFlySheet);
        manager.load(dragonFlyUpSheet);
        manager.load(dragonFlyDownSheet);
        manager.load(dragonHitSheet);
        manager.load(dragonIdle);
        manager.load(dragonDeadSheet);
        manager.load(bossBorder);
        manager.load(bossHP);
        manager.load(fireballSheet);
        manager.load(fireSheet);
        manager.load(musicLevel1);
        manager.load(musicLevel2);
        manager.load(musicLevel3);
        manager.load(musicMainMenu);
        manager.load(musicGaveOver);
        manager.load(musicVictory);
        manager.load(musicBoss);
        manager.load(soundSwordSwing);
        manager.load(soundSwordCut);
        manager.load(soundVikingDeath);
        manager.load(soundVikingHit);
        manager.load(soundPlayerDeath);
        manager.load(soundDragonDeath);
        manager.load(soundPlayerHit);
    }

    public void dispose() {
        manager.dispose();
    }
}
