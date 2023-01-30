package com.example.a2month_navigation_lesson1.room;

import androidx.contentpager.content.Query;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.a2month_navigation_lesson1.models.Task;

import java.util.List;

@Dao
public interface TasDao {

    @Query("SELECT*FROM task")
    List<Task>getAll(); //polucgaet 1 raz

    @Query("SELECT*FROM task")
    LiveData<List<Task>> getAllLive();// poluchit i slishat

    @Insert
    void insert(Task task);

@Update
    void update(Task task);

@Delete
    void delete(Task task);
}
