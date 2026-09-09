package com.ryoma.frontend;

import com.badlogic.gdx.graphics.Color;

/*
 * Penjelasan Soal 7 (Tipe Inheritance):
 * Tipe inheritance yang terbentuk dari GameObject lalu Enemy lalu Fairy / Boss adalah:
 * 1. Multilevel Inheritance: Tingkatan bertingkat dari GameObject diturunkan ke Enemy,
 *    lalu Enemy diturunkan ke Fairy/Boss.
 * 2. Hierarchical Inheritance: Satu parent class Enemy memiliki beberapa
 *    subclass turunan langsung (Fairy dan Boss).
 */
public class Boss extends Enemy {

    // Soal 7a
    public Boss(String name, int hp) {
        super(380f, 400f, 48f, 48f, Color.BLUE, name, hp, 5000L);
    }

    // Soal 7b
    public Boss(float x, float y, String name, int hp) {
        super(x, y, 48f, 48f, Color.BLUE, name, hp, 5000L);
    }
}
