package com.ryoma.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ryoma.frontend.objects.Collidable;
import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.objects.patterns.ShootingPattern;
import com.ryoma.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.ryoma.frontend.systems.BulletManager;

public class Boss extends Enemy {
    private ShootingPattern shootingPattern = new SpreadShot(200f, 3, 30f);

    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    public void shootBullet(BulletManager bulletManager) {
        if (shootingPattern != null && active) {
            shootingPattern.execute(x + width / 2f - 8f, y, bulletManager, false);
        }
    }

    public ShootingPattern getShootingPattern() { return shootingPattern; }
    public void setShootingPattern(ShootingPattern shootingPattern) { this.shootingPattern = shootingPattern; }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            if (collisionCooldown <= 0) {
                System.out.println("Player touches boss");
                collisionCooldown = 1.0f;
            }
        }
    }
}
