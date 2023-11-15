package com.example.myapplication.todo;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class TodoActivity extends AppCompatActivity{

    private static final String TAG="Todo_Activity";
    private RecyclerView rvTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        rvTodo=findViewById(R.id.rvTodo);

        SingleAdapter adapter=new SingleAdapter();
        adapter.addItem(new TodoInput("input"));

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        rvTodo.setLayoutManager(layoutManager);
        rvTodo.setAdapter(adapter);
    }
}
