package com.ryoma.frontend.objects.patterns.entityStrategy;

import com.ryoma.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}
