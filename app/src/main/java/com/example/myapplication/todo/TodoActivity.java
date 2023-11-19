package com.example.myapplication.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {

    private ArrayList<String> todoList;
    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private EditText todoInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        //할 일 목록 및 recyclerview 초기화
        todoList = new ArrayList<>();
        recyclerView = findViewById(R.id.todoRecyclerView);
        adapter = new TodoAdapter(todoList);

        //레이아웃 매니저 및 어댑터 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //EditText 초기화
        todoInput = findViewById(R.id.todoInput);
    }

    //버튼 기능
    public void addTask(View view) {
        String task = todoInput.getText().toString(); //EditText에서 입력 가져옴
        if (!task.isEmpty()) {
            todoList.add(task); //목록에 추가
            adapter.notifyDataSetChanged();
            todoInput.getText().clear();
        }
    }
}
