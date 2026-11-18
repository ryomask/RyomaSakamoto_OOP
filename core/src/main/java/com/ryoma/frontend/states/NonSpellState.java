package com.ryoma.frontend.states;

import com.ryoma.frontend.objects.GameObject;
import com.ryoma.frontend.objects.enemies.Boss;
import com.ryoma.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.ryoma.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.ryoma.frontend.systems.BulletManager;
import com.ryoma.frontend.systems.LevelWaveManager;


import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

    public abstract class NonSpellState implements WaveState {
        private static final float TEST_TIMEOUT_SECONDS = 4f;
        private static final float GLIDE_DISTANCE = 90f;
        private static final float GLIDE_SECONDS = 1f;

        private final Random random = new Random();
        private List<Supplier<Attack>> attacks;
        private Attack currentAttack;
        private int currentIndex;
        private float phaseTimer = 0f;

        protected abstract String getPhaseName();
        protected abstract int getPhaseLife();
        protected abstract int getLifeThreshold();
        protected abstract List<Supplier<Attack>> createAttacks(GameObject player);
        protected abstract float getTimeoutSeconds();
        protected abstract WaveState createNextState();

        protected float getIntroSeconds() { return 0f; }
        protected boolean isCollidableDuringIntro() { return true; }
        protected EntityMovementPattern createIntroMovement(Boss boss) { return new FixedMovement(); }

        // ====================================================
        // Core Methods
        // ====================================================

        // TODO: Implementasi method yang harusnya diwariskan dari WaveState, yaitu:

        @Override
        public void onEnter(LevelWaveManager manager) {
            phaseTimer = 0f;
            currentIndex = -1;
            currentAttack = null;

            Boss boss = manager.getBoss();
            if (boss == null || boss.isDestroyed()) {
                boss = manager.getBoss();
                320f, 440f, "Rumia", getPhaseLife());
            }

            boss.setLife((getPhaseLife()));
            boss.setSurvivesAtZeroHp(true);

            if (getIntroSeconds() > 0f) {
                boss.setDamageable(false);
                boss.setCollidable(true);
                boss.setMovementPattern(createIntroMovement(boss));
            } else {
                boss.setDamageable(true);
                boss.setCollidable(true);
                boss.setMovementPattern(BossGlide.random(boss, GLIDE_DISTANCE, GLIDE_SECONDS, random));
            }
            attacks = createAttacks(manager.getPlayer());
        }

        @Override
        public void update(LevelWaveManager manager, float delta){
            phaseTimer += delta;
            Boss boss = manager.getBoss();
            if (boss != null
                && !boss.isDestroyed()
                && phaseTimer >= getIntroSeconds()){
            }

            if (currentAttack == null){
                boss.setDamageable(true);
                boss.setCollidable(true);
                startAttack(boss, 0);
            } else if (currentAttack.isFinished()){
                startAttack(boss, pickAnotherAttack());
            }

            BulletManager bulletManager =  manager.getBulletManager();
            currentAttack.update(boss, bulletManager, delta);
        }

        float timeout = manager.getPlayer() == null ? TEST_TIMEOUT_SECONDS : getTimeoutSeconds();
        boolean hpCutoffReached = (boss != null && boss.getHp() < getLifeThreshold());

        if (hpCutoffReached || phaseTimer >= timeout){
            if (boss != null){
                boss.setlife(getLifeThreshold());
            }

            BulletManager bulletManager = manager.getBulletManager();
            if (bulletManager != null){
                bulletManager.clearEnemyBullets();
            }
            manager.setState(createNextState());
        }

        private void startAttack(Boss boss, int index) {
            currentIndex = index;
            currentAttack = attacks.get(index).get();
            boss.setMovementPattern(BossGlide.random(boss, GLIDE_DISTANCE, GLIDE_SECONDS, random));
        }

        private int pickAnotherAttack() {
            int next = random.nextInt(attacks.size() - 1);
            return (next >= currentIndex) ? next + 1 : next;
        }

        public boolean isHpCutoffReached() {
            return hpCutoffReached;
        }
    }


