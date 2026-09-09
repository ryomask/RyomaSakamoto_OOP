package com.ryoma.frontend;

import com.badlogic.gdx.graphics.Color;

// Soal 6
public class Fairy extends Enemy {

    // Soal 6a
    public Fairy(String name, int hp) {
        super(150f, 380f, 24f, 24f, Color.PINK, name, hp, 500L);
    }

    // Soal 6b
    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 24f, 24f, Color.PINK, name, hp, 500L);
    }
}


