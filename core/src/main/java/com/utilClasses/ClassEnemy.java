package com.utilClasses;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ClassEnemy {
    public float health;
    public int attackProbability;
    public float velocityEnemy;
    public Vector2 positionEnemy;
    public float sizeEnemy;
    public float randomNumber;
    public float oscillation;
    public int typeOfMovement;
    public int typeOfEnemy;
    public boolean useSimpleZigZag = false;
    public float zigzagSpeed = 1.3f;

    public enum ShotPattern {
        NONE,
        TARGETED,
        RADIAL
    }
    public ShotPattern shotPattern;

    public EnemyState currentState = EnemyState.ENTERING;
    public float targetHeight;
    public boolean isMovementDirection = true;
    public int waveRow;
    public float shotTimer = 0f;
    public float entryDelay = 0f;
    public float entryTimer = 0f;
    public boolean singleShot = false;
    public int shotOrder = 0;
    public int shotsRemaining = 1;
    private int initialShots = 1;
    private static final float REPEAT_SHOT_PAUSE = 1.5f;

    // TUTORIAL WAVE (LVL1)//
    public boolean isTutorialWave;
    public boolean hasShot = false;
    public float leaveTimer = 0f;
    private static final float LEAVE_TIMER = 3f;
    public float leaveDelay = LEAVE_TIMER;

    //EXIT MOVEMENT AND CONSTANTS
    private float exitVelocityX = 0f;
    private float exitTargetVelocityX = 0f;
    private static final float EXIT_SMOOTHING = 3f;
    private static final float EXIT_SPEED = 5f;
    private static final float WORLD_HEIGHT = 9f;
    private static final float WORLD_WIDTH = 16f;
    private static final float EXIT_MARGIN = 2f;
    private static final float BULLET_SIZE = 3f;

    public Rectangle collisionEnemy;

    //curve stuff
    public boolean useCurvePath = false;
    private Vector2 curveStart, curveControl,curveEnd;
    private float curveT = 0f;
    private static final float CURVE_DURATION = 4f;
    public int curvePasses = 1;

    //Radial shoot stuff
    private int radialRoundsRemaining = 0;
    private float radialRoundTimer = 0f;
    private static final float RADIAL_ROUND_INTERVAL = 0.1f;
    private int radialRounds = 5;
    private int radialDirections = 16;

    public void setRadialIntensity(int directions, int rounds) {
        this.radialDirections = directions;
        this.radialRounds = rounds;
    }

    public void setShotsRemaining(int shots) {
        this.shotsRemaining = shots;
        this.initialShots = shots;
    }

    public enum EnemyState {
        ENTERING,
        IN_POSITION,
        LEAVING
    }

    public ClassEnemy(float health,
                      int attackProbability,
                      float velocityEnemy, Vector2 positionEnemy,
                      float sizeEnemy, float oscillation, int typeOfMovement,int typeOfEnemy,
                      ShotPattern shotPattern,
                      boolean isTutorialWave) {
        this.health = health;
        this.attackProbability = attackProbability;
        collisionEnemy = new Rectangle();
        collisionEnemy.set(-100, -100, sizeEnemy / 2f, sizeEnemy / 2f);
        this.velocityEnemy = velocityEnemy;
        this.positionEnemy = positionEnemy.cpy();
        this.sizeEnemy = sizeEnemy;
        this.oscillation = oscillation;
        this.typeOfMovement = typeOfMovement;
        this.typeOfEnemy = typeOfEnemy;
        this.shotPattern = shotPattern;
        this.isTutorialWave = isTutorialWave;
    }


    public void newPosition(float delta, float oscillation, SpriteBatch batch, Sprite textureEnemy, int typeOfMovement) {
        if(useCurvePath) {
            updateCurveMovement(delta);
            collisionEnemy.setPosition(positionEnemy.x, positionEnemy.y);
            batch.draw(textureEnemy, positionEnemy.x, positionEnemy.y, sizeEnemy / 2, sizeEnemy / 2);
        return;
        }

        if (currentState == EnemyState.ENTERING) {
            if(entryTimer < entryDelay) {
                entryTimer += delta;
            } else {
                positionEnemy.y -= velocityEnemy * delta;

                if (positionEnemy.y <= targetHeight) {
                    positionEnemy.y = targetHeight;
                    currentState = EnemyState.IN_POSITION;
                }
            }
        } else if (currentState == EnemyState.IN_POSITION) {

            if (positionEnemy.x > 14f) {
                isMovementDirection = false;
            } else if (positionEnemy.x < 0f) {
                isMovementDirection = true;
            }

            if (useSimpleZigZag) {
                MovementsEnemies.zigzagHorizontal(positionEnemy, delta, zigzagSpeed, this.oscillation, oscillation, targetHeight);
            } else {
                switch (typeOfMovement) {
                    case 1 -> {
                        positionEnemy = MovementsEnemies.movementType1(positionEnemy, delta, oscillation, velocityEnemy, this.oscillation, isMovementDirection);
                    }
                    case 2 -> {
                        positionEnemy = MovementsEnemies.movementType2(positionEnemy, delta, oscillation, velocityEnemy, this.oscillation, isMovementDirection);
                    }
                    case 3 -> {
                        positionEnemy = MovementsEnemies.movementType3(positionEnemy, delta, oscillation, velocityEnemy, this.oscillation, isMovementDirection);
                    }
                    default -> {

                    }
                }
            }
        } else if (currentState == EnemyState.LEAVING) {
            updateLeavingMovement(delta);
        }


        collisionEnemy.setPosition(positionEnemy.x, positionEnemy.y);
        batch.draw(textureEnemy, positionEnemy.x, positionEnemy.y, sizeEnemy / 2, sizeEnemy / 2);
    }

    public void setCurvePath(Vector2 start, Vector2 control, Vector2 end) {
        this.useCurvePath = true;
        this.curveStart = start;
        this.curveControl = control;
        this.curveEnd = end;
        this.positionEnemy = start.cpy();
    }

    public void updateCurveMovement(float delta) {
        if(entryTimer < entryDelay) {
            entryTimer += delta;
            return;
        }
        curveT += delta / CURVE_DURATION;

        if (curveT >= 1f) {
            curvePasses--;
            if (curvePasses > 0) {
                curveT = 0f;
                positionEnemy = curveStart.cpy();
            } else {
                curveT = 1f;
                useCurvePath = false;
                currentState = EnemyState.LEAVING;
                startLeaving();
                return;
            }
        }

        float oneMinusT = 1f - curveT;
        positionEnemy.x = oneMinusT * oneMinusT * curveStart.x + 2 * oneMinusT * curveT * curveControl.x + curveT * curveT * curveEnd.x;
        positionEnemy.y = oneMinusT * oneMinusT * curveStart.y + 2 * oneMinusT * curveT * curveControl.y + curveT * curveT * curveEnd.y;
    }

    private void startLeaving() {
        currentState = EnemyState.LEAVING;
        float screenCenter = WORLD_WIDTH / 2f;
        if(positionEnemy.x < screenCenter) {
            exitTargetVelocityX =-EXIT_SPEED;
        } else {
            exitTargetVelocityX = EXIT_SPEED;
        }
    }

    private void updateLeavingMovement(float delta) {
        exitVelocityX += (exitTargetVelocityX - exitVelocityX) * EXIT_SMOOTHING * delta;
        positionEnemy.x += exitVelocityX * delta;
    }

    public boolean isOutOfBounds() {
        return positionEnemy.x < -EXIT_MARGIN || positionEnemy.x > WORLD_WIDTH + EXIT_MARGIN
        || positionEnemy.y < -EXIT_MARGIN || positionEnemy.y > WORLD_HEIGHT + EXIT_MARGIN;
    }


    public void drawEnemyAndShot(ControllerBullets c, float delta, Vector2 playerPosition) {
        updateRadialBurst(delta, c);
        if(singleShot) {
            boolean readyToShoot = useCurvePath ? (curveT >= 0.4f) : (currentState == EnemyState.IN_POSITION);

            if(readyToShoot && !hasShot) {
                shotTimer += delta;
                boolean isFirstShot = (shotsRemaining == initialShots);
                float initialDelay = (shotPattern == ShotPattern.RADIAL) ? (1.5f + shotOrder * 0.5f) : (0.5f + shotOrder * 0.5f);
                float requiredDelay = isFirstShot ? initialDelay : REPEAT_SHOT_PAUSE;

                if(shotTimer >= requiredDelay) {
                    switch(shotPattern) {
                        case TARGETED -> shootAtPlayer(c, playerPosition);
                        case RADIAL -> shootRadial(c);
                        case NONE -> {}
                    }
                    shotTimer = 0f;
                    shotsRemaining--;

                    if (shotsRemaining <= 0) {
                        hasShot = true;
                        leaveTimer = leaveDelay;
                    }
                }
            }
            if (hasShot && !useCurvePath && !useSimpleZigZag) {
                leaveTimer -= delta;
                if (leaveTimer <= 0f) {
                    startLeaving();
                }
            }
            return;
        }
        if (isTutorialWave) {
            if (currentState == EnemyState.IN_POSITION && !hasShot) {
                shotTimer += delta;
                float delay = 1f + waveRow * 0.5f;
                if (shotTimer >= delay) {

                    switch (shotPattern) {
                        case TARGETED -> shootAtPlayer(c, playerPosition);
                        case RADIAL -> shootRadial(c);
                        case NONE -> {
                        }
                    }
                    hasShot = true;
                    leaveTimer = LEAVE_TIMER;
                }
            }
            if(hasShot) {
                leaveTimer -= delta;
                if (leaveTimer <= 0f) {
                    startLeaving();
                }
            }
          return;
        }
        switch (shotPattern) {
            case TARGETED -> {
                if (MathUtils.random() < delta * (1d / attackProbability) * 10f) {
                    shootAtPlayer(c, playerPosition);
                }
            }
            case RADIAL -> {
                if (MathUtils.random() < delta * (1d / attackProbability) * 10f) {
                    shootRadial(c);
                }
            }
            case NONE -> {
            }
        }
    }

    private void shootAtPlayer(ControllerBullets c, Vector2 playerPosition) {
        Vector2 direction = new Vector2(playerPosition).sub(positionEnemy);
        direction.nor();

        float bulletSpeed = 6f;
        c.shot(positionEnemy.x, positionEnemy.y, BULLET_SIZE, direction.x * bulletSpeed, direction.y * bulletSpeed);

    }

    private void shootRadial(ControllerBullets c) {
        fireRadialRound(c);
        radialRoundsRemaining = radialRounds - 1;
        radialRoundTimer = 0f;
    }

    private void fireRadialRound(ControllerBullets c) {
        float bulletSpeed = 4f;

        for (int i = 0; i < radialDirections; i++) {
            float angle = (360f / radialDirections) * i;
            float velX = MathUtils.cosDeg(angle) * bulletSpeed;
            float velY = MathUtils.sinDeg(angle) * bulletSpeed;

            c.shot(positionEnemy.x, positionEnemy.y, BULLET_SIZE, velX, velY);
        }
    }

    private void updateRadialBurst(float delta, ControllerBullets c) {
        if (radialRoundsRemaining > 0) {
            radialRoundTimer += delta;
            if (radialRoundTimer >= RADIAL_ROUND_INTERVAL) {
                radialRoundTimer = 0f;
                fireRadialRound(c);
                radialRoundsRemaining--;
            }
        }
    }

}
