package com.utilClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class Controls {
    private float timerA = 0;
    private float velocity = 10f;

    public void controlsKeysShots(ControllerBullets bulletsPlayer, Vector2 movementPlayer, Sound shotSound, float delta){
        //controls to shoot oh hell naw
        timerA += delta;
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            if (timerA > 0.15f) {
                bulletsPlayer.shot(movementPlayer.x, movementPlayer.y + 0.2f, 1f, 0f, 12f);
                shotSound.play(0.2f);
                bulletsPlayer.shot(movementPlayer.x + 0.4f, movementPlayer.y + 0.2f, 1f, 0f, 12f);
                shotSound.play(0.2f);
                timerA = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.K)){
            velocity = 2f;
        }else{
            velocity = 8f;
        }
    }




    public Vector2 controlsKeys(Vector2 movement, Float delta){

        if (Gdx.input.isKeyPressed(Input.Keys.D) && movement.x < 14.5){
            movement.x += velocity * delta;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && movement.x > 0){
            movement.x -= velocity * delta;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && movement.y < 8){
            movement.y += (velocity / 2) * delta;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) && movement.y > 0){
            movement.y -= (velocity / 2) * delta;

        }


        return movement;
    }




}
