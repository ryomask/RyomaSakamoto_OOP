package com.ryoma.frontend;

import com.badlogic.gdx.graphics.Color;

// Soal 4
public class Player extends GameObject {
    public String name;
    public int hp;
    public int power;
    public int spellCards;

    // Soal 4
    public long score;

    // Soal 4a
    public Player(String name, int hp, int power, int spellCards) {
        super(280f, 40f, 32f, 32f, 0f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0L;
    }

    // Soal 4b
    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32f, 32f, 0f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0L;
    }

    // Soal 10
    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
        }
    }

    public long getScore() {
        return score;
    }
    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }
    public int getPower(){
        return power;
    }
    public int getSpellCards(){
        return spellCards;
    }
    public String getName() {
        return name;
    }
}
