package com.ryoma.frontend.commands;

import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.systems.BulletManager;

public class MoveCommand implements Command {
    private float dx;
    private float dy;

    public MoveCommand(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void set(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.move(dx, dy);
            float animDx = 0f;
            if (dx < 0) animDx = -1f;
            else if (dx > 0) animDx = 1f;
            player.updateAnimationState(animDx);
        }
    }
}
