package com.example.myapplication.alarm;
import static com.example.myapplication.weather.position.TransCoordinate.TO_GRID;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.media.MediaPlayer;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FragNoti;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.sports.adapter.TodaySchedule;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.myapplication.weather.WeatherActivity;
import com.example.myapplication.weather.api.UltraSrtNcstAPI;
import com.example.myapplication.weather.api.VillageFcstAPI;
import com.example.myapplication.weather.position.LatXLngY;
import com.example.myapplication.weather.position.TransCoordinate;
import com.example.myapplication.weather.time.CurrentTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragAlarmCalled extends Activity {
    private TodaySchedule adapter =new TodaySchedule();
    private  RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
    private RecyclerView sportsRv;
    private MediaPlayer mediaPlayer;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private CurrentTime currentTime;
    private TransCoordinate trans;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_alarm_called);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Button btnExit = findViewById(R.id.GoOffButton);
        playAlarmSound();

        //알람 다음날 재설정
        int id = getIntent().getIntExtra("id", 0); // 0 is the default value if "id" is not found
        String timeValue = getIntent().getStringExtra("time");
        Intent receiverIntent = new Intent(this, AlarmReceiver.class);
        receiverIntent.putExtra("id", id);
        receiverIntent.putExtra("time", timeValue);
        long alarmTimeMillis = calculateAlarmTime(timeValue);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);

        //시간 설정
        TextView timeTv = findViewById(R.id.alarm_time_tv);
        timeTv.setText(convert24HourTo12Hour(timeValue));


        //스포츠 일정 불러오기
        sportsRv = findViewById(R.id.alarm_sports_rv);
        new QueryDatabaseTask().execute();

        TextView tmp = findViewById(R.id.weatherAlarmTMP);
        TextView reh = findViewById(R.id.weatherAlarmREHText);
        TextView wsd = findViewById(R.id.weatherAlarmWSDText);
        ImageView skyImage = findViewById(R.id.weatherAlarmImage);

        currentTime = new CurrentTime();

        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude(); // 위도
                double latitude = location.getLatitude(); // 경도
            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FragAlarmCalled.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            // 가장최근 위치정보 가져오기
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                System.out.println("latitude! = " + latitude);
                System.out.println("longitude! = " + longitude);
            }

            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }

        trans = new TransCoordinate();
        LatXLngY temp = trans.convertGRID_GPS(TO_GRID, latitude, longitude);
        Log.e(">>", "x = " + temp.x + ", y = " + temp.y);
        String nx = String.format("%.0f", temp.x);
        String ny = String.format("%.0f", temp.y);

        UltraSrtNcstAPI ulweather = new UltraSrtNcstAPI(currentTime.getBaseUlDate(), currentTime.getBaseUlTime(), nx, ny);

        /** API를 받아오는 Thread */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ulweather.getAPI();

                    // 초단기실황
                    List<String> ucg = ulweather.getCategory();
                    List<String> uob = ulweather.getObsrValue();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 초단기실황 데이터
                            String T1H, RN1, REH, PTY, VEC, WSD;
                            List<Integer> TmpList = new ArrayList<>();

                            for (int i = 0; i < ucg.size(); i++) {
                                // 초단기실황에서 기온
                                if (ucg.get(i).equals("PTY")) {
                                    PTY = uob.get(i);

                                    if (PTY.equals("0")){
                                        skyImage.setImageResource(R.drawable.weather_sunny_icon);
                                    }
                                    else if (PTY.equals("1")|| PTY.equals("2") || PTY.equals("5") || PTY.equals("6")){
                                        skyImage.setImageResource(R.drawable.weather_rain_icon);
                                    }
                                    else if (PTY.equals("3") || PTY.equals("7")){
                                        skyImage.setImageResource(R.drawable.weather_snow_icon);
                                    }
                                }
                                if (ucg.get(i).equals("T1H")) {
                                    T1H = uob.get(i);
                                    tmp.setText(T1H + "℃");
                                }
                                if (ucg.get(i).equals("REH")) {
                                    REH = uob.get(i);
                                    reh.setText(REH + "%");
                                }
                                if (ucg.get(i).equals("WSD")) {
                                    WSD = uob.get(i);
                                    wsd.setText(WSD + "m/s");
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("onCreateThread = " + e);
                }
            }
        }).start();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopAlarmSound();
                finish();
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
        long alarmTimeMillis = 0;
        if (alarmHour < currentHour || (alarmHour == currentHour && alarmMinute <= currentMinute)) {
            // 알람 시간이 현재 시간보다 이전이라면 다음날로 설정
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmTimeMillis = calendar.getTimeInMillis();

        return alarmTimeMillis;
    }
    public static String convert24HourTo12Hour(String inputTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm",new Locale("ko", "KR"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("a h:mm", new Locale("ko", "KR"));

        try {
            Date date = inputFormat.parse(inputTime);
            if (date != null) {
                return outputFormat.format(date).replaceAll("AM","오전").replaceAll("PM","오후");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ""; // 변환 실패 시 빈 문자열 반환 또는 예외 처리
    }

    private class QueryDatabaseTask extends AsyncTask<Void, Void, List<FixtureDB>> {
        @Override
        protected List<FixtureDB> doInBackground(Void... voids) {
            // 데이터베이스에서 FixtureDB 정보 가져오기
            FixtureDBDao fixtureDao = FixtureDatabase.getInstance(FragAlarmCalled.this).fixtureDao();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // 현재 날짜 및 시간을 가져옵니다.
            String currentTime = dateFormat.format(new Date());
            return fixtureDao.getFixturesForToday(currentTime);
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
    // 알람 노래를 재생하는 함수
    private void playAlarmSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound); // 알람 소리 파일을 raw 폴더에 넣고 사용
        mediaPlayer.setLooping(true); // 반복 재생 설정
        mediaPlayer.start(); // 재생 시작
    }

    // 알람 노래를 멈추는 함수
    private void stopAlarmSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // 노래 정지
            mediaPlayer.release(); // 미디어 플레이어 리소스 해제
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarmSound(); // Activity가 종료될 때 노래 정지 및 리소스 해제
    }
}
