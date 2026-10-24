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
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.items.ItemType;
import com.ryoma.frontend.systems.AssetManager;
import com.ryoma.frontend.systems.AudioManager;
import com.ryoma.frontend.systems.EntityFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private List<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    @Override
    public void create() {
        // TODO 1:
        // Pada inisialisasi renderer, buat SpriteBatch dan simpan ke batch.
        // Hint LibGDX: new SpriteBatch().
        batch = new SpriteBatch();

        // TODO 2:
        // Inisialisasi list fairy dan entities sebagai ArrayList kosong.
        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        // TODO 3:
        // Sebelum membuat entitas, ambil instance AssetManager dan panggil init().
        AssetManager.getInstance().init();
        AudioManager.getInstance().init();

        // TODO 4:
        // Ubah pembuatan semua entitas! Ikuti tabel dan buat Player, Fairy, Boss, dan Item
        // agar memakai metode EntityFactory yang benar.
        // Untuk kedua Fairy, masukkan mereka ke list fairy menggunakan add(...).
        player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);
        Fairy redFairy = EntityFactory.createFairy(150, 380, "Red Fairy", 20);
        Fairy blueFairy = EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue");
        fairy.add(redFairy);
        fairy.add(blueFairy);

        boss = EntityFactory.createBoss(380, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(200, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(320, 480, ItemType.POINT);

        // TODO 5:
        // Masukkan semua objek yang baru saja kita buat ke dalam entities.
        entities.add(player);
        entities.add(redFairy);
        entities.add(blueFairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
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
                // TODO: Buat agar setiap entity melakukan method .render() dengan mengoper parameter SpriteBatch.
                entity.render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        // TODO: Panggil dispose untuk AssetManager agar Texture yang dimuat juga dilepas.
        AssetManager.getInstance().dispose();
        AudioManager.getInstance().dispose();
    }
}
