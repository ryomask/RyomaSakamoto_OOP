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
    private int defeatCounter = 0;
    private int itemDropCount = 0;

    public void resolveCollisions(Player player, List<GameObject> entities, BulletManager bulletManager) {
        if (player == null || entities == null || bulletManager == null) return;

        List<GameObject> newDrops = new ArrayList<>();

        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {
            if (bullet.isDestroyed()) continue;

            for (GameObject entity : entities) {
                if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {
                    if (bullet.getShotHitbox().overlaps(enemy.getShotHitbox())) {
                        System.out.println("[CollisionReferee] Player Bullet hit " + enemy.getName() + " for " + bullet.getDamage() + " DMG!");

                        if (enemy.isDamageable()) {
                            boolean defeated = enemy.takeDamage(bullet.getDamage());

                            if (defeated) {
                                player.addScore(enemy.getScoreValue());

                                ItemType dropType = null;

                                if (enemy.getItemDrop() != null) {
                                    dropType = enemy.getItemDrop();
                                } else if (defeatCounter++ % 3 == 0) {
                                    dropType = (itemDropCount++ % 2 == 0)
                                        ? ItemType.POWER
                                        : ItemType.POINT;
                                }

                                if (dropType != null) {
                                    Item drop = EntityFactory.createItem(
                                        enemy.getX(),
                                        enemy.getY(),
                                        dropType
                                    );

                                    newDrops.add(drop);

                                    System.out.println(
                                        "[CollisionReferee] Defeated " + enemy.getName()
                                            + "! Dropped " + dropType
                                            + " item at (" + enemy.getX()
                                            + ", " + enemy.getY() + ")"
                                    );
                                }
                            }
                        }

                        bullet.destroy();
                        break;
                    }
                }
            }
        }

        for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {
            if (bullet.isDestroyed()) continue;

            if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {
                if (!player.isInvulnerable()) {
                    System.out.println("[CollisionReferee] Enemy Bullet hit " + player.getName() + "!");
                    player.takeDamage(100, bulletManager, entities);
                    break;
                }
            } else if (!bullet.hasBeenGrazed()
                && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())) {
                bullet.setGrazed(true);
                player.addGraze();

                System.out.println(
                    "[CollisionReferee] GRAZE! " + player.getName()
                        + " grazed an enemy bullet (+50 pts)!"
                );
            }
        }

        for (GameObject entity : entities) {
            if (entity.isDestroyed()) continue;

            if (entity instanceof Item item) {
                if (player.getCoreHitbox().overlaps(item.getCoreHitbox())
                    || player.getGrazeHitbox().overlaps(item.getCoreHitbox())) {
                    System.out.println("[CollisionReferee] Player collected item: " + item.getItemType());
                    player.collectItem(item);
                }
            } else if (entity instanceof Enemy enemy && enemy.isCollidable()) {
                if (player.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                    if (!player.isInvulnerable()) {
                        System.out.println(
                            "[CollisionReferee] CORE COLLISION! Player collided with "
                                + enemy.getName() + "!"
                        );

                        player.takeDamage(100, bulletManager, entities);
                    }
                }
            }
        }

        entities.addAll(newDrops);
    }
}
