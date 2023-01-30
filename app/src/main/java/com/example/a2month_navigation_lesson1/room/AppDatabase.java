package com.example.a2month_navigation_lesson1.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.a2month_navigation_lesson1.models.Task;

@Database(entities = {Task.class},version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract TasDao taskDao();
}
