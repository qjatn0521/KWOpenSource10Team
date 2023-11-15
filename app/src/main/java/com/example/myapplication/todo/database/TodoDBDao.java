package com.example.myapplication.todo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoDBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFixture(TodoDB todo);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFixtures(List<TodoDB> todoList);

    @Query("DELETE FROM todos WHERE checkedTime < :currentTime")
    void deleteOldTodos(String currentTime);

    @Query("SELECT * FROM todos")
    List<TodoDB> getAllTodos();

    @Delete
    void deleteFixture(TodoDB todo);

}