package com.utilClasses;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ClassBullets implements Poolable {
    public Vector2 velocity = new Vector2();
    public Vector2 position = new Vector2();
    public float radio;
    public boolean state = false;
    public Rectangle collision = new Rectangle();



    public void Start(float x, float y, float radio, float velX, float velY) {
        position.set(x, y);
        velocity.set(velX, velY);
        this.radio = radio;
        collision.set(x, y, radio / 16f, radio / 6f);
        state = true;
    }

    public void update(float delta){
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        collision.setPosition(position.x , position.y);
    }


    @Override
    public void reset() {
        position.set(0,0);
        velocity.set(0,0);
        state = false;
    }
}
