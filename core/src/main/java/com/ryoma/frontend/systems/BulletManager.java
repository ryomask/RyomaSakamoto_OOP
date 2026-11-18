package com.ryoma.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ryoma.frontend.objects.GameObject;
import com.ryoma.frontend.objects.bullets.Bullet;
import com.ryoma.frontend.objects.bullets.BulletType;
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.items.ItemType;
import com.ryoma.frontend.objects.patterns.shootingStrategy.RingShot;
import com.ryoma.frontend.objects.patterns.bulletStrategy.FantasySealMovement;
import com.ryoma.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BulletManager {
    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;
    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;
    private List<GameObject> entities; // [TAMBAHAN] Tambahkan field untuk menampung daftar entitas arena

    public BulletManager() {
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();
        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage, String spriteKey) {
        String key = (spriteKey != null) ? spriteKey : "bullet_amulet";
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage, key);
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage, String spriteKey) {
        String key = (spriteKey != null) ? spriteKey : "bullet_danmaku";
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage, key);
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        return spawnEnemyBullet(x, y, vx, vy, damage, "bullet_danmaku");
    }

    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy, List<GameObject> entities) {
        int orbCount = 8;
        int orbDamage = 35;

        List<GameObject> targetEntities = (entities != null) ? entities : this.entities;
        // TODO (2 poin): Terapkan Strategy Composition — buat objek RingShot dengan bulletSpeed 150f, jumlah peluru orbCount, damage orbDamage, dan strategi pergerakan peluru berupa FantasySealMovement(targetEnemy, targetEntities), lalu panggil execute(originX, originY, this, true)
        RingShot ringShot = new RingShot(150f, orbCount, orbDamage, new FantasySealMovement(targetEnemy, targetEntities));
        ringShot.execute(originX, originY, this, true);

        System.out.println("[BulletManager] Spawned 8 Bomb Spirit Orbs via RingShot composed with FantasySealMovement!");
    }

    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy) {
        spawnBombOrbs(originX, originY, targetEnemy, null);
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> playerIter = activePlayerBullets.iterator();
        while (playerIter.hasNext()) {
            Bullet bullet = playerIter.next();

            if (entities != null && bullet.getMovementPattern() instanceof HomingBulletMovement homing && homing.getEntities() == null) {
                homing.setEntities(entities);
            }

            bullet.update(delta);

            boolean outOfBounds = bullet.getX() < 0 || bullet.getX() > screenWidth ||
                bullet.getY() < 0 || bullet.getY() > screenHeight;

            if (outOfBounds || bullet.isDestroyed()) {
                bullet.destroy();
                playerIter.remove();
                playerBulletPool.offer(bullet);
            }
        }
        Iterator<Bullet> enemyIter = activeEnemyBullets.iterator();
        while (enemyIter.hasNext()) {
            Bullet bullet = enemyIter.next();
            bullet.update(delta);

            boolean outOfBounds = bullet.getX() < 0 || bullet.getX() > screenWidth ||
                bullet.getY() < 0 || bullet.getY() > screenHeight;

            if (outOfBounds || bullet.isDestroyed()) {
                bullet.destroy();
                enemyIter.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
    }

    public List<Bullet> getActivePlayerBullets() { return activePlayerBullets; }
    public List<Bullet> getActiveEnemyBullets() { return activeEnemyBullets; }
    public int getPlayerPoolSize() { return playerBulletPool.size(); }
    public int getEnemyPoolSize() { return enemyBulletPool.size(); }

    public List<GameObject> getEntities() { return entities; }
    public void setEntities(List<GameObject> entities) { this.entities = entities; }

    public void clearEnemyBullets(List<GameObject> entities) {
        Iterator<Bullet> eIter = activeEnemyBullets.iterator();
        while (eIter.hasNext()) {
            Bullet bullet = eIter.next();
            if (entities != null) {
                Item pointDrop = EntityFactory.createItem(bullet.getX(), bullet.getY(), ItemType.POINT);
                entities.add(pointDrop);
            }
            bullet.destroy();
            eIter.remove();
            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    public void clearPlayerBullets() {
        Iterator<Bullet> pIter = activePlayerBullets.iterator();
        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();
            bullet.destroy();
            pIter.remove();
            playerBulletPool.offer(bullet);
        }
    }
}
