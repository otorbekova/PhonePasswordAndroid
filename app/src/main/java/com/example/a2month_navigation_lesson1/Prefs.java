package com.example.a2month_navigation_lesson1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.security.PublicKey;

public class Prefs {
public SharedPreferences preferences;


    public Prefs(Activity activity){
    preferences=activity.getPreferences(Context.MODE_PRIVATE);

}
public void isShown(boolean value){ //board
    preferences.edit().putBoolean("isShown",value).apply();
}
public boolean isShown(){ //main
    return preferences.getBoolean("isShown",false);
}

    public void saveName(String name) {
    preferences.edit().putString("name",name).apply();
    }

    public String getName() {
        return preferences.getString("name","");
    }

    public void saveAvatarUrl(String avatar) {
        preferences.edit().putString("avatar",avatar).apply();
    }

    public String getAvatarUrl() {
        return preferences.getString("avatar","");
    }

}
