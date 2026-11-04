package com.ryoma.frontend.objects.patterns;

import com.ryoma.frontend.systems.BulletManager;

public interface ShootingPattern {
    void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer);
}
