package com.ryoma.frontend.objects.patterns.bulletStrategy;

import com.ryoma.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {
    void move(Bullet bullet, float delta);
}
