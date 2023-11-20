package com.example.myapplication.todo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FragNoti;
import com.example.myapplication.R;
import com.example.myapplication.sports.TeamViewModel;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;
import com.example.myapplication.todo.database.TodoDB;
import com.example.myapplication.todo.database.TodoDBDao;
import com.example.myapplication.todo.database.TodoDatabase;
import com.example.myapplication.todo.viewModel.TodoViewModel;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    private ArrayList<TodoDB> todoList;
    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private EditText todoInput;

    private TodoViewModel viewModel;

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
        new getTodoTask().execute();

        //EditText 초기화
        todoInput = findViewById(R.id.todoInput);
        TextView btn = findViewById(R.id.addbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = todoInput.getText().toString(); //EditText에서 입력 가져옴
                if (!task.isEmpty()) {
                    TodoDB tmp = new TodoDB();
                    tmp.checked= false;
                    tmp.todoText = task;
                    new InsertTodoTask().execute(tmp);
                    todoList.add(tmp); //목록에 추가
                    adapter.notifyDataSetChanged();
                    todoInput.getText().clear();
                }
            }
        });

        //뷰 모델 생성
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        ImageView backbtn = findViewById(R.id.back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    class InsertTodoTask extends AsyncTask<TodoDB, Void, Void> {
        @Override
        protected Void doInBackground(TodoDB... todo) {
            if (todo != null) {
                TodoDBDao todoDao = TodoDatabase.getInstance(getBaseContext()).todoDao();
                todoDao.insertTodo(todo[0]);
            }
            Log.d("todo","success");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // doInBackground 작업이 완료된 후에 호출되는 메서드
            // loading.setVisibility(View.GONE);

        }
    }

    private class getTodoTask extends AsyncTask<Void, Void, List<TodoDB>> {
        @Override
        protected List<TodoDB> doInBackground(Void... voids) {
            // 데이터베이스에서 FixtureDB 정보 가져오기
            TodoDBDao todoDao = TodoDatabase.getInstance(getBaseContext()).todoDao();

            return todoDao.getAllTodos();
        }

        @Override
        protected void onPostExecute(List<TodoDB> todos) {
            // 데이터베이스 쿼리 결과를 처리하고 UI 업데이트
            for(TodoDB data : todos) {
                todoList.add(data); //목록에 추가
            }
            adapter.notifyDataSetChanged();
        }
    }


}
