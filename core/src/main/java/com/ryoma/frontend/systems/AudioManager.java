package com.ryoma.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private static AudioManager instance;

    private Music bgm;
    private Sound shootSound;
    private Sound enemyDieSound;
    private Sound itemCollectSound;

    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void init() {
        try {
            bgm = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm.mp3"));
            bgm.setLooping(true);
            bgm.setVolume(0.4f);
            bgm.play();
        } catch (Exception e) {
            System.out.println("BGM gagal dimuat: " + e.getMessage());
        }

        try {
            shootSound = Gdx.audio.newSound(Gdx.files.internal("audio/plst00.wav"));
            enemyDieSound = Gdx.audio.newSound(Gdx.files.internal("audio/pldead00.wav"));
            itemCollectSound = Gdx.audio.newSound(Gdx.files.internal("audio/item00.wav"));
        } catch (Exception e) {
            System.out.println("SFX gagal dimuat: " + e.getMessage());
        }
    }

    public void playShootSound() {
        if (shootSound != null) shootSound.play(0.5f);
    }

    public void playEnemyDieSound() {
        if (enemyDieSound != null) enemyDieSound.play(0.6f);
    }

    public void playItemCollectSound() {
        if (itemCollectSound != null) itemCollectSound.play(0.6f);
    }

    public void dispose() {
        if (bgm != null) {
            bgm.stop();
            bgm.dispose();
        }
        if (shootSound != null) shootSound.dispose();
        if (enemyDieSound != null) enemyDieSound.dispose();
        if (itemCollectSound != null) itemCollectSound.dispose();
    }
}
