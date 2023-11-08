package com.example.myapplication.todo.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.todo.database.TodoDB;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository repository;
    private LiveData<List<TodoDB>> allTodos;

    public TodoViewModel(Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTodos = repository.getAllTodos();
    }

    public LiveData<List<TodoDB>> getAllTodos() {
        return allTodos;
    }

    public void insertFixture(TodoDB todo) {
        repository.insertFixture(todo);
    }

    public void insertFixtures(List<TodoDB> todoList) {
        repository.insertFixtures(todoList);
    }

    public void deleteOldTodos(String currentTime) {
        repository.deleteOldTodos(currentTime);
    }

    public void deleteFixture(TodoDB todo) {
        repository.deleteFixture(todo);
    }
}
