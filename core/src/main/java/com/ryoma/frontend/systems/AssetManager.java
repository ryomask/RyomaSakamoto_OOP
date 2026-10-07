package com.ryoma.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;

public class AssetManager {
    private static AssetManager instance;

    private final Map<String, TextureRegion> textureRegionMap;
    private final Map<String, Animation<TextureRegion>> animationMap;
    private final Map<String, Texture> textureMap;

    private AssetManager() {
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public Texture loadTexture(String filename) {
        if (textureMap.containsKey(filename)) {
            return textureMap.get(filename);
        }
        if (Gdx.files != null && Gdx.files.internal(filename).exists()) {
            Texture tex = new Texture(Gdx.files.internal(filename));
            textureMap.put(filename, tex);
            return tex;
        }
        return null;
    }

    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    public void registerRegionFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int col) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
            textureRegionMap.put(key, grid[row][col]);
        }
    }

    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration) {
        registerAnimationFromSheet(key, filename, tileWidth, tileHeight, row, 0, numFrames, frameDuration, LOOP);
    }

    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int startCol, int numFrames, float frameDuration, Animation.PlayMode playMode) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
            TextureRegion[] frames = new TextureRegion[numFrames];
            for (int i = 0; i < numFrames; i++) {
                frames[i] = grid[row][startCol + i];
            }
            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);
            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    public TextureRegion getTextureRegion(String key) { return textureRegionMap.get(key); }
    public Animation<TextureRegion> getAnimation(String key) { return animationMap.get(key); }

    public void init() {
        registerAnimationFromSheet("player_idle", "player.png", 32, 48, 0,  8, 0.125f);
        registerAnimationFromSheet("player_left", "player.png", 32, 48, 1,  4, 0.12f);
        registerAnimationFromSheet("player_right", "player.png", 32, 48, 2,  4, 0.12f);
        registerAnimationFromSheet("boss_idle", "rumia.png", 64, 64, 0,  4, 0.2f);
        registerAnimationFromSheet("boss_left", "rumia.png", 64, 64, 1,  4, 0.15f);
        registerAnimationFromSheet("boss_right", "rumia.png", 64, 64, 2,  4, 0.15f);
        registerAnimationFromSheet("fairy_idle_red", "fairy.png", 32, 32, 1,  8, 0.125f);
        registerAnimationFromSheet("fairy_idle_blue", "fairy.png", 32, 32, 0,  8, 0.125f);

        registerAnimationFromSheet("rumia_idle", "rumia.png", 64, 64, 0, 4, 0.2f);
        registerAnimationFromSheet("fairy_idle", "fairy.png", 32, 32, 0, 4, 0.15f);
        registerRegionFromSheet("bullet_danmaku", "bullets_small.png", 16, 16, 1, 2);

        registerRegionFromSheet("item_power", "items.png", 16, 16, 0, 0);
        registerRegionFromSheet("item_point", "items.png", 16, 16, 0, 1);
        registerRegionFromSheet("item_bomb", "items.png", 16, 16, 0, 2);
        registerRegionFromSheet("item_life", "items.png", 16, 16, 0, 3);
    }

    public void dispose() {
        for (Texture tex : textureMap.values()) {
            tex.dispose();
        }
        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }
}
