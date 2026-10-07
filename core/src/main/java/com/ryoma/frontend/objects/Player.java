package com.ryoma.frontend.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ryoma.frontend.objects.enemies.Enemy;
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.items.ItemType;
import com.ryoma.frontend.objects.bullets.Bullet;
import com.ryoma.frontend.objects.BulletType;
import com.ryoma.frontend.systems.AssetManager;

public class Player extends GameObject {
    String name;
    int hp;
    int power;
    int spellCards;
    private long score;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 200, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 200, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float dx = 0;
        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                this.y += speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                this.y -= speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.x -= speed * delta;
                dx = -1f;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                this.x += speed * delta;
                dx = 1f;
            }
        }
        updateAnimationState(dx);
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        boolean defeated = target.takeDamage(damage);
        if (defeated) {
            addScore(target.getScoreValue());
        }
    }

    public void collectItem(Item item) {
            if (item.isDestroyed()) return;

        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER:
                    this.power += type.getPowerBonus();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                    break;
                case POINT:
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POINT item!");
                    break;
                case BOMB:
                    this.spellCards += 1;
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                    break;
                case LIFE:
                    this.hp += 20;
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected LIFE item! HP: " + hp);
                    break;
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }

        item.destroy();
    }

    public void takeDamage(int damage) {
        setHp(getHp() - damage);
        System.out.println(getName() + " took " + damage + " damage! Remaining HP: " + getHp());
        if (getHp() == 0) {
            System.out.println(getName() + " was defeated (Pichuun~)! ");
        }
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            System.out.println(getName() + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    public boolean isAlive() {
        return getHp() > 0;
    }

    // Encapsulation getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
    }

    public long getScore() {
        return score;
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan -1.
            // 2. Ubah currentDir menjadi -1.
            // 3. Ambil animasi "player_left" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
            if (currentDir != -1) {
                currentDir = -1;
                Animation<TextureRegion> anim = assets.getAnimation( "player_left");
                if (anim != null){
                    setAnimation(anim);
                }
            }

        } else if (dx > 0) {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan 1.
            // 2. Ubah currentDir menjadi 1.
            // 3. Ambil animasi "player_right" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
            if (currentDir != 1) {
                currentDir = 1;
                Animation<TextureRegion> anim = assets.getAnimation( "player_right");
                if (anim != null){
                    setAnimation(anim);
                }
            }

        } else {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan 0.
            // 2. Ubah currentDir menjadi 0.
            // 3. Ambil animasi "player_idle" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
            if (currentDir != 0) {
                currentDir = 0;
                Animation<TextureRegion> anim = assets.getAnimation( "player_idle");
                if (anim != null){
                    setAnimation(anim);
                }
            }

        }
    }


    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item) {
            Item item = (Item) other;
            if (!item.isCollected()) {
                System.out.println("Player touches items");
                collectItem(item);
                item.setCollected(true); // Tandai item sudah diambil
            }
        }
    }

    public Bullet shootBullet() {
        int damage = 10 + power;
        System.out.println(name + " shoots bullet dealing " + damage + " DMG!");
        return new Bullet(this.x + (this.width / 2f) - 4f, this.y + this.height, BulletType.AMULET, damage);
    }
}
