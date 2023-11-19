package com.example.myapplication.alarm;
import static com.example.myapplication.weather.position.TransCoordinate.TO_GRID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
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
                System.exit(0);
            }
        });

    }
}
