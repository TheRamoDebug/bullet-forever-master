package com.utilClasses;

import com.badlogic.gdx.Preferences;

public class Player {

    private static int health;
    private static int levelOfPlayer;
    private static int numberLevel;
    private static int enemiesDeath;
    private static int damage;

    public static void defaultStats() {
        damage = 50;
        health = 6;
    }

    // MODO INFINITO
    private static int maxScoreInfinite;
    private static int currectDeaths;

    private static boolean deleteLife = false;


    public static void loadInformation() {
        Preferences prefs = SettingsClass.getPrefs();

        enemiesDeath = prefs.getInteger("enemiesDeath", 0);
        numberLevel = prefs.getInteger("numberLevel", 1);
        maxScoreInfinite = prefs.getInteger("maxScoreInfinite", 0);

        selectLevel();
    }


    public static void saveInformation() {
        Preferences prefs = SettingsClass.getPrefs();

        prefs.putInteger("maxScoreInfinite", maxScoreInfinite);
        prefs.putInteger("enemiesDeath", enemiesDeath);
        prefs.putInteger("numberLevel", numberLevel);

        prefs.flush();
    }


    public static void selectLevel() {
        if (enemiesDeath >= 0) {
            levelOfPlayer = 1;
            defaultStats();
        }
    }


    public static void setLessHealth() {
        health -= 1;

        deleteLife = true;

        if (health <= 0) {
            health = 0;
        }
    }


    public static boolean canRestLife() {
        return deleteLife;
    }


    public static void changeBool() {
        deleteLife = false;
    }


    public static void score() {
        enemiesDeath += 1;
    }


    public static boolean isAlive() {
        return health > 0;
    }


    public static void setHealth(int newHealth) {
        health = newHealth;
    }


    public static int getDamage() {
        return damage;
    }


    public static int getHealth() {
        return health;
    }


    public static int getEnemiesDeath() {
        return enemiesDeath;
    }


    public static int getNumberLevel() {
        return numberLevel;
    }


    // MODO INFINITO
    public static int maxScore() {
        if (maxScoreInfinite <= currectDeaths) {
            maxScoreInfinite = currectDeaths;
        }

        return maxScoreInfinite;
    }


    // MODO INFINITO
    public static void setCurrentDeaths(int current) {
        currectDeaths += current;
    }


    public void managerPlayer() {

    }
}
