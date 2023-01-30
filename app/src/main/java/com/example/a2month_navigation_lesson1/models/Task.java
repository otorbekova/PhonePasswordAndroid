package com.example.a2month_navigation_lesson1.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity // указать что это таблица
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true) //указать что это поле айди основной ключ autoGenerate-это автомат
    private long id;
    private String title;
    public String desk;
    private String docId;
    public Task() {
    }

    public Task(String title, String desk) {
        this.title = title;
        this.desk = desk;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public  String getDesc(){
        return desk;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public  void setDesk(String desc){
        this.desk=desc;
    }

    public String getDocId() {
        return docId;
    }
    public void setDocId(String docId){
        this.docId=docId;
    }
}
