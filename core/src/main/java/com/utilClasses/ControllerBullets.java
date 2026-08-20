package com.utilClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;



public class ControllerBullets {

    public final Array<ClassBullets> balasActivas = new Array<>();

    private static final float BULLET_ORIGIN_X = 0.08f;
    private static final float BULLET_ORIGIN_Y = 0.175f;
    private static final float BULLET_WIDTH = 0.16f;
    private static final float BULLET_HEIGHT = 0.35f;


    public final Pool<ClassBullets> balaPool = new Pool<>() {
        @Override
        protected ClassBullets newObject() {
            return new ClassBullets();
        }
    };

    public void shot(float x, float y, float radio,float velX, float velY) {
        ClassBullets newBullet = balaPool.obtain();
        newBullet.Start(x, y, radio,velX, velY);
        balasActivas.add(newBullet);
    }


    public void updateScreen(float delta, float widthScreen, float heightScreen ){

        for (int i = balasActivas.size - 1; i >= 0; i--){
            ClassBullets b = balasActivas.get(i);
            b.update(delta);

            if (b.position.x < -1 || b.position.x > widthScreen + 1 ||
                b.position.y < -1 || b.position.y > heightScreen + 1){

                balasActivas.removeIndex(i);
                balaPool.free(b);
            }
        }
    }



    public void drawBulletsAndCollide(SpriteBatch c, Texture bullet, ControllerEnemies controllerEnemy, StatsClass statsClass){
        for (int i = balasActivas.size - 1; i >= 0; i--) {
            ClassBullets b = balasActivas.get(i);

            if(controllerEnemy.isCollidingWithEnemy(b, statsClass)){
                balasActivas.removeIndex(i);
                balaPool.free(b);
            }


            if (b.state) {
                float angle = b.velocity.angleDeg() - 90f;
                TextureRegion region = new TextureRegion(bullet);
                c.draw(region, b.position.x, b.position.y, BULLET_ORIGIN_X, BULLET_ORIGIN_Y, BULLET_WIDTH, BULLET_HEIGHT, 1f, 1f, angle);
            }
        }
    }



    public void drawBulletsEnemies(SpriteBatch c, Texture bullet, Circle collisionPlayer){
        for (int i = balasActivas.size - 1; i >= 0; i--) {
            ClassBullets b = balasActivas.get(i);

            if (Intersector.overlaps(collisionPlayer, b.collision)) {
                Player.setLessHealth();
                MasterClass.planeDamage();
                b.state = false;
            }

            if (b.state) {
                float angle = b.velocity.angleDeg() - 90f;
                TextureRegion region = new TextureRegion(bullet);
                c.draw(region, b.position.x, b.position.y, BULLET_ORIGIN_X, BULLET_ORIGIN_Y, BULLET_WIDTH, BULLET_HEIGHT, 1f, 1f, angle);
            }
            else {
                balasActivas.removeIndex(i);
                balaPool.free(b);
            }
        }
    }
}
