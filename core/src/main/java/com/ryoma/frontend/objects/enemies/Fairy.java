package com.ryoma.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ryoma.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.ryoma.frontend.objects.patterns.entityStrategy.LinearEntityMovement;

public class Fairy extends Enemy {
    private float shootTimer = 0f;
    private float shootDelay = 0.67f; // ZUN EoSD tick 40: 0.67s entry delay before first shot
    private float shootInterval = 1.4f; // Subsequent shot interval
    private int maxShots = 2; // Stream fairies fire 1-2 shots before exiting
    private int shotsFired = 0;

    public Fairy(String name, int hp) {
        super(150, 380, 24, 24, Color.PINK, name, hp, 500L);
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
        setShotHitboxSize(28f, 28f);
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 24, 24, Color.PINK, name, hp, 500L);
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
        setShotHitboxSize(28f, 28f);
    }

    public Fairy(float x, float y, String name, int hp, float shootDelay, float shootInterval, int maxShots) {
        super(x, y, 24, 24, Color.PINK, name, hp, 500L);
        this.shootDelay = shootDelay;
        this.shootInterval = shootInterval;
        this.maxShots = maxShots;
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
        setShotHitboxSize(28f, 28f);
    }

    public Fairy(float x, float y, String name, int hp, String defaultSpriteKey) {
        super(x, y, 32f, 32f, Color.PURPLE, name, hp, 100L);
        this.spriteKey = defaultSpriteKey;
        setShotHitboxSize(28f, 28f);
    }

    public boolean canShoot(float delta) {
        shootTimer += delta;
        if (shotsFired == 0) {
            if (shootTimer >= shootDelay) {
                shotsFired++;
                shootTimer = 0f;
                return true;
            }
        } else if (shotsFired < maxShots) {
            if (shootTimer >= shootInterval) {
                shotsFired++;
                shootTimer = 0f;
                return true;
            }
        }
        return false;
    }

    public void setShootTiming(float shootDelay, float shootInterval, int maxShots) {
        this.shootDelay = shootDelay;
        this.shootInterval = shootInterval;
        this.maxShots = maxShots;
    }

    public int getShotsFired() { return shotsFired; }
    public float getShootTimer() { return shootTimer; }
    public float getShootDelay() { return shootDelay; }
    public float getShootInterval() { return shootInterval; }
    public int getMaxShots() { return maxShots; }
}
