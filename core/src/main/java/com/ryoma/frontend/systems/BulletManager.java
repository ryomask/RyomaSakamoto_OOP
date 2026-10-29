package com.ryoma.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ryoma.frontend.objects.bullets.Bullet;
import com.ryoma.frontend.objects.BulletType;
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

    public BulletManager() {
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();
        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
    }

    // Peminjaman bullet Pemain dari Pool (5 poin)
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        // TODO:
        // 1. Cek apakah playerBulletPool tidak kosong.
        //    Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
        // 2. Re-initialize bullet.
        // 3. Tambahkan bullet ke dalam daftar activePlayerBullets.
        // 4. Kembalikan instance bullet tersebut.
        if (playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.PLAYER_AMULET, damage, true);
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage);
            bullet.init(x, y, vx, vy, BulletType.PLAYER_AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
    }

    // Peminjaman bullet Musuh dari Pool (5 poin)
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        // TODO:
        // 1. Cek apakah enemyBulletPool tidak kosong:
        //    Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
        // 2. Re-initialize bullet.
        // 3. Tambahkan bullet ke dalam daftar activeEnemyBullets
        // 4. Kembalikan instance bullet tersebut
        if (enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.ENEMY_ORB, damage, false);
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage);
            bullet.init(x, y, vx, vy, BulletType.ENEMY_ORB, damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> playerIter = activePlayerBullets.iterator();
        while (playerIter.hasNext()) {
            Bullet bullet = playerIter.next();
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
}
