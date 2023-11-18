package com.example.myapplication.weather;

import static com.example.myapplication.weather.position.TransCoordinate.TO_GRID;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.weather.api.UltraSrtNcstAPI;
import com.example.myapplication.weather.api.VillageFcstAPI;
import com.example.myapplication.weather.position.LatXLngY;
import com.example.myapplication.weather.position.TransCoordinate;
import com.example.myapplication.weather.time.CurrentTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    String date = "20231114";

    private LocationManager locationManager;
    private LocationListener locationListener;
    private CurrentTime currentTime;
    private TransCoordinate trans;
    double longitude;
    double latitude;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_weather);

        currentTime = new CurrentTime();

        TextView localArea = findViewById(R.id.localArea);
        TextView temperature = findViewById(R.id.temperature);
        TextView highTemp = findViewById(R.id.HighTemp);
        TextView lowTemp = findViewById(R.id.LowTemp);
        TextView sky = findViewById(R.id.Sky);

        localArea.setText("서울 특별시");
        /** 현재 위치를 받아오는 코드 */
        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude(); // 위도
                double latitude = location.getLatitude(); // 경도

                localArea.setText(Double.toString(longitude));
            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
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
        LatXLngY tmp = trans.convertGRID_GPS(TO_GRID, latitude, longitude);
        Log.e(">>", "x = " + tmp.x + ", y = " + tmp.y);
        String nx = String.format("%.0f", tmp.x);
        String ny = String.format("%.0f", tmp.y);

        UltraSrtNcstAPI ulweather = new UltraSrtNcstAPI(currentTime.getBaseUlDate(), currentTime.getBaseUlTime(), nx, ny);
        VillageFcstAPI viweather = new VillageFcstAPI(currentTime.getBaseViDate(), currentTime.getBaseViTime(), nx, ny);
        Log.e(">>", "x = " + nx + ", y = " + ny);

        /** API를 받아오는 Thread */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ulweather.getAPI();
                    viweather.getAPI();

                    // 초단기실황
                    List<String> ucg = ulweather.getCategory();
                    List<String> uob = ulweather.getObsrValue();

                    // 단기예보
                    List<String> vbd = viweather.getBaseDate();
                    List<String> vcg = viweather.getCategory();
                    List<String> vfd = viweather.getFcstDate();
                    List<String> vft = viweather.getFcstTime();
                    List<String> vfv = viweather.getFcstValue();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 초단기실황 데이터
                            String T1H, RN1, REH, PTY, VEC, WSD;
                            List<Integer> TmpList = new ArrayList<>();

                            for (int i = 0; i < ucg.size(); i++) {
                                // 초단기실황에서 기온
                                if (ucg.get(i).equals("T1H")) {
                                    T1H = uob.get(i);
                                    temperature.setText(T1H + "°C");
                                }

                                // 초단기실황에서 습도
                                if (ucg.get(i).equals("REH")) {
                                    RN1 = uob.get(i);
                                }

                                // 초단기실황에서 강수형태
                                if (ucg.get(i).equals("PTY")) {
                                    PTY = uob.get(i);

                                    String str = "null"; // 강수 설명 String
                                    switch (PTY) {
                                        case "0":
                                            str = "강우 없음";
                                            break;
                                        case "1":
                                            str = "비";
                                            break;
                                        case "2":
                                            str = "비/눈";
                                            break;
                                        case "3":
                                            str = "눈";
                                            break;
                                        case "4":
                                            str = "빗방울";
                                            break;
                                        case "5":
                                            str = "빗방울눈날림";
                                            break;
                                        case "6":
                                            str = "눈날림";
                                            break;
                                        }

                                        // lowTemp.setText(str);
                                }

                                    // 풍향
                                    if (ucg.get(i).equals("VEC")) {
                                        VEC = uob.get(i);
                                    }

                                    // 풍속
                                    if (ucg.get(i).equals("WSD")) {
                                        WSD = uob.get(i);
                                    }
                                }

                                int skyCnt = 0, tmpCnt = 0;
                                for (int i = 0; i < vcg.size(); i++) {
                                    // 1시간별 기온 찾기
                                    if (vcg.get(i).equals("TMP") && tmpCnt != 24) {
                                        TmpList.add(Integer.parseInt(vfv.get(i)));
                                        Log.e("addTemp", vfv.get(i));
                                        tmpCnt++;
                                    }
                                    if (vcg.get(i).equals("SKY") && skyCnt == 0) {
                                        skyCnt++;
                                        switch (vfv.get(i)) {
                                            case "1":
                                                sky.setText("맑음");
                                                break;
                                            case "3":
                                                sky.setText("구름많음");
                                                break;
                                            case "4":
                                                sky.setText("흐림");
                                                break;
                                        }

                                    }
                                }

                                if (!TmpList.isEmpty()) {
                                    int max = Collections.max(TmpList);
                                    int min = Collections.min(TmpList);
                                    highTemp.setText(max + "℃");
                                    lowTemp.setText(min + "℃");
                                }
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("onCreateThread = " + e);
                    }
                }
            }).start();
    }
}



