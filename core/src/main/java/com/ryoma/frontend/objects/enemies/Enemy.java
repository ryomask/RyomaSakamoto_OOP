package com.ryoma.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.ryoma.frontend.objects.EntityShooter;
import com.ryoma.frontend.objects.items.ItemType;

public class Enemy extends EntityShooter {
    protected float collisionCooldown = 0f;
    protected String name;
    protected int hp;
    protected int maxHp;
    protected long scoreValue;

    protected ItemType itemDrop = null;
    protected boolean damageable = true;
    protected boolean collidable = true;
    protected float shotHitboxWidth = 0f;
    protected float shotHitboxHeight = 0f;
    protected String spriteKey;

    public Enemy(float x, float y, float width, float height, Color color, String name, int hp, long scoreValue) {
        super(x, y, width, height, 100f, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (collisionCooldown > 0) {
            collisionCooldown -= delta;
            if (collisionCooldown < 0) collisionCooldown = 0;
        }
    }

    public boolean takeDamage(int damage) {
        if (!damageable) {
            return false;
        }

        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;
        System.out.println(name + " took " + damage + " DMG! HP: " + hp + "/" + maxHp);
        if (hp == 0) {
            System.out.println(name + " was defeated!");
            destroy();
            return true;
        }
        return false;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
    public long getScoreValue() { return scoreValue; }

    public ItemType getItemDrop() { return itemDrop; }
    public void setItemDrop(ItemType itemDrop) { this.itemDrop = itemDrop; }

    public boolean isDamageable() { return damageable; }
    public void setDamageable(boolean damageable) { this.damageable = damageable; }

    public boolean isCollidable() { return collidable; }
    public void setCollidable(boolean collidable) { this.collidable = collidable; }

    public Rectangle getShotHitbox() {
        if (shotHitboxWidth <= 0f || shotHitboxHeight <= 0f) {
            return getCoreHitbox();
        }
        return new Rectangle(x + (width - shotHitboxWidth) / 2f, y + (height - shotHitboxHeight) / 2f,
            shotHitboxWidth, shotHitboxHeight);
    }

    public void setShotHitboxSize(float width, float height) {
        this.shotHitboxWidth = width;
        this.shotHitboxHeight = height;
    }
}
