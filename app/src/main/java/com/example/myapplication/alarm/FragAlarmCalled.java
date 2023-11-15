package com.example.myapplication.alarm;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FragNoti;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.sports.adapter.TodaySchedule;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragAlarmCalled extends Activity {
    private TodaySchedule adapter =new TodaySchedule();
    private  RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
    private RecyclerView sportsRv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_alarm_called);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Button btnExit = findViewById(R.id.GoOffButton);

        //알람 다음날 재설정
        int id = getIntent().getIntExtra("id", 0); // 0 is the default value if "id" is not found
        String timeValue = getIntent().getStringExtra("time");
        Intent receiverIntent = new Intent(this, AlarmReceiver.class);
        receiverIntent.putExtra("id", id);
        receiverIntent.putExtra("time", timeValue);
        long alarmTimeMillis = calculateAlarmTime(timeValue);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+alarmTimeMillis, pendingIntent);


        //스포츠 일정 불러오기
        sportsRv = findViewById(R.id.alarm_sports_rv);
        new QueryDatabaseTask().execute();


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

    }

    private long calculateAlarmTime(String alarmTime) {
        // 현재 시간 계산
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // 입력된 알람 시간 파싱
        String[] timeComponents = alarmTime.split(":");
        int alarmHour = Integer.parseInt(timeComponents[0].replaceAll("[^0-9]", ""));
        int alarmMinute = Integer.parseInt(timeComponents[1].replaceAll("[^0-9]", ""));

        if (alarmTime.contains("오후") && alarmHour < 12) {
            alarmHour += 12;
        }

        // 알람 시간을 밀리초로 변환
        long currentTimeMillis = System.currentTimeMillis();
        long alarmTimeMillis = 0;
        if (alarmHour < currentHour || (alarmHour == currentHour && alarmMinute <= currentMinute)) {
            // 알람 시간이 현재 시간보다 이전이라면 다음날로 설정
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        calendar.set(Calendar.SECOND, 0);
        alarmTimeMillis = calendar.getTimeInMillis() - currentTimeMillis;

        return alarmTimeMillis;
    }
    private class QueryDatabaseTask extends AsyncTask<Void, Void, List<FixtureDB>> {
        @Override
        protected List<FixtureDB> doInBackground(Void... voids) {
            // 데이터베이스에서 FixtureDB 정보 가져오기
            FixtureDBDao fixtureDao = FixtureDatabase.getInstance(FragAlarmCalled.this).fixtureDao();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // 현재 날짜 및 시간을 가져옵니다.
            String currentTime = dateFormat.format(new Date());
            return fixtureDao.getEarliestFixtureAndSameDateFixtures(currentTime);
        }

        @Override
        protected void onPostExecute(List<FixtureDB> fixtures) {
            // 데이터베이스 쿼리 결과를 처리하고 UI 업데이트
            if(fixtures!=null&&!fixtures.isEmpty()) {
                for(FixtureDB data : fixtures) {
                    adapter.addItem(data);
                }
                sportsRv.setLayoutManager(layoutManager);
                sportsRv.setAdapter(adapter);
            }
        }
    }
}
