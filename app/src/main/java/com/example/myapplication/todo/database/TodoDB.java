package com.example.myapplication.todo.database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class TodoDB {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public boolean checked = false;
    public String checkedTime;
    public String todoText;

}