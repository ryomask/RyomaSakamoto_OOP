package com.ryoma.frontend;

public class Player {
    // Attributes / Fields
    public String name;
    public int hp;
    public int damage;
    public int power;
    public int spellCards;
    public int target;

    // Constructor
    public Player(String name, int damage, int hp, int power, int spellCards, int target) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.power = power;
        this.spellCards = spellCards;
        this.target = target;
    }
    public void takeDamage(int damage) {
        // 1. Kurangi hp sebesar nilai damage.
        hp -= damage;

        // 2. HP tidak boleh kurang dari 0.
        if(hp < 0){
            hp = 0;
        }
        // 3. Tampilkan HP saat ini dalam format: [EnemyName] took [damage] damage! HP: [currentHP]/[maxHP]
        if (hp > 0){
            hp = 0;
        } else {
            System.out.print(name + "took" + damage "damage remaining hp: " + hp);
        } elif {
            System.out.println(name + "enemy lose");
        }// 4. Jika HP mencapai 0, tampilkan bahwa Enemy telah dikalahkan dalam format: [EnemyName] was defeated!
    }
    public void shoot(Enemy target) {

        int damage = 10;
        System.out.println(name + "has shooting: " + target "Target" + damage "OMG" );
        target.takedamage(damage);


        // 1. Buat int bernama damage yang dihitung dengan menambahkan power sebanyak 10.

        // 2. Tampilkan informasi bahwa Player menembak Enemy dalam format: [name] shoots [TargetName] dealing [damage] DMG!

        // 3. Panggil method takeDamage() milik object Enemy.
    }

}
