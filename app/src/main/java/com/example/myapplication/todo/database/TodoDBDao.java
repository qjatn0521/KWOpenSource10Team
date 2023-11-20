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
    void insertTodo(TodoDB todo);

    @Query("SELECT * FROM todos")
    List<TodoDB> getAllTodos();

    @Delete
    void deleteFixture(TodoDB todo);

    @Query("UPDATE todos SET `checked` = 1 WHERE id = :todoId")
    void updateCheckStatus(long todoId);

    @Query("UPDATE todos SET `checked` = 0 WHERE id = :todoId")
    void updateCheckStatusToFalse(long todoId);

    @Query("SELECT * FROM todos WHERE `checked` = 0")
    List<TodoDB> getUncheckedTodos();

}