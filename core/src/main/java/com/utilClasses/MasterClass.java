package com.utilClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MasterClass{
    private static final Sound shotPlayer = Gdx.audio.newSound(Gdx.files.internal("Sounds/shotSound.mp3"));
    private static final Sound damagePlayer = Gdx.audio.newSound(Gdx.files.internal("Sounds/damage.mp3"));
    private static final Music backgroundMusicGameplay = Gdx.audio.newMusic(Gdx.files.internal("Music/backMusic.mp3"));
    private static final Music backgroundMusicMenu = Gdx.audio.newMusic(Gdx.files.internal("Music/backgroundMenuMusic.mp3"));


    public static Sound getShotPlayer(){return shotPlayer;}

    public static void planeDamage(){
        damagePlayer.play();
    }

    public static void stopMusicMenu(){
        backgroundMusicMenu.stop();
    }

    public static void stopMusicGameplay(){
        backgroundMusicGameplay.stop();
    }


    public static void backgroundMusicGameplay(){
        backgroundMusicGameplay.setLooping(true);
        backgroundMusicGameplay.play();
    }

    public static void backgroundMusicMenu(){
        backgroundMusicMenu.setLooping(true);
        backgroundMusicMenu.setVolume(0.5f);
        backgroundMusicMenu.play();
    }




    public static void dispose() {
        if (backgroundMusicMenu != null) backgroundMusicMenu.dispose();
        if (backgroundMusicGameplay != null) backgroundMusicGameplay.dispose();
    }
}
