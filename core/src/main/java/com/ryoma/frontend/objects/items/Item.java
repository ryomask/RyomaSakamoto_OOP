package com.ryoma.frontend.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.ryoma.frontend.objects.Collidable;
import com.ryoma.frontend.objects.GameObject;
import com.ryoma.frontend.objects.Player;

public class Item extends GameObject {
    private String itemType;
    private long scoreValue;
    private com.ryoma.frontend.objects.items.ItemType itemTypeEnum;

    public Item(float x, float y, String itemType) {
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    public Item(float x, float y, float width, float height, float speed, String itemType) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    public Item(float x, float y, float width, float height, float speed, String itemType, long scoreValue) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = scoreValue;
    }

    public Item(float x, float y, com.ryoma.frontend.objects.items.ItemType itemTypeEnum) {
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemTypeEnum = itemTypeEnum;
        this.itemType = itemTypeEnum.name();
        this.scoreValue = itemTypeEnum.getScoreValue();
    }

    public Item(float x, float y, float width, float height, float speed, com.ryoma.frontend.objects.items.ItemType itemTypeEnum, long scoreValue) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemTypeEnum = itemTypeEnum;
        this.itemType = itemTypeEnum.name();
        this.scoreValue = scoreValue;
    }

    @Override
    public void update(float delta) {
        this.y -= speed * delta;
    }

    public String getItemType() { return itemType; }

    public com.ryoma.frontend.objects.items.ItemType getItemTypeEnum() { return itemTypeEnum; }

    public long getScoreValue() { return scoreValue; }

    public void setScoreValue(long scoreValue) { this.scoreValue = scoreValue; }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
        }
    }
}
