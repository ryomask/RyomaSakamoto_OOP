package com.ryoma.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ryoma.frontend.objects.GameObject;
import com.ryoma.frontend.objects.bullets.Bullet;
import com.ryoma.frontend.objects.bullets.BulletType;
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.items.ItemType;

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

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage);
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage);
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
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

    // Eksekusi Spell Card (Bomb): Hapus seluruh peluru musuh aktif dan ubah menjadi drop item POINT!
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

    // Pembersihan seluruh peluru pemain saat restart permainan (Copy-Paste):
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
