package com.ryoma.frontend.systems;

import com.ryoma.frontend.objects.GameObject;
import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.objects.bullets.Bullet;
import com.ryoma.frontend.objects.enemies.Enemy;
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.items.ItemType;
import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {

    public void resolveCollisions(Player player, List<GameObject> entities, BulletManager bulletManager) {
        if (player == null || entities == null || bulletManager == null) return;

        List<GameObject> newDrops = new ArrayList<>();

        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {
            if (bullet.isDestroyed()) continue;

            for (GameObject entity : entities) {
                if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {
                    if (bullet.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                        boolean defeated = enemy.takeDamage(bullet.getDamage());
                        if (defeated) {
                            player.addScore(enemy.getScoreValue());
                            Item dropPower = EntityFactory.createPowerItem(enemy.getX(), enemy.getY());
                            newDrops.add(dropPower);
                        }
                        bullet.destroy();
                        break;
                    }
                }
            }
        }

        // 2. Mediator checks Enemy Bullets vs Player (Core Hitbox vs Graze Hitbox)
        for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {
            if (bullet.isDestroyed()) continue;

            if (player.isAlive() && !player.isDestroyed()) {
                // Priority A: Direct Core Hitbox Collision (Damage & Life Loss)
                if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {
                    // TODO 1: Bungkus isi blok ini dengan pengecekan `if (!player.isInvulnerable())`,
                    if (!player.isInvulnerable()) {
                        System.out.println("[CollisionReferee] CORE HIT! Enemy Bullet hit " + player.getName() + " for " + bullet.getDamage() + " DMG!");
                        // TODO 2: Ganti `player.takeDamage(bullet.getDamage())` menjadi `player.takeDamage(100, bulletManager, entities)`
                        player.takeDamage(100, bulletManager, entities);
                        bullet.destroy(); // Mark bullet for recycling by BulletManager
                        // TODO 3: Tambahkan `break` di akhir!
                        break;
                    }
                }
                // Priority B: Graze Hitbox Collision (Passes close to player without hitting core)
                else if (!bullet.hasBeenGrazed() && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())) {
                    bullet.setGrazed(true);
                    // TODO 4: Ganti player.addScore(50) menjadi pemanggilan metode addGraze() milik player
                    player.addGraze();
                    System.out.println("[CollisionReferee] GRAZE! " + player.getName() + " grazed an enemy bullet (+50 pts)!");
                }
            }
        }

        // 3. Mediator checks Player vs Items and Enemies (Core vs Graze)
        for (GameObject entity : entities) {
            if (entity.isDestroyed()) continue;

            if (entity instanceof Item item) {
                if (!item.isCollected()) {
                    if (item.getCoreHitbox().overlaps(player.getCoreHitbox()) || item.getCoreHitbox().overlaps(player.getGrazeHitbox())) {
                        player.collectItem(item);
                        item.setCollected(true);
                    }
                }
            }

            if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {
                if (player.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                    // TODO 5: Bungkus isi blok ini dengan `if (!player.isInvulnerable())`
                    if (!player.isInvulnerable()) {
                        System.out.println("[CollisionReferee] CORE COLLISION! Player collided with " + enemy.getName() + "!");
                        // TODO 6: ganti `player.takeDamage(10)` menjadi `player.takeDamage(100, bulletManager, entities)`
                        player.takeDamage(100, bulletManager, entities);
                    }
                }
            }
        }

        entities.addAll(newDrops);
    }
}
