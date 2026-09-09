package com.ryoma.frontend;

import com.badlogic.gdx.graphics.Color;

// Soal 5
public class Enemy extends GameObject {
    public String name;
    public int hp;
    public int maxHp;

    // Soal 5
    public long scoreValue;

    // Soal 5a
    public Enemy(String name, int hp) {
        super(200f, 380f, 24f, 24f, 0f, Color.PINK);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100L;
    }

    // Soal 5b
    public Enemy(float x, float y, float width, float height, Color color, String name, int hp, long scoreValue) {
        super(x, y, width, height, 0f, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    // Soal 10
    public boolean takeDamage(int damage) {

        if (this.hp <= 0) {
            return false;
        }

        this.hp -= damage;

        if (this.hp <= 0) {
            this.hp = 0;
            setHp(getHp() - damage);
            return true;
        }

        return false;
    }

    public int getHp() {
        return hp;
    }

    public long getScoreValue() {
        return scoreValue;
    }
    public long setScoreValue() {
        return scoreValue;
    }
    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public String getName(){
        return name;
    }
    public String setName(){
        return name;
    }

}
