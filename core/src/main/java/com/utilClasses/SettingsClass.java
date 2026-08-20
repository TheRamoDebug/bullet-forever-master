package com.utilClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsClass {
    private static final String name = "SaveData";

    public static Preferences getPrefs() {
        return Gdx.app.getPreferences(name);
    }
}
