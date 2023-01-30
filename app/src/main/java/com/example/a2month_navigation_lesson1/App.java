package com.example.a2month_navigation_lesson1;

import android.app.Application;

import androidx.multidex.MultiDexApplication;
import androidx.room.Room;

import com.example.a2month_navigation_lesson1.room.AppDatabase;


public class App extends MultiDexApplication {
    public  static App instance ;
    private AppDatabase database;

    @Override
public void onCreate() {
        super.onCreate();
        instance=this;
        database= Room.databaseBuilder(this, AppDatabase.class,"mydatabase") // в телефоне саздает файл
                .allowMainThreadQueries()
                .build();// разрешить для записи для маленьких данных
    }
    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
