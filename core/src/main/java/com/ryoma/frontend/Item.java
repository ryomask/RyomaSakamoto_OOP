package com.ryoma.frontend;

import com.badlogic.gdx.graphics.Color;

// Soal 8
public class Item extends GameObject {
    private String itemType;
    private long scoreValue;

    // Soal 8a
    public Item(float x, float y, String itemType) {
        super(x, y, 16f, 16f, 100f, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    // Soal 8b
    public Item(float x, float y, float width, float height, float speed, String itemType) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    // Soal 8c
    public Item(float x, float y, float width, float height, float speed, String itemType, long scoreValue) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = scoreValue;
    }

    // Soal 9: Override Method update
    /* Method update() perlu dioverride di class Item karena Item merupakan
     * entitas pasif dalam game yang memiliki efek jatuh/turun secara otomatis
     * seiring berjalannya waktu (game loop).
     *
     * Beda dg Player yang pergerakannya dikendalikan oleh input pengguna
     * atau Enemy yang bergerak berdasarkan pola khusus, Item bergerak turun
     * secara konstan menggunakan nilai speed-nya.
     */
    @Override
    public void update(float delta) {
        this.y -= this.speed * delta;
    }

    public long getScoreValue() {
        return scoreValue;
    }
}
