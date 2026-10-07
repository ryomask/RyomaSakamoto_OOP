package com.ryoma.frontend.systems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.objects.bullets.Bullet;
import com.ryoma.frontend.objects.BulletType;
import com.ryoma.frontend.objects.enemies.Boss;
import com.ryoma.frontend.objects.enemies.Fairy;
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.items.ItemType;

public class EntityFactory {

    public static Boss createBoss(float x, float y, String name, int hp) {
        Boss boss = new Boss(x, y, name, hp);
        Animation<TextureRegion> anim = AssetManager.getInstance().getAnimation("boss_idle");
        boss.setAnimation(anim);
        return boss;
    }

    public static Fairy createFairy(float x, float y, String name, int hp) {
        Fairy fairy = new Fairy(x, y, name, hp);
        Animation<TextureRegion> anim = AssetManager.getInstance().getAnimation("fairy_idle_red");
        fairy.setAnimation(anim);
        return fairy;
    }

    public static Fairy createFairy(float x, float y, String name, int hp, String keyString) {
        Fairy fairy = new Fairy(x, y, name, hp);
        Animation<TextureRegion> anim = AssetManager.getInstance().getAnimation(keyString);
        fairy.setAnimation(anim);
        return fairy;
    }

    public static Item createItem(float x, float y, ItemType itemType) {
        Item item = new Item(x, y, itemType);
        String key = switch (itemType) {
            case POWER -> "item_power";
            case POINT -> "item_point";
            case BOMB  -> "item_bomb";
            case LIFE  -> "item_life";
        };
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(key);
        item.setSprite(sprite);
        return item;
    }

    public static Bullet createEnemyBullet(float x, float y, int damage) {
        Bullet bullet = new Bullet(x, y, 0f, BulletType.DANMAKU, damage);
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion("bullet_danmaku");
        bullet.setSprite(sprite);
        return bullet;
    }
}
