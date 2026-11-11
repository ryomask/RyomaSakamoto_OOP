package com.ryoma.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ryoma.frontend.objects.Collidable;
import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.ryoma.frontend.objects.patterns.shootingStrategy.FrontShot;

public class Fairy extends Enemy {
    public Fairy(String name, int hp) {
        super(150, 380, 24, 24, Color.PINK, name, hp, 500L);
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 24, 24, Color.PINK, name, hp, 500L);
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
    }

    public Fairy(float x, float y, String name, int hp, float shootDelay, float shootInterval, int maxShots) {
        super(x, y, 24, 24, Color.PINK, name, hp, 500L);
        this.shootDelay = shootDelay;
        this.shootInterval = shootInterval;
        this.maxShots = maxShots;
        // TODO (2 poin): Suntikkan strategi bawaan FrontShot dan LinearEntityMovement
        setShootingPattern(new FrontShot(150f, 10));
        setMovementPattern(new LinearEntityMovement(0f, -40f));
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

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            if (collisionCooldown <= 0) {
                System.out.println("Player touches fairy");
                collisionCooldown = 1.0f;
            }
        }
    }
}
