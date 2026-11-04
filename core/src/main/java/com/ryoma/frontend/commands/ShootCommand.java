package com.ryoma.frontend.commands;

import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.systems.BulletManager;

public class ShootCommand implements Command {
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null && bulletManager != null) {
            player.shootBullet(bulletManager);
        }
    }
}
