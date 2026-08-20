package com.utilClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class PlayerAnimation implements Disposable {
    private Texture idle;
    private Texture movementLeft;
    private Texture movementRight;
    private Animation<TextureRegion> animationIdle;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationRight;

    public enum State { IDLE, LEFT, RIGHT }
    private State currentState = State.IDLE;
    private float stateTime = 0f;


    public PlayerAnimation(){
        idle = new Texture("Sprites/idle.png");
        movementLeft = new Texture("Sprites/left.png");
        movementRight = new Texture("Sprites/right.png");


        TextureRegion[][] imageCut = TextureRegion.split(idle,idle.getWidth() / 4, idle.getHeight());
        TextureRegion[][] imageCut2 = TextureRegion.split(movementLeft,idle.getWidth() / 4, idle.getHeight());
        TextureRegion[][] imageCut3 = TextureRegion.split(movementRight,idle.getWidth() / 4, idle.getHeight());

        animationIdle  = new Animation<>(0.15f, imageCut[0]);
        animationLeft  = new Animation<>(0.30f, imageCut3[0]);
        animationRight  = new Animation<>(0.30f, imageCut2[0]);


        animationIdle.setPlayMode(Animation.PlayMode.LOOP);
        animationLeft.setPlayMode(Animation.PlayMode.NORMAL);
        animationRight.setPlayMode(Animation.PlayMode.NORMAL);
    }


    public TextureRegion update(float delta){
        State previousState = currentState;

        TextureRegion textureRegion;

        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            currentState = State.RIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            currentState = State.LEFT;
        } else {
            currentState = State.IDLE;
        }

        if (currentState != previousState) {
            stateTime = 0f;
        } else {
            stateTime += delta;
        }

        textureRegion = switch (currentState) {
            case RIGHT -> animationRight.getKeyFrame(stateTime);
            case LEFT -> animationLeft.getKeyFrame(stateTime);
            default -> animationIdle.getKeyFrame(stateTime);
        };

        return textureRegion;
    }






    @Override
    public void dispose() {
        idle.dispose();
        movementLeft.dispose();
        movementRight.dispose();
    }
}
