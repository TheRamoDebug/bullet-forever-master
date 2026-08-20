package com.utilClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class LevelsController {
    private int wave = 0;
    private boolean waveCreated = false;
    private int wavePhase = 0;
    private float waveTimer = 0f;
    private float enemyInfinite = 0;
    private float contEnemyLevel = 0;
    private float interWaveTimer = 0f;
    private boolean waitingNextWave = false;
    private static final float INTER_WAVE_DELAY = 2f;
    private static final float FIRST_WAVE_DELAY = 5f;

    private static final float WORLD_WIDTH = 16f;
    private static final float WORLD_HEIGHT = 9f;

    private float movementBackground = 0;
    private float oscillation = 0;
    private float cont = -3f;


    public boolean selectLevel(SpriteBatch c, ControllerEnemies ce, ControllerBullets cbEnemy, Texture fondo, Sprite enemySprite, float delta, int level, Vector2 playerPosition){
        switch (level){
            case -1 -> {
                levelminus1(c, ce, cbEnemy, fondo, enemySprite, delta, playerPosition);
                return true;
            }

            case 1 -> {
                return level1(c, ce, cbEnemy, fondo, enemySprite, delta, playerPosition);
            }

            case 2 ->{
                level2(c, ce, cbEnemy, fondo,enemySprite,delta);
            }

            case 3-> {
                level3(c, ce, cbEnemy, fondo,enemySprite,delta);
            }

            case 4-> {
                level4(c, ce, cbEnemy, fondo,enemySprite,delta);
            }

            default -> {

            }
        }
        return true;
    }


    public void BackgroundChange(SpriteBatch c, Texture fondo , float delta){
        movementBackground += delta * 20;

        c.draw(fondo, 0, -movementBackground, WORLD_WIDTH, WORLD_HEIGHT);
        c.draw(fondo, 0, 9f - movementBackground, WORLD_WIDTH, WORLD_HEIGHT);

        if (movementBackground >= 9f) {
            movementBackground = 0;
        }
    }


    //MODO INFINITO
    public void levelminus1(SpriteBatch c, ControllerEnemies ce, ControllerBullets cbEnemy, Texture fondo, Sprite enemySprite, float delta, Vector2 playerPosition){
        BackgroundChange(c, fondo, delta);

        cont += delta;
        oscillation += delta * 0.5f;

        if(cont > 0.2 && enemyInfinite != contEnemyLevel) {

            cont = 0;
            oscillation += 1.5f;

            float x = MathUtils.random(1f, WORLD_WIDTH - 2f);
            float y = WORLD_HEIGHT + 1f;

            ClassEnemy auxiliarEnemy = new ClassEnemy(100, 20, 4, new Vector2(x, y), 1.5f, oscillation, 1, 1, ClassEnemy.ShotPattern.TARGETED, false);

            auxiliarEnemy.targetHeight = 5f;
            ce.addEnemy(auxiliarEnemy);
            contEnemyLevel += 1;
        }
        if(ce.getEnemiesCount() == 0 && cont > 1f) {
            enemyInfinite += 3;
            contEnemyLevel = 0;
        }

        ce.movementEnemies(delta,oscillation, c, enemySprite,cbEnemy, playerPosition);
    }

    //NIVEL 1
    public boolean level1(SpriteBatch c, ControllerEnemies ce, ControllerBullets cbEnemy, Texture fondo, Sprite enemySprite, float delta, Vector2 playerPosition){
        BackgroundChange(c, fondo, delta);

        cont += delta;
        oscillation += delta * 0.5f;
        waveTimer += delta;

        //WAVE 1
        if (wave == 0 && !waveCreated && waveTimer >= FIRST_WAVE_DELAY) {

            float startY = WORLD_HEIGHT + 1f;

            for (int row = 0; row < 4; row++) {
                float y = startY + (3 - row) * 0.9f;

                ClassEnemy enemyLeft = new ClassEnemy(100, 20, 4, new Vector2(7.2f, y), 2, oscillation, 0, 1, ClassEnemy.ShotPattern.TARGETED, true);
                enemyLeft.waveRow = row;
                enemyLeft.targetHeight = 5.5f + row * 0.55f;
                enemyLeft.leaveDelay = 3f;

                ClassEnemy enemyRight = new ClassEnemy(100, 20, 4, new Vector2(8.8f, y), 2, oscillation, 0, 1, ClassEnemy.ShotPattern.TARGETED, true);
                enemyRight.waveRow = row;
                enemyRight.targetHeight = 5.5f + row * 0.55f;
                enemyRight.leaveDelay = 3f;

                ce.addEnemy(enemyLeft);
                ce.addEnemy(enemyRight);
            }

            waveCreated = true;
        }


        //WAVE 2 - PHASE 1
        if (wave == 1 && !waveCreated) {

            float startY = WORLD_HEIGHT + 1f;

            for (int row = 0; row < 3; row++) {
                float y = startY + (2 + row) * 0.9f;

                float leftX = 7.2f - row * 1.5f;
                float rightX = 8.8f + row * 1.5f;

                ClassEnemy enemyLeft = new ClassEnemy(100, 20, 4, new Vector2(leftX, y), 2, oscillation, 0, 1, ClassEnemy.ShotPattern.TARGETED, false);
                ClassEnemy enemyRight = new ClassEnemy(100, 20, 4, new Vector2(rightX, y), 2, oscillation, 0, 1, ClassEnemy.ShotPattern.TARGETED, false);

                enemyLeft.waveRow = row;
                enemyRight.waveRow = row;

                enemyLeft.entryDelay = row * 0.5f;
                enemyRight.entryDelay = row * 0.5f;

                enemyLeft.targetHeight = 4.5f + row * 0.8f;
                enemyRight.targetHeight = 4.5f + row * 0.8f;

                enemyLeft.singleShot = true;
                enemyRight.singleShot = true;

                enemyLeft.leaveDelay = 2f;
                enemyRight.leaveDelay = 2f;

                enemyLeft.shotOrder = row * 2;
                enemyRight.shotOrder = row * 2 + 1;

                ce.addEnemy(enemyLeft);
                ce.addEnemy(enemyRight);
            }

            waveCreated = true;
        }

        //WAVE 2 - PHASE 2
        if (wave == 2 && !waveCreated) {
            float startY = WORLD_HEIGHT + 1f;
            for (int i = 0; i < 15; i++) {
                float x = -3f + i * 1.0f;

                ClassEnemy zigzagEnemy = new ClassEnemy(100, 20, 4, new Vector2(x, startY), 2, oscillation + i * 0.6f, 1, 1, ClassEnemy.ShotPattern.TARGETED, false);
                zigzagEnemy.entryDelay = i * 0.1f;
                zigzagEnemy.targetHeight = 7.2f;
                zigzagEnemy.singleShot = true;
                zigzagEnemy.shotOrder = i;
                zigzagEnemy.useSimpleZigZag = true;
                ce.addEnemy(zigzagEnemy);
            }
            ClassEnemy sideLeft = new ClassEnemy(200, 20, 4, new Vector2(4f,startY), 2, oscillation, 0, 2, ClassEnemy.ShotPattern.RADIAL, false);
            sideLeft.targetHeight = 5f;
            sideLeft.singleShot = true;
            sideLeft.entryDelay = 0f;
            sideLeft.leaveDelay = 2f;
            sideLeft.setShotsRemaining(3);
            sideLeft.setRadialIntensity(18, 1);

            ClassEnemy sideRight = new ClassEnemy(200, 20, 4, new Vector2(13f, startY), 2, oscillation, 0, 2, ClassEnemy.ShotPattern.RADIAL, false);
            sideRight.targetHeight = 5f;
            sideRight.singleShot = true;
            sideRight.entryDelay = 3.5f;
            sideRight.leaveDelay = 1f;
            sideRight.setShotsRemaining(3);
            sideRight.setRadialIntensity(18, 1);

            ce.addEnemy(sideLeft);
            ce.addEnemy(sideRight);

            waveCreated = true;
        }
        //WAVE 2 - PHASE 3

        if(wave == 3 && !waveCreated) {
            Vector2 curveStart = new Vector2(17f, 5f);
            Vector2 curveControl = new Vector2(3f, 3f);
            Vector2 curveEnd = new Vector2(9f, 10f);

            for(int i = 0; i < 9; i++) {
                ClassEnemy curveEnemy = new ClassEnemy(100, 20, 4, new Vector2(17f, 3f), 2, oscillation, 1, 1, ClassEnemy.ShotPattern.TARGETED, false);
                curveEnemy.entryDelay = i * 0.2f;
                curveEnemy.singleShot = true;
                curveEnemy.shotOrder = 0;
                curveEnemy.curvePasses = 3;
                curveEnemy.setCurvePath(curveStart, curveControl, curveEnd);
                ce.addEnemy(curveEnemy);
            }

            for (int row = 0; row < 3; row++) {
                float targetH = 4f + row * 1.5f;
                float leftX = 3f + row * 1.5f;
                float rightX = 13f - row * 1.5f;

                ClassEnemy leftEnemy = new ClassEnemy(100, 20, 4, new Vector2(leftX, WORLD_HEIGHT + 1f), 2, oscillation, 0, 1, ClassEnemy.ShotPattern.RADIAL, false);
                leftEnemy.entryDelay = row * 0.3f;
                leftEnemy.targetHeight = targetH;
                leftEnemy.singleShot = true;
                leftEnemy.shotOrder = row;
                leftEnemy.leaveDelay = 1f;
                leftEnemy.setRadialIntensity(8, 2);
                leftEnemy.setShotsRemaining(3);
                ce.addEnemy(leftEnemy);

                ClassEnemy rightEnemy = new ClassEnemy(100, 20, 4, new Vector2(rightX, WORLD_HEIGHT + 1f), 2, oscillation, 0, 1, ClassEnemy.ShotPattern.RADIAL, false);
                rightEnemy.entryDelay = row * 0.3f;
                rightEnemy.targetHeight = targetH;
                rightEnemy.singleShot = true;
                rightEnemy.shotOrder = row;
                rightEnemy.leaveDelay = 1f;
                rightEnemy.setRadialIntensity(8, 2);
                rightEnemy.setShotsRemaining(3);
                ce.addEnemy(rightEnemy);
            }
            waveCreated = true;

        }

        ce.movementEnemies(delta, oscillation, c, enemySprite, cbEnemy, playerPosition);

        if (waveCreated && ce.getEnemiesCount() == 0) {
            if (!waitingNextWave) {
                waitingNextWave = true;
                interWaveTimer = 0f;
            } else {
                interWaveTimer += delta;
                if (interWaveTimer >= INTER_WAVE_DELAY) {
                    wave++;
                    waveCreated = false;
                    waitingNextWave = false;
                }
            }
        }
        if(wave > 3){
            return false;
        }
        return true;
    }




    //NIVEL 2
    public void level2(SpriteBatch c, ControllerEnemies ce, ControllerBullets cbEnemy, Texture fondo, Sprite enemySprite, float delta){

    }


    //NIVEL 3
    public void level3(SpriteBatch c, ControllerEnemies ce, ControllerBullets cbEnemy, Texture fondo, Sprite enemySprite, float delta){

    }


    //NIVEL 4
    public void level4(SpriteBatch c, ControllerEnemies ce, ControllerBullets cbEnemy, Texture fondo, Sprite enemySprite, float delta){

    }
}
