package com.example.myapplication.todo.viewModel;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import com.example.myapplication.todo.database.TodoDB;
import com.example.myapplication.todo.database.TodoDBDao;
import com.example.myapplication.todo.database.TodoDatabase;

import java.util.List;

public class TodoRepository {
    private TodoDBDao todoDao;
    private LiveData<List<TodoDB>> allTodos;

    public TodoRepository(Application application) {
        TodoDatabase database = TodoDatabase.getInstance(application);
        todoDao = database.todoDao();
        allTodos = (LiveData<List<TodoDB>>) todoDao.getAllTodos();
    }

    public LiveData<List<TodoDB>> getAllTodos() {
        return allTodos;
    }

    public void insertFixture(TodoDB todo) {
        new InsertFixtureAsyncTask(todoDao).execute(todo);
    }

    public void insertFixtures(List<TodoDB> todoList) {
        new InsertFixturesAsyncTask(todoDao).execute(todoList);
    }

    public void deleteOldTodos(String currentTime) {
        new DeleteOldTodosAsyncTask(todoDao).execute(currentTime);
    }

    public void deleteFixture(TodoDB todo) {
        new DeleteFixtureAsyncTask(todoDao).execute(todo);
    }

    private static class InsertFixtureAsyncTask extends AsyncTask<TodoDB, Void, Void> {
        private TodoDBDao todoDao;

        private InsertFixtureAsyncTask(TodoDBDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(TodoDB... todos) {
            todoDao.insertFixture(todos[0]);
            return null;
        }
    }

    private static class InsertFixturesAsyncTask extends AsyncTask<List<TodoDB>, Void, Void> {
        private TodoDBDao todoDao;

        private InsertFixturesAsyncTask(TodoDBDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(List<TodoDB>... todoLists) {
            todoDao.insertFixtures(todoLists[0]);
            return null;
        }
    }

    private static class DeleteOldTodosAsyncTask extends AsyncTask<String, Void, Void> {
        private TodoDBDao todoDao;

        private DeleteOldTodosAsyncTask(TodoDBDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(String... currentTimes) {
            todoDao.deleteOldTodos(currentTimes[0]);
            return null;
        }
    }

    private static class DeleteFixtureAsyncTask extends AsyncTask<TodoDB, Void, Void> {
        private TodoDBDao todoDao;

        private DeleteFixtureAsyncTask(TodoDBDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(TodoDB... todos) {
            todoDao.deleteFixture(todos[0]);
            return null;
        }
    }
}
