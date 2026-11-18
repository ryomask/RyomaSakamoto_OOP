package com.ryoma.frontend;

import com.ryoma.frontend.commands.*;
import com.ryoma.frontend.objects.GameObject;
import com.ryoma.frontend.objects.EntityShooter;
import com.ryoma.frontend.objects.Player;
import com.ryoma.frontend.objects.bullets.Bullet;
import com.ryoma.frontend.objects.bullets.BulletType;
import com.ryoma.frontend.objects.enemies.Boss;
import com.ryoma.frontend.objects.enemies.Fairy;
import com.ryoma.frontend.objects.items.Item;
import com.ryoma.frontend.objects.items.ItemType;
import com.ryoma.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.ryoma.frontend.objects.patterns.shootingStrategy.RingShot;
import com.ryoma.frontend.objects.patterns.bulletStrategy.LinearBulletMovement;
import com.ryoma.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.ryoma.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.ryoma.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.ryoma.frontend.systems.BulletManager;
import com.ryoma.frontend.systems.CollisionReferee;
import com.ryoma.frontend.systems.EntityFactory;
import com.ryoma.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        runTest();
    }

    public static void runTest() {
        Test runner = new Test();

        // ==========================================
        // MODULE 1: BASIC CLASSES & OBJECT INTERACTION
        // ==========================================
        System.out.println("=== TOUHOU OOP PRACTICUM - MODULE 1: BASIC CLASSES & OBJECT INTERACTION ===");

        Player player = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        Boss cirno = EntityFactory.createBoss(200, 420, "Cirno (Stage 2 Boss)", 50);

        System.out.println("\n--- Initial Battle State ---");
        System.out.println("Player: " + player.getName() + " | HP: " + player.getHp() + " | Power: " + player.getPower() + " | SpellCards: " + player.getSpellCards());
        System.out.println("Enemy:  " + cirno.getName() + " | HP: " + cirno.getHp());

        System.out.println("\n--- Turn 1: Player Shoots Enemy ---");
        cirno.takeDamage(15);

        System.out.println("\n--- Turn 2: Enemy Counter-attacks ---");
        BulletManager bManTemp = new BulletManager();
        List<GameObject> tempEntities = new ArrayList<>();
        player.takeDamage(30, bManTemp, tempEntities);

        System.out.println("\n--- Turn 3: Player Shoots Enemy Finishing Blow ---");
        cirno.takeDamage(50);

        System.out.println("\n--- Turn 4: Enemy Deals Fatal Damage to Reimu ---");
        player.takeDamage(100, bManTemp, tempEntities);

        System.out.println("\n=== Battle Simulation Complete ===");


        // ==========================================
        // MODULE 2: ENCAPSULATION, INHERITANCE & SCORE SYSTEM
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 2: ENCAPSULATION, INHERITANCE & SCORE SYSTEM ===");

        Player player2 = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        Fairy fairy = EntityFactory.createFairy(60f, 480f, "Stage 1 Fairy", 20, "fairy_idle_blue");
        Boss cirno2 = EntityFactory.createBoss(200, 420, "Cirno (Stage 2 Boss)", 150);
        Item pointItem = EntityFactory.createItem(300, 450, ItemType.POINT);

        System.out.println("\n--- Testing Encapsulation & Inheritance ---");
        System.out.println("Player: " + player2.getName() + " | Position: (" + player2.getX() + ", " + player2.getY() + ")");
        System.out.println("Fairy:  " + fairy.getName() + " | Defeat Worth: " + fairy.getScoreValue() + " pts");
        System.out.println("Boss:   " + cirno2.getName() + " | Defeat Worth: " + cirno2.getScoreValue() + " pts | Size: " + cirno2.getWidth() + "x" + cirno2.getHeight());
        System.out.println("Item:   " + pointItem.getItemType() + " Item | Value: " + pointItem.getScoreValue() + " pts | Speed: " + pointItem.getSpeed());

        System.out.println("\n--- Testing Item Movement Update ---");
        System.out.println("Initial Item Y: " + pointItem.getY());
        pointItem.update(0.5f);
        System.out.println("Item Y after 0.5s update: " + pointItem.getY() + " (linear downward movement)");

        System.out.println("\n--- Testing Scoring System ---");
        System.out.println("Initial Score: " + player2.getScore());
        fairy.takeDamage(20);
        player2.addScore(fairy.getScoreValue());
        player2.collectItem(pointItem);
        cirno2.takeDamage(150);
        player2.addScore(cirno2.getScoreValue());
        System.out.println("Final Score: " + player2.getScore() + " pts");

        System.out.println("\n=== Module 2 Test Completed Successfully ===");


        // ==========================================
        // MODULE 3: POLYMORPHISM & ABSTRACTION
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 3: POLYMORPHISM & ABSTRACTION ===");

        System.out.println("--- Testing ItemType & BulletType Enums ---");
        System.out.print("Available Item Types: ");
        for (ItemType type : ItemType.values()) {
            System.out.print(type + " ");
        }
        System.out.println();

        System.out.print("Available Bullet Types: ");
        for (BulletType type : BulletType.values()) {
            System.out.print(type + " ");
        }
        System.out.println();

        List<GameObject> entities = new ArrayList<>();
        Player player3 = EntityFactory.createPlayer(100, 100, "Reimu Hakurei", 100, 15, 3);
        Fairy fairy3 = EntityFactory.createFairy(100, 100, "Stage 1 Fairy", 20, "fairy_idle_blue");
        Boss boss3 = EntityFactory.createBoss(200, 200, "Cirno", 150);
        Item item3 = EntityFactory.createItem(300, 300, ItemType.POWER);

        entities.add(fairy3);
        entities.add(boss3);
        entities.add(item3);

        System.out.println("\n--- Testing Iterative Update on List<GameObject> entities ---");
        System.out.println("Initial Item Y: " + item3.getY());
        for (GameObject entity : entities) {
            entity.update(0.5f);
        }
        System.out.println("Item Y after entity.update(0.5s): " + item3.getY());

        System.out.println("\n--- Testing Polymorphic Collision Information ---");
        for (GameObject entity : entities) {
            System.out.println("Simulating collision at (" + player3.getX() + ", " + player3.getY() + ") (" + entity.getClass().getSimpleName() + " position):");
            player3.onCollision(entity);
            player3.setX(entity.getX());
            player3.setY(entity.getY());
        }

        System.out.println("\n=== Module 3 Test Completed Successfully ===");


        // ==========================================
        // MODULE 4: COLLECTIONS, GENERICS & ITERATORS
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 4: COLLECTIONS, GENERICS & ITERATORS ===");

        List<GameObject> gameEntities = new ArrayList<>();

        Player player4 = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        Fairy targetFairy = EntityFactory.createFairy(200, 200, "Target Fairy", 20, "fairy_idle_blue");

        gameEntities.add(player4);
        gameEntities.add(targetFairy);

        System.out.println("\n--- Initial Entities in List<GameObject> ---");
        System.out.println("Entities Count: " + gameEntities.size());

        System.out.println("\n--- Turn 1: Player Fires Bullet ---");
        Bullet bullet1 = new Bullet(200, 150, 400f, BulletType.AMULET, 25);
        gameEntities.add(bullet1);

        System.out.println("Entities Count before update: " + gameEntities.size());

        System.out.println("\n--- Updating frame: Bullet moves upward & collides with Fairy ---");
        for (int i = 0; i < gameEntities.size(); i++) {
            for (int j = i + 1; j < gameEntities.size(); j++) {
                GameObject e1 = gameEntities.get(i);
                GameObject e2 = gameEntities.get(j);
                if (e1.getCoreHitbox().overlaps(e2.getCoreHitbox())) {
                    e1.onCollision(e2);
                    e2.onCollision(e1);
                }
            }
        }

        runner.updateAndClean(gameEntities, 0.125f, 640, 480);

        System.out.println("Entities Count after generic Iterator cleanup: " + gameEntities.size());

        System.out.println("\n=== Module 4 Test Completed Successfully ===");


        // ==========================================
        // MODULE 7: OBJECT POOL & MEDIATOR PATTERN
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 7: OBJECT POOL & MEDIATOR ===");

        BulletManager bulletManager = new BulletManager();
        CollisionReferee referee = new CollisionReferee();

        Player player7 = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);

        Fairy enemyFairy = EntityFactory.createFairy(200, 200, "Stage 1 Fairy", 20, "fairy_idle_blue");
        List<GameObject> m7Entities = new ArrayList<>();
        m7Entities.add(enemyFairy);

        System.out.println("\n--- Testing Object Pool ---");
        System.out.println("Initial Player Pool Size: " + bulletManager.getPlayerPoolSize());
        System.out.println("Initial Active Player Bullets: " + bulletManager.getActivePlayerBullets().size());

        player7.shootBullet(bulletManager);
        System.out.println("Active Player Bullets after 1st shoot: " + bulletManager.getActivePlayerBullets().size());

        bulletManager.update(0.26f, 640, 480);

        System.out.println("\n--- Testing Mediator Pattern Collision Resolution ---");
        referee.resolveCollisions(player7, m7Entities, bulletManager);

        bulletManager.update(0.01f, 640, 480);

        System.out.println("\n--- Object Pool Recycling Check ---");
        System.out.println("Active Player Bullets after hit & recycling: " + bulletManager.getActivePlayerBullets().size());
        System.out.println("Recycled Player Pool Size: " + bulletManager.getPlayerPoolSize());

        player7.shootBullet(bulletManager);
        System.out.println("Active Player Bullets after 2nd shoot (Re-used pooled instance): " + bulletManager.getActivePlayerBullets().size());
        System.out.println("Player Pool Size after re-using: " + bulletManager.getPlayerPoolSize());

        System.out.println("\n--- Testing Graze vs Core Hitbox Detection ---");
        Bullet enemyBullet = bulletManager.spawnEnemyBullet(190, 50, 0, 0, 15);
        long scoreBeforeGraze = player7.getScore();
        referee.resolveCollisions(player7, m7Entities, bulletManager);
        System.out.println("Graze score added (+50 pts): " + (player7.getScore() - scoreBeforeGraze == 50));
        System.out.println("Player Graze Counter: " + player7.getGrazeCount());

        System.out.println("\n=== Module 7 Test Completed Successfully ===");


        // ==========================================
        // MODULE 8: COMMAND PATTERN & OBSERVER PATTERN
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 8: COMMAND & OBSERVER PATTERNS ===");

        Player player8 = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        BulletManager bManager8 = new BulletManager();
        GameHUD hud = new GameHUD();

        System.out.println("\n--- Testing Observer Pattern Registration ---");
        player8.registerObserver(hud);
        System.out.println("HUD Received Initial Score: " + hud.getScore());
        System.out.println("HUD Received Initial HP:    " + hud.getHp());
        System.out.println("HUD Received Initial Lives: " + hud.getLives());
        System.out.println("HUD Received Initial Bombs: " + hud.getSpellCards());
        System.out.println("HUD Received Initial Graze: " + hud.getGrazeCount());

        System.out.println("\n--- Testing Command Pattern Execution ---");
        Command moveUp = new MoveCommand(0, 0.5f);
        moveUp.execute(player8, bManager8);
        System.out.println("Player position after MoveCommand: (" + player8.getX() + ", " + player8.getY() + ")");

        Command focusOn = new FocusCommand(true);
        focusOn.execute(player8, bManager8);
        System.out.println("Player Focus Mode Enabled: " + player8.isFocused());

        Command shoot = new ShootCommand();
        shoot.execute(player8, bManager8);
        System.out.println("Active Player Bullets after ShootCommand: " + bManager8.getActivePlayerBullets().size());

        bManager8.spawnEnemyBullet(200, 300, 0, -100, 15);
        bManager8.spawnEnemyBullet(250, 350, 0, -100, 15);
        System.out.println("Active Enemy Bullets before Bomb: " + bManager8.getActiveEnemyBullets().size());

        Command bomb = new BombCommand();
        bomb.execute(player8, bManager8);

        System.out.println("Active Enemy Bullets after BombCommand: " + bManager8.getActiveEnemyBullets().size());
        System.out.println("HUD Received Updated Bombs: " + hud.getSpellCards());

        System.out.println("\n--- Testing Observer Notification on Graze & Item Collection ---");
        player8.addGraze();
        System.out.println("HUD Received Updated Graze: " + hud.getGrazeCount());

        Item pItem = EntityFactory.createItem(200, 50, ItemType.POWER);
        player8.collectItem(pItem);
        System.out.println("HUD Received Updated Score: " + hud.getScore());

        System.out.println("\n--- Testing Life Stock & Respawn System ---");
        System.out.println("Initial Lives: " + player8.getLives());
        player8.update(6.0f); // Expire bomb invulnerability timer (5.0s)
        player8.takeDamage(100, bManager8, null);
        System.out.println("Lives after fatal hit: " + player8.getLives() + " (Respawned with i-frames: " + player8.isInvulnerable() + ")");
        System.out.println("HUD Received Updated Lives: " + hud.getLives());
        Item lifeItem = EntityFactory.createItem(200, 50, ItemType.LIFE);
        player8.collectItem(lifeItem);
        System.out.println("Lives after collecting LIFE item: " + player8.getLives());
        System.out.println("HUD Received Restored Lives: " + hud.getLives());

        System.out.println("\n--- Testing InputHandler (Bob Nystrom Invoker & Key Rebinding) ---");
        InputHandler inputHandler = new InputHandler();
        System.out.println("Default Shoot Key: " + inputHandler.getKeyShoot() + " (Z)");
        inputHandler.setKeyShoot(62); // Rebind to Space
        System.out.println("Rebound Shoot Key: " + inputHandler.getKeyShoot() + " (Space)");
        System.out.println("Zero-GC Commands Buffer Initialized: " + (inputHandler.handleInput(0.016f) != null));

        System.out.println("\n=== Module 8 Test Completed Successfully ===");


        // ==========================================
        // MODULE 9: STRATEGY PATTERN & DEPENDENCY INJECTION
        // ==========================================
        System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 9: STRATEGY PATTERN & DEPENDENCY INJECTION ===");

        EntityShooter shooterPlayer = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        EntityShooter shooterBoss = EntityFactory.createBoss(200, 420, "Cirno", 150);
        BulletManager bManager9 = new BulletManager();

        System.out.println("\n--- Testing EntityShooter Base Class Hierarchy ---");
        System.out.println("Player is instance of EntityShooter: " + (shooterPlayer instanceof EntityShooter));
        System.out.println("Boss is instance of EntityShooter:   " + (shooterBoss instanceof EntityShooter));

        System.out.println("\n--- Testing Runtime Dependency Injection Strategy Swapping ---");
        shooterBoss.setShootingPattern(new RingShot(200f, 8, 15));
        shooterBoss.shootBullet(bManager9);
        System.out.println("Active Enemy Bullets after RingShot: " + bManager9.getActiveEnemyBullets().size());

        bManager9.clearEnemyBullets();

        // [PERBAIKAN] Menggunakan constructor SpreadShot 4 parameter yang valid
        shooterBoss.setShootingPattern(new SpreadShot(200f, 3, 30f, 10));
        shooterBoss.shootBullet(bManager9);
        System.out.println("Active Enemy Bullets after SpreadShot: " + bManager9.getActiveEnemyBullets().size());

        System.out.println("\n--- Testing Player Power-Based Dynamic Strategy Swapping ---");
        Player reimu = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 0, 3);
        System.out.println("Player Strategy at Power 0:   " + reimu.getShootingPattern().getClass().getSimpleName());

        reimu.setPower(10);
        System.out.println("Player Strategy at Power 10:  " + reimu.getShootingPattern().getClass().getSimpleName() + " (Front + Homing Side)");

        reimu.setPower(20);
        System.out.println("Player Strategy at Power 20:  " + reimu.getShootingPattern().getClass().getSimpleName());

        reimu.setPower(40);
        System.out.println("Player Strategy at Power 40:  " + reimu.getShootingPattern().getClass().getSimpleName());

        reimu.setPower(80);
        System.out.println("Player Strategy at Power 80:  " + reimu.getShootingPattern().getClass().getSimpleName() + " (with Homing Needles)");

        reimu.setPower(128);
        System.out.println("Player Strategy at Power 128: " + reimu.getShootingPattern().getClass().getSimpleName() + " (MAX Power)");

        System.out.println("\n--- Testing Entity Movement Patterns ---");
        shooterBoss.setMovementPattern(new LinearEntityMovement(50f, -20f));
        float bossInitialX = shooterBoss.getX();
        float bossInitialY = shooterBoss.getY();
        shooterBoss.update(1.0f);
        System.out.println("Boss Position after 1s LinearEntityMovement: (" + shooterBoss.getX() + ", " + shooterBoss.getY() + ")");
        System.out.println("Position updated correctly: " + (shooterBoss.getX() == bossInitialX + 50f && shooterBoss.getY() == bossInitialY - 20f));

        Fairy testFairy = EntityFactory.createFairy(150, 400, "Stage 1 Fairy", 10, "fairy_idle_blue");
        float fairyInitialY = testFairy.getY();
        testFairy.update(1.0f);
        System.out.println("Fairy Position after 1s LinearEntityMovement: (" + testFairy.getX() + ", " + testFairy.getY() + ")");

        shooterBoss.setMovementPattern(new TargetPointMovement(200f, 400f, 100f));
        shooterBoss.update(1.0f);
        System.out.println("Boss Position after 1s TargetPointMovement towards (200, 400): (" + shooterBoss.getX() + ", " + shooterBoss.getY() + ")");

        System.out.println("\n--- Testing Bullet Movement Trajectory Strategies ---");
        bManager9.clearEnemyBullets();

        // [PERBAIKAN] Menggunakan constructor SpreadShot 5 parameter dengan movement pattern yang valid
        shooterBoss.setShootingPattern(new SpreadShot(200f, 3, 30f, 10, new LinearBulletMovement()));
        shooterBoss.shootBullet(bManager9);
        Bullet spreadBullet = bManager9.getActiveEnemyBullets().get(0);
        spreadBullet.update(0.5f);
        System.out.println("SpreadShot Bullet Position after 0.5s LinearBulletMovement: (" + spreadBullet.getX() + ", " + spreadBullet.getY() + ")");

        Bullet homingBullet = new Bullet(100, 100, 200f, BulletType.AMULET, 10);
        homingBullet.setMovementPattern(new HomingBulletMovement(shooterBoss, 90f));
        homingBullet.update(0.5f);
        System.out.println("Homing Bullet Angle steered toward Boss: " + homingBullet.getAngle() + "°");

        List<GameObject> m9Entities = new ArrayList<>();
        m9Entities.add(shooterBoss);
        bManager9.setEntities(m9Entities);
        reimu.setPower(10);
        reimu.shootBullet(bManager9);
        Bullet decoupledSideBullet = bManager9.getActivePlayerBullets().get(1);
        float angleBeforeInject = decoupledSideBullet.getAngle();
        bManager9.update(0.1f, 640, 480);
        System.out.println("Decoupled SideShot Homing Steered via BulletManager: " + angleBeforeInject + "° -> " + decoupledSideBullet.getAngle() + "°");
        bManager9.clearPlayerBullets();

        System.out.println("\n=== Module 9 Test Completed Successfully ===");
    }

    // Non-static (Instance) Generic Method with Bounded Type Parameter <T extends GameObject>
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                if (entity.isOffScreen(screenWidth, screenHeight)) {
                    System.out.println("Removed via Generic Iterator (off-screen): " + entity.getClass().getSimpleName() + " at y=" + entity.getY());
                } else {
                    System.out.println("Removed via Generic Iterator (destroyed): " + entity.getClass().getSimpleName());
                }
                iterator.remove(); // Safe removal using Iterator!
            }
        }
    }
}
