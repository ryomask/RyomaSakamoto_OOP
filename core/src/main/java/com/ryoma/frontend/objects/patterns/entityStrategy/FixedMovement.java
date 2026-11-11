package com.ryoma.frontend.objects.patterns.entityStrategy;

import com.ryoma.frontend.objects.GameObject;

public class FixedMovement implements EntityMovementPattern {
    @Override
    public void move(GameObject entity, float delta) {
        // Fixed position: tidak ada perpindahan posisi
    }
}
