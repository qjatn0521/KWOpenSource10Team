package com.example.myapplication.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.R;

import java.util.Arrays;
import java.util.List;

public class TodoActivity extends AppCompatActivity{

    private static final String TAG="Todo_Activity";
    private Context mContext=TodoActivity.this;
    private ListView listView;
    private SingleAdapter adapter;

    List<String> items=Arrays.asList("할 일 1", "할 일 2", "할 일 3", "할 일 4", "할 일 5");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        adapter=new SingleAdapter();
        listView=findViewById(R.id.list_view);

        adapter.addItems(items);

        listView.setAdapter(adapter);
    }
}
