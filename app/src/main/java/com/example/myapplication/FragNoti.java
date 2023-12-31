package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.alarm.FragAlarmCalled;
import com.example.myapplication.databinding.FragNotiBinding;
import com.example.myapplication.sports.SportsActivity;
import com.example.myapplication.sports.adapter.TodaySchedule;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;
import com.example.myapplication.todo.TodoActivity;
import com.example.myapplication.todo.TodoAdapter;
import com.example.myapplication.todo.database.TodoDB;
import com.example.myapplication.todo.database.TodoDBDao;
import com.example.myapplication.todo.database.TodoDatabase;
import com.example.myapplication.weather.WeatherActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragNoti extends Fragment {

    private FragNotiBinding binding;
    private String currentTime;
    private TodaySchedule adapter =new TodaySchedule();
    private TodoAdapter adapter2 = new TodoAdapter(new ArrayList<>());
    private  RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
    private RecyclerView rv;
    private RecyclerView todosRv;
    private TextView loadingSprots;
    private TextView loadingTodo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragNotiBinding.inflate((getLayoutInflater()));


        rv = binding.recyclerView;
        todosRv = binding.recyclerView2;
        loadingSprots = binding.loadingSports;
        loadingTodo = binding.loadingTodo;
        binding.btnSettingSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SportsActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSettingWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WeatherActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSettingTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TodoActivity.class);
                startActivity(intent);
            }
        });

        // 스포츠 그날 일정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 현재 날짜 및 시간을 가져옵니다.
        currentTime = dateFormat.format(new Date());


        new QueryDatabaseTask().execute();
        new getTodos().execute();
        //데이터 베이스 연결

        return binding.getRoot();
    }
    private class QueryDatabaseTask extends AsyncTask<Void, Void, List<FixtureDB>> {
        @Override
        protected List<FixtureDB> doInBackground(Void... voids) {
            // 데이터베이스에서 FixtureDB 정보 가져오기
            FixtureDBDao fixtureDao = FixtureDatabase.getInstance(getContext()).fixtureDao();

            return fixtureDao.getEarliestFixtureAndSameDateFixtures(currentTime);
        }

        @Override
        protected void onPostExecute(List<FixtureDB> fixtures) {
            // 데이터베이스 쿼리 결과를 처리하고 UI 업데이트
            if(fixtures!=null&&!fixtures.isEmpty()) {
                for(FixtureDB data : fixtures) {
                    adapter.addItem(data);
                }
                String[] todayString = fixtures.get(0).dateString.split("-");
                binding.todayTv.setVisibility(View.VISIBLE);
                binding.todayTv.setText("다음 경기 : "+todayString[1]+"월 "+todayString[2]+"일");
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
            }
            loadingSprots.setVisibility(View.GONE);
        }
    }

    private class getTodos extends AsyncTask<Void, Void, List<TodoDB>> {
        @Override
        protected List<TodoDB> doInBackground(Void... voids) {
            // 데이터베이스에서 FixtureDB 정보 가져오기
            TodoDBDao todoDBDao = TodoDatabase.getInstance(getContext()).todoDao();
            // 현재 날짜 및 시간을 가져옵니다.
            return todoDBDao.getAllTodos();
        }

        @Override
        protected void onPostExecute(List<TodoDB> fixtures) {
            // 데이터베이스 쿼리 결과를 처리하고 UI 업데이트
            if(fixtures!=null&&!fixtures.isEmpty()) {
                for(TodoDB data : fixtures) {
                    adapter2.addItem(data);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                todosRv.setLayoutManager(layoutManager);
                todosRv.setAdapter(adapter2);
            }
            loadingTodo.setVisibility(View.GONE);
        }
    }

}
