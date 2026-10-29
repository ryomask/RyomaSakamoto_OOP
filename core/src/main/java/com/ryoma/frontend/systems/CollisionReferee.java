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

        for(Bullet bullet :bulletManager.getActiveEnemyBullets()){
        if (bullet.isDestroyed()) continue;

        if (player.isAlive() && !player.isDestroyed()) {
            if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {
                player.takeDamage(bullet.getDamage());
                bullet.destroy();
            } else if (!bullet.hasBeenGrazed() && bullet.getCoreHitbox()) {
                bullet.setGrazed(true);
                player.addScore(50);
                System.out.println("[CollisionReferee]GRAZE! Reimu Hakurei grazed an enemy bullet (+50pts)");
            }
        }
    }

        for(GameObject entity :entities){
        if (entity.isDestroyed()) continue;
        if (!item.isCollected()) {
            if (item.getCoreHitbox().overlaps(player.getCoreHitbox()) || item.getCoreHitbox().overlaps(player.getGrazeHitboc())) {
                player.collectItem(item);
                item.setCollected(true);
            }
        }
    }
        if(entity instanceofEnemy enemy &&!enemy.isDestroyed()){
        if (player.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
            player.takeDamage(10);
        }
    }
}

