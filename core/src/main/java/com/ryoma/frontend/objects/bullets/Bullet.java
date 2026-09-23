package com.ryoma.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.ryoma.frontend.objects.BulletType;
import com.ryoma.frontend.objects.Collidable;
import com.ryoma.frontend.objects.GameObject;
import com.ryoma.frontend.objects.enemies.Enemy;

public class Bullet extends GameObject {
    private BulletType bulletType;
    private int damage;

    // Constructor dengan kecepatan default
    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 8, 16, 400f, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    // Constructor overload dengan kecepatan custom
    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y, 8, 16, speed, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        this.y += this.speed * delta; // Bergerak ke atas
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            System.out.println("Bullet hit" + enemy.getName() + "for" + damage + "DMG!");
            enemy.takeDamage(damage);
            destroy();
            // 1. Tampilkan pesan bahwa Bullet mengenai Enemy dalam format:
            //    Bullet hit [EnemyName] for [damage] DMG!

            // 2. Panggil takeDamage() milik Enemy dengan damage milik Bullet ini.

            // 3. Bikin si bullet hancur (destroy) setelah mengenai Enemy, apapun hasilnya
            //    (baik enemy kalah atau masih hidup), krn satu bullet cuma boleh kena satu target.
        }
    }


    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }
}
