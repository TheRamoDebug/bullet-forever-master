package com.utilClasses;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ShotPatterns {

    private static final float BULLET_SIZE = 3f;

    private ShotPatterns() {
    }

    public static void targeted(ControllerBullets c, Vector2 origin, Vector2 playerPosition, float bulletSpeed) {
        Vector2 direction = new Vector2(playerPosition).sub(origin);
        direction.nor();
        c.shot(origin.x, origin.y, BULLET_SIZE, direction.x * bulletSpeed, direction.y * bulletSpeed);
    }

    public static void spread(ControllerBullets c, Vector2 origin, Vector2 playerPosition, int bullets, float angle, float bulletSpeed) {
        Vector2 direction = new Vector2(playerPosition).sub(origin);
        direction.nor();

        float centerAngle = MathUtils.atan2Deg(direction.y, direction.x);
        float totalAngle = angle * (bullets - 1);
        float startAngle = centerAngle - totalAngle / 2f;

        for (int i = 0; i < bullets; i++) {
            float currentAngle = startAngle + i * angle;
            float velX = MathUtils.cosDeg(currentAngle) * bulletSpeed;
            float velY = MathUtils.sinDeg(currentAngle) * bulletSpeed;
            c.shot(origin.x, origin.y, BULLET_SIZE, velX, velY);
        }
    }

    //sobrecarga

    public static void spread(ControllerBullets c, Vector2 origin, Vector2 playerPosition, int bullets, float angle, float bulletSpeed, float offsetAngle) {
        Vector2 direction = new Vector2(playerPosition).sub(origin);
        direction.nor();

        float centerAngle = MathUtils.atan2Deg(direction.y, direction.x) + offsetAngle;
        float totalAngle = angle * (bullets - 1);
        float startAngle = centerAngle - totalAngle / 2f;

        for (int i = 0; i < bullets; i++) {
            float currentAngle = startAngle + i * angle;
            float velX = MathUtils.cosDeg(currentAngle) * bulletSpeed;
            float velY = MathUtils.sinDeg(currentAngle) * bulletSpeed;

            c.shot(origin.x, origin.y, BULLET_SIZE, velX, velY);
        }
    }

    public static void radialRound(ControllerBullets c, Vector2 origin, int directions, float bulletSpeed) {

        for (int i = 0; i < directions; i++) {

            float angle = (360f / directions) * i;

            float velX = MathUtils.cosDeg(angle) * bulletSpeed;

            float velY = MathUtils.sinDeg(angle) * bulletSpeed;

            c.shot(origin.x, origin.y, BULLET_SIZE, velX, velY);
        }
    }

    //sobrecarga

    public static void radialRound(ControllerBullets c, Vector2 origin, int directions, float bulletSpeed, float angleOffset) {

        for (int i = 0; i < directions; i++) {
            float angle = (360f / directions) * i + angleOffset;
            float velX = MathUtils.cosDeg(angle) * bulletSpeed;
            float velY = MathUtils.sinDeg(angle) * bulletSpeed;
            c.shot(origin.x, origin.y, BULLET_SIZE, velX, velY);
        }
    }

    public static void spiral(ControllerBullets c, Vector2 origin, float angleA, float angleB, float bulletSpeed) {

        float velXA = MathUtils.cosDeg(angleA) * bulletSpeed;

        float velYA = MathUtils.sinDeg(angleA) * bulletSpeed;

        c.shot(origin.x, origin.y, BULLET_SIZE, velXA, velYA);

        float velXB =
            MathUtils.cosDeg(angleB) * bulletSpeed;

        float velYB =
            MathUtils.sinDeg(angleB) * bulletSpeed;

        c.shot(origin.x, origin.y, BULLET_SIZE, velXB, velYB);
    }
}
