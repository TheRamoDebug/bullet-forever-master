package com.utilClasses;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MovementsEnemies {


    public static Vector2 movementType1(Vector2 positionEnemy, float delta, float oscillation, float velocityEnemy, float fixedOscillation, boolean isMovementDirection){

        if (positionEnemy.x > 14f) {
            isMovementDirection = false;
        } else if (positionEnemy.x < 0f) {
            isMovementDirection = true;
        }

        if (isMovementDirection) {
            positionEnemy.x += velocityEnemy * delta;
        }

        if (!isMovementDirection) {
            positionEnemy.x -= velocityEnemy * delta;
        }

        float targetY = (MathUtils.sin(fixedOscillation + oscillation * velocityEnemy) * 1.5f) + 5f;
        positionEnemy.y += (targetY - positionEnemy.y) * 3f * delta;

        return positionEnemy;
    }

    public static Vector2 movementType2(Vector2 positionEnemy, float delta, float oscillation, float velocityEnemy, float fixedOscillation, boolean isMovementDirection){


        if (isMovementDirection) {
            positionEnemy.x += velocityEnemy * delta;
        }

        if (!isMovementDirection) {
            positionEnemy.x -= velocityEnemy * delta;
        }

        positionEnemy.y = (MathUtils.sin(fixedOscillation + oscillation * velocityEnemy * 4)) + 5f;

        return positionEnemy;
    }

    public static Vector2 movementType3(Vector2 positionEnemy, float delta, float oscillation, float velocityEnemy, float fixedOscillation, boolean isMovementDirection){


        if (isMovementDirection ) {
            positionEnemy.x += velocityEnemy * delta;
        }

        if (!isMovementDirection) {
            positionEnemy.x -= velocityEnemy * delta;
        }

        if (MathUtils.sin(oscillation + fixedOscillation * 16) < -0.9){

            if (positionEnemy.y < 8f) {
                positionEnemy.y -= 0.5f + 5f;
            }

        } else if (MathUtils.sin(oscillation + fixedOscillation * 16) > 0.9) {
            if (positionEnemy.y > 5f) {
                positionEnemy.y += 0.5f + 5f;
            }
        }


        return positionEnemy;
    }


    public static Vector2 movementType4(Vector2 positionEnemy, float delta, float oscillation, float velocityEnemy, float fixedOscillation, boolean isMovementDirection){


        if (isMovementDirection) {
            positionEnemy.x += velocityEnemy * delta;
        }

        if (!isMovementDirection) {
            positionEnemy.x -= velocityEnemy * delta;
        }
        positionEnemy.y = 7f;

        return positionEnemy;
    }

    public static void zigzagHorizontal(Vector2 positionEnemy, float delta, float speedX, float phase, float globalOscillation, float baseHeight) {
        positionEnemy.x += speedX * delta;
        float targetY = baseHeight + MathUtils.sin(phase + globalOscillation * 2f) * 0.8f;
        positionEnemy.y += (targetY - positionEnemy.y) * 3f * delta;
    }

}

