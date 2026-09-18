package com.ryoma.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.ryoma.frontend.objects.Collidable;
import com.ryoma.frontend.objects.Player;

public class Boss extends Enemy {
    public Boss(String name, int hp) {
        super(380, 400, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            if (collisionCooldown <= 0) {
                System.out.println("Player touches boss");
                collisionCooldown = 1.0f;
            }
        }
    }
}
