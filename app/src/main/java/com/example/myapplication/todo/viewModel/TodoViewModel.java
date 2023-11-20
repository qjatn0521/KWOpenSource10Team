package com.example.myapplication.todo.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.sports.model.TeamResponse;
import com.example.myapplication.todo.database.TodoDB;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TodoViewModel extends AndroidViewModel {
    private MutableLiveData<List<TodoDB>> allTodos = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> loadingTodoList = new MutableLiveData<>();

    public TodoViewModel(Application application) {
        super(application);
    }

    public LiveData<List<TodoDB>> getAllTodos() {
        return allTodos;
    }


    public void deleteFixture(TodoDB todo) {
        deleteFixture(todo);
    }

    public LiveData<List<TodoDB>> getAllTodosFromRepository() {
        return getAllTodos();
    }
}
