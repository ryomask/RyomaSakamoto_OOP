package com.ryoma.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.ryoma.frontend.objects.enemies.Enemy;
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.items.ItemType;
import com.ryoma.frontend.objects.bullets.Bullet;
import com.ryoma.frontend.objects.patterns.ShootingPattern;
import com.ryoma.frontend.objects.patterns.shootingStrategy.*;
import com.ryoma.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.ryoma.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.ryoma.frontend.observers.GameObserver;
import com.ryoma.frontend.systems.AssetManager;
import com.ryoma.frontend.systems.BulletManager;
import com.ryoma.frontend.systems.EntityFactory;

import java.util.ArrayList;
import java.util.List;

public class Player extends EntityShooter {
    String name;
    int hp;
    private int lives = 3;
    int power;
    int spellCards;
    private long score;

    private List<GameObserver> observers = new ArrayList<>();

    private int grazeCount = 0;
    private boolean focused = false;
    private float shootCooldown = 5f / 60f;
    private float shootTimer = shootCooldown;
    private float homingCooldown = 30f / 60f;
    private float homingShootTimer = homingCooldown;
    private float invulnerableTimer = 0f;

    private int currentDir = 0;
    private ShootingPattern mainShootingPattern;
    private ShootingPattern sideShootingPattern;

    public Player(String name, int hp, int power, int spellCards) {
        super(200, 50, 32, 48, 240f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 240f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        shootTimer += delta;
        homingShootTimer += delta;

        if (shootTimer > 0.15f) {
            shootTimer = shootCooldown;
            homingShootTimer = homingCooldown;
        }

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;
            if (invulnerableTimer < 0f) invulnerableTimer = 0f;
        }
    }

    public void move(float dx, float dy) {
        // [PERBAIKAN] Menggabungkan operator perkalian kecepatan fokus
        float currentSpeed = focused ? speed * 0.5f : speed;
        this.x += dx * currentSpeed;
        this.y += dy * currentSpeed;
        clampToPlayfield();
    }

    private void clampToPlayfield() {
        float minX = 32;
        float maxX = 416 - width;
        float minY = 16;
        float maxY = 464 - height;

        x = Math.max(minX, Math.min(x, maxX));
        y = Math.max(minY, Math.min(y, maxY));
    }

    public void registerObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyScoreChanged(){
        for (GameObserver obs : observers) obs.onScoreChanged(score);
    }
    private void notifyHpChanged(){
        for (GameObserver obs : observers) obs.onHpChanged(hp);
    }
    private void notifyLivesChanged(){
        for (GameObserver obs : observers) obs.onLivesChanged(lives);
    }
    private void notifySpellCardsChanged(){
        for (GameObserver obs : observers) obs.onSpellCardsChanged(spellCards);
    }
    private void notifyPowerChanged(){
        for (GameObserver obs : observers) obs.onPowerChanged(power);
    }
    private void notifyGrazeChanged(){
        for (GameObserver obs : observers) obs.onGrazeChanged(grazeCount);
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) {
            if (currentDir != -1) {
                currentDir = -1;
                Animation<TextureRegion> anim = assets.getAnimation("player_left");
                if (anim != null) {
                    setAnimation(anim);
                }
            }
        } else if (dx > 0) {
            if (currentDir != 1) {
                currentDir = 1;
                Animation<TextureRegion> anim = assets.getAnimation("player_right");
                if (anim != null) {
                    setAnimation(anim);
                }
            }
        } else {
            if (currentDir != 0) {
                currentDir = 0;
                Animation<TextureRegion> anim = assets.getAnimation("player_idle");
                if (anim != null) {
                    setAnimation(anim);
                }
            }
        }
    }

    public void updatePowerStrategy() {
        float spreadAngle = focused ? 4f : 18f;
        float dualSpreadAngle = focused ? 3f : 12f;
        float sideHOffset = focused ? 8f : 24f;
        float sideVOffset = focused ? 16f : -16f;
        float leftAngle = focused ? 100f : 120f;
        float rightAngle = focused ? 80f : 60f;

        float normalSpeed = 720f;
        float homingSpeed = 500f;

        if (power < 8) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = null;
            homingCooldown = 30f / 60f;
        } else if (power < 16) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 32) {
            mainShootingPattern = new SpreadShot(normalSpeed, 2, dualSpreadAngle, 30, 8f, null, null, null);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 48) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 64) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new SideShot(homingSpeed, 12, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 15f / 60f;
        } else if (power < 80) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 9, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 12, sideHOffset + 4f, sideVOffset, leftAngle + 30f, rightAngle - 30f, true, new HomingBulletMovement(360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        } else if (power < 128) {
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f, true, new HomingBulletMovement(360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f, true, new HomingBulletMovement(360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        } else {
            mainShootingPattern = new SpreadShot(normalSpeed, 4, spreadAngle * 1.2f, 24, 16f, null, null, null);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f, true, new HomingBulletMovement(360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f, true, new HomingBulletMovement(360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 8f, sideVOffset, leftAngle + 30f, rightAngle - 30f, true, new HomingBulletMovement(360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        }

        if (sideShootingPattern != null) {
            setShootingPattern(new CompositeShootingPattern(mainShootingPattern, sideShootingPattern));
        } else {
            setShootingPattern(mainShootingPattern);
        }
    }

    public void collectItem(Item item) {
        if (item.isDestroyed()) return;
        com.ryoma.frontend.systems.AudioManager.getInstance().playItemCollectSound();

        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER:
                    this.power += type.getPowerBonus();
                    updatePowerStrategy();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                    break;
                case POINT:
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POINT item!");
                    break;
                case BOMB:
                    this.spellCards += 1;
                    notifySpellCardsChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                    break;
                case LIFE:
                    this.lives++;
                    this.hp = 100;
                    notifyLivesChanged();
                    notifyHpChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected LIFE item! Remaining Lives: " + lives);
                    break;
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }

        item.destroy();
    }

    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(int damage, BulletManager bulletManager, List<GameObject> entities) {
        if (isInvulnerable() || isDestroyed()) {
            if (isInvulnerable()) {
                System.out.println(name + " is INVULNERABLE! (0 damage taken)");
            }
            return;
        }
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        notifyHpChanged();
        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
            loseLife(bulletManager, entities);
        }
    }

    public void loseLife(BulletManager bulletManager, List<GameObject> entities) {
        this.lives--;
        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (this.lives > 0) {
            System.out.println("[Player] " + name + " lost a life! Remaining Lives: " + this.lives + ". Respawning at (200, 50)...");
            respawn();
        } else {
            System.out.println("[Player] GAME OVER! " + name + " has run out of lives!");
            destroy();
        }
    }

    public void respawn() {
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.hp = 100;
        notifyHpChanged();
        this.invulnerableTimer = 3.0f;
        // [PERBAIKAN] Menggabungkan operator pengurangan power saat respawn
        this.power = Math.max(10, this.power - 16);
        updatePowerStrategy();
        notifyPowerChanged();
        this.spellCards = 3;
        notifySpellCardsChanged();
    }

    public void resetGame() {
        this.lives = 3;
        this.hp = 100;
        this.power = 15;
        this.spellCards = 3;
        this.score = 0;
        this.grazeCount = 0;
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.invulnerableTimer = 3.0f;
        updatePowerStrategy();

        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            notifyScoreChanged();
            System.out.println(getName() + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    public boolean isAlive() {
        return this.hp > 0 && this.lives > 0 && !isDestroyed();
    }

    public boolean isGameOver() {
        return this.lives <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public int getLives() { return lives; }
    public void setLives(int lives) {
        this.lives = lives;
        notifyLivesChanged();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHp() { return hp; }
    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
        notifyHpChanged();
    }
    public int getPower() { return power; }
    public void setPower(int power) {
        this.power = power;
        updatePowerStrategy();
        notifyPowerChanged();
    }
    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }
    public long getScore() { return score; }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(x + (width / 2f) - 4f, y + (height / 2f) - 4f, 8f, 8f);
    }

    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null) return;

        if (shootTimer >= shootCooldown) {
            if (mainShootingPattern != null) {
                // [PERBAIKAN] Menggabungkan operator pengurangan posisi X peluru utama
                mainShootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true);
            } else if (shootingPattern != null) {
                // [PERBAIKAN] Menggabungkan operator pengurangan posisi X peluru fallback
                shootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true);
            } else {
                // [PERBAIKAN] Menggabungkan operator pengurangan posisi X peluru default
                bulletManager.spawnPlayerBullet(x + width / 2f - 8f, y + height, 0f, 400f, 10 + power);
            }
            shootTimer = 0f;
        }

        if (sideShootingPattern != null && homingShootTimer >= homingCooldown) {
            // [PERBAIKAN] Menggabungkan operator pengurangan posisi X peluru samping (side shooting)
            sideShootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true);
            homingShootTimer = 0f;
        }
    }

    public void useBomb(BulletManager bulletManager, List<GameObject> entities) {
        if (spellCards > 0) {
            spellCards--;
            notifySpellCardsChanged();
            invulnerableTimer = 5.0f;
            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities);

                GameObject target = null;
                if (entities != null) {
                    for (GameObject obj : entities) {
                        if (obj instanceof Enemy enemy && !enemy.isDestroyed()) {
                            target = enemy;
                            break;
                        }
                    }
                }

                bulletManager.spawnBombOrbs(x + width / 2f, y + height / 2f, target, entities);
            }
            System.out.println(name + " unleashes SPELL CARD (Fantasy Seal)! All enemy bullets cleared & 8 homing orbs launched!");
        } else {
            System.out.println("No Spell Cards (Bombs) remaining!");
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        this.grazeCount++;
        notifyGrazeChanged();
        addScore(50);
    }

    public ShootingPattern getMainShootingPattern() { return mainShootingPattern; }
    public ShootingPattern getSideShootingPattern() { return sideShootingPattern; }
    public float getShootCooldown() { return shootCooldown; }
    public float getHomingCooldown() { return homingCooldown; }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item) {
            Item item = (Item) other;
            if (!item.isCollected()) {
                System.out.println("Player touches items");
                collectItem(item);
                item.setCollected(true);
            }
        }
    }

    public Bullet shootBullet() {
        int damage = 10 + power;
        System.out.println(name + " shoots bullet dealing " + damage + " DMG!");

        com.ryoma.frontend.systems.AudioManager.getInstance().playShootSound();

        return EntityFactory.createPlayerBullet(this.x + (this.width / 2f) - 8f, this.y + this.height, damage);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {
            if (invulnerableTimer > 0f) {
                // [PERBAIKAN] Menggabungkan operator modulus (sisa bagi) untuk efek kedip invulnerabilitas
                boolean visible = ((int) (invulnerableTimer * 16)) % 2 == 0;
                if (!visible) return;
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f);
            }
            super.render(batch);
            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f);
            }
        }
    }

    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 4f);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 2f);
            shapeRenderer.end();
        }
    }

    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy();
        }
    }
    public int getGrazeCount() { return grazeCount; }
}
