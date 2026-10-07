package com.ryoma.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.ryoma.frontend.objects.GameObject;
import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.objects.enemies.Boss;
import com.ryoma.frontend.objects.enemies.Fairy;
import com.ryoma.frontend.objects.items.ItemType;
import com.ryoma.frontend.systems.AssetManager;
import com.ryoma.frontend.systems.EntityFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.ryoma.frontend.systems.EntityFactory.*;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private List<GameObject> entities;

    @Override
    public void create() {
        batch = new SpriteBatch();
        entities = new ArrayList<>();

        // Inisialisasi Asset Singleton
        AssetManager.getInstance().init();

        // Pembuatan entitas lewat Factory
        player = createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);
        Fairy redFairy = createFairy(150, 380, "Red Fairy", 20);
        Fairy blueFairy = createFairy(250, 380, "Blue Fairy", 20,"fairy_idle_blue");
        Boss boss = createBoss(380, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(200, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(320, 480, ItemType.POINT);

        entities.add(player);
        entities.add(createFairy(150, 380, "Stage 1 Fairy", 20));
        entities.add(createBoss(380, 400, "Rumia", 150));
        entities.add(EntityFactory.createItem(200, 450, ItemType.POWER));
        entities.add(EntityFactory.createItem(320, 480, ItemType.POINT));
    }

    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                System.out.println("Removed via Generic Iterator: " + entity.getClass().getSimpleName());
                iterator.remove();
            }
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            entities.add(player.shootBullet());
        }

        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (!a.isDestroyed() && !b.isDestroyed()) {
                    if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                        a.onCollision(b);
                        b.onCollision(a);
                    }
                }
            }
        }

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // Render menggunakan SpriteBatch
        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()){
                entity.render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        AssetManager.getInstance().dispose();
    }
}
