package com.ryoma.frontend;

public class Enemy {
    // Attributes / Fields
    public String name;
    public int hp;
    public int damage;
    public int power;
    public int spellCards;
    public int target;

    // Constructor
    public Enemy(String name, int damage, int hp, int power, int spellCards, int target) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.power = power;
        this.spellCards = spellCards;
        this.target = target;

        public void attack(Player player, int damage) {
            system.out.println(enemy "Musuh" + )


        }
        }
            // 1. Tampilkan informasi bahwa Enemy menyerang Player dalam format: [EnemyName] unleashes bullet barrage on [PlayerName]!

            // 2. Panggil takeDamage() milik Player menggunakan damage yang diberikan.
        }

