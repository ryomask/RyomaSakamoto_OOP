package com.ryoma.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float speed;
    protected Color color;

    public GameObject(float x, float y, float width, float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    /*
     * Penjelasan Soal 3:
     * Method update() dibiarkan kosong (non-abstract) agar subclass yang sifatnya
     * statis atau tidak membutuhkan pergerakan otomatis tidak dipaksa untuk
     * meng-override method ini.
     *
     * Jika dibuat abstract, seluruh class turunan seperti Player atau Enemy
     * wajib menuliskan method update(). Dan membuatnya non abstract dengan
     * body kosong, hanya subclass Item yang cukup mengoverride method ini
     */
    public void update(float delta) {
        // ini kosong
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
    public float setY(){
        return y;
    }
    public float setX(){
        return x;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public float getSpeed(){
        return speed;
    }

    public Color setColor(){
        return color;
    }

    public void setWidth(float width) {
        if (width > 0) this.width = width;
    }

    public void setHeight(float height) {
        if (height > 0) this.height = height;
    }

    public void setSpeed(float speed) {
        if (speed >= 0) this.speed = speed;
    }


}
