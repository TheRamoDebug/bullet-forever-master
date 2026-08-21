package com.utilClasses;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;

public class Boss {

    private float health;
    private float maxHealth;
    private float x;
    private float y;
    private boolean alive;
    private Circle bossCollision;

    // PHASE
    private int phase;
    private float phaseHealth;
    private float phaseMaxHealth;
    private float phaseTimer;
    private float phaseDuration;

    // SHOOT
    private float shotTimer = 0f;
    private float patternTimer = 0f;

    // TARGETED
    private float targetedTimer = 0f;
    private float targetedInterval = 1.3f;

    // SPREAD
    private float spreadTimer = 0f;
    private float spreadInterval = 0.6f;
    private int spreadBullets = 20;
    private float spreadAngle = 7.3f;
    private float spreadBulletSpeed = 4.5f;
    private boolean spreadDirection = false;

    // RADIAL
    private int radialDirections = 14;
    private int radialRounds = 6;
    private int radialRoundsRemaining = 0;
    private float radialRoundTimer = 0f;
    private float radialInterval = 0.12f;
    private float radialAngleOffset = 0f;
    private float radialRotation = 8f;
    private float phaseTwoTimer = 0f;
    private boolean phaseTwoSpread = false;

    // SPIRAL
    private float spiralAngleA = 0f;
    private float spiralAngleB = 180f;
    private float spiralAngularSpeed = 70f;
    private float spiralBulletSpeed = 6f;
    private float spiralInterval = 0.20f;


    public Boss(float x, float y, float maxHealth) {

        this.x = x;
        this.y = y;
        bossCollision = new Circle(x, y, 0.6f);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.alive = true;
        this.phase = 1;
        this.phaseMaxHealth = 350f;
        this.phaseHealth = phaseMaxHealth;
        this.phaseTimer = 0f;
        this.phaseDuration = 40f;
    }


    public void damage(float damage) {

        if (!alive) {
            return;
        }

        health -= damage;
        phaseHealth -= damage;

        if (health <= 0f) {
            health = 0f;
            alive = false;
            return;
        }

        if (phaseHealth <= 0f) {
            phaseHealth = 0f;
            nextPhase();
        }
    }


    private void nextPhase() {

        if (phase >= 3) {
            alive = false;
            return;
        }

        phase++;
        phaseTimer = 0f;
        shotTimer = 0f;
        patternTimer = 0f;
        targetedTimer = 0f;
        spreadTimer = 0f;
        radialRoundsRemaining = 0;
        radialRoundTimer = 0f;
        radialAngleOffset = 0f;
        phaseTwoTimer = 0f;
        phaseTwoSpread = false;

        switch (phase) {

            case 2:
                phaseMaxHealth = 350f;
                phaseDuration = 50f;
                break;

            case 3:
                phaseMaxHealth = 300f;
                phaseDuration = 60f;
                break;
        }
        phaseHealth = phaseMaxHealth;
        System.out.println(
            "CAMBIO A FASE: " + phase
        );
    }


    public void update(float delta, ControllerBullets bulletsEnemy, Vector2 playerPosition) {

        if (!alive) {
            return;
        }
        updateCollision();

        phaseTimer += delta;
        shotTimer += delta;
        patternTimer += delta;

        if (phaseTimer >= phaseDuration) {
            nextPhase();
            return;
        }


        switch (phase) {

            // FASE 1 - TARGETED

            case 1:

                targetedTimer += delta;
                spreadTimer += delta;

                if (spreadTimer >= spreadInterval) {
                    spreadTimer = 0f;
                    spreadDirection = !spreadDirection;
                    float spreadOffset = spreadDirection ? 10f : -10f;
                    ShotPatterns.spread(bulletsEnemy, new Vector2(x, y), playerPosition, spreadBullets, spreadAngle, spreadBulletSpeed, spreadOffset);
                }

                if (targetedTimer >= targetedInterval) {
                    targetedTimer = 0f;
                    ShotPatterns.targeted(bulletsEnemy, new Vector2(x, y), playerPosition, 7f);
                }
                break;


            // FASE 2 - RADIAL + SPREAD

            case 2:
                updatePhaseTwo(delta, bulletsEnemy, playerPosition
                );

                break;


            // FASE 3 - SPIRAL

            case 3:
                updateSpiral(delta, bulletsEnemy);
                break;
        }
    }

    private void updateCollision() {

        bossCollision.set(x, y, 0.6f);
    }

    public boolean isCollidingWithBullet(ClassBullets bullet) {

        if (!alive) {
            return false;
        }

        return Intersector.overlaps(bossCollision, bullet.collision);
    }


    private void updatePhaseTwo(
        float delta,
        ControllerBullets bulletsEnemy,
        Vector2 playerPosition) {

        phaseTwoTimer += delta;

        updateRadialBurst(
            delta,
            bulletsEnemy
        );

        if (radialRoundsRemaining > 0) {
            return;
        }

        if (phaseTwoTimer >= 1.199f) {

            phaseTwoTimer = 0f;
            phaseTwoSpread = !phaseTwoSpread;

            if (phaseTwoSpread) {

                ShotPatterns.spread(
                    bulletsEnemy,
                    new Vector2(x, y),
                    playerPosition,
                    9,
                    10f,
                    5f
                );

            } else {

                radialRoundsRemaining = radialRounds;
                radialRoundTimer = 0f;

                fireRadialRound(
                    bulletsEnemy
                );

                radialRoundsRemaining--;
            }
        }
    }



    private void updateRadialBurst(float delta, ControllerBullets bulletsEnemy) {

        if (radialRoundsRemaining > 0) {
            radialRoundTimer += delta;
            if (radialRoundTimer >= radialInterval) {
                radialRoundTimer = 0f;
                fireRadialRound(bulletsEnemy);
                radialRoundsRemaining--;
            }
        }
    }

    private void fireRadialRound(ControllerBullets bulletsEnemy) {

        ShotPatterns.radialRound(bulletsEnemy, new Vector2(x, y), radialDirections, 5f, radialAngleOffset);
        radialAngleOffset += radialRotation;
    }


    private void updateSpiral(float delta, ControllerBullets bulletsEnemy) {

        if (shotTimer >= spiralInterval) {
            shotTimer = 0f;
            ShotPatterns.spiral(bulletsEnemy, new Vector2(x, y), spiralAngleA, spiralAngleB, spiralBulletSpeed
            );
        }

        spiralAngleA += spiralAngularSpeed * delta;
        spiralAngleB -= spiralAngularSpeed * delta;

        if (spiralAngleA >= 360f) {
            spiralAngleA -= 360f;
        }

        if (spiralAngleB < 0f) {
            spiralAngleB += 360f;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public int getPhase() {
        return phase;
    }

    public float getPhaseHealth() {
        return phaseHealth;
    }

    public float getPhaseMaxHealth() {
        return phaseMaxHealth;
    }

    public float getHealthPercentage() {

        if (maxHealth <= 0f) {
            return 0f;
        }
        return health / maxHealth;
    }

    public float getPhaseTimer() {
        return phaseTimer;
    }

    public float getPhaseDuration() {
        return phaseDuration;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
