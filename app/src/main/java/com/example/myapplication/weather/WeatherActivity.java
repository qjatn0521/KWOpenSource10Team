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
import android.widget.ImageView;
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
        ImageView image = findViewById(R.id.weatherImage);

        TextView fcstTime0 = findViewById(R.id.fsctTime0);
        TextView fcstTime1 = findViewById(R.id.fsctTime1);
        TextView fcstTime2 = findViewById(R.id.fsctTime2);
        TextView fcstTime3 = findViewById(R.id.fsctTime3);
        TextView fcstTime4 = findViewById(R.id.fsctTime4);
        TextView tempText0 = findViewById(R.id.tempText0);
        TextView tempText1 = findViewById(R.id.tempText1);
        TextView tempText2 = findViewById(R.id.tempText2);
        TextView tempText3 = findViewById(R.id.tempText3);
        TextView tempText4 = findViewById(R.id.tempText4);
        TextView REHText0 = findViewById(R.id.REHText0);
        TextView REHText1 = findViewById(R.id.REHText1);
        TextView REHText2 = findViewById(R.id.REHText2);
        TextView REHText3 = findViewById(R.id.REHText3);
        TextView REHText4 = findViewById(R.id.REHText4);
        TextView WSDText0 = findViewById(R.id.WSDText0);
        TextView WSDText1 = findViewById(R.id.WSDText1);
        TextView WSDText2 = findViewById(R.id.WSDText2);
        TextView WSDText3 = findViewById(R.id.WSDText3);
        TextView WSDText4 = findViewById(R.id.WSDText4);
        ImageView weatherImage0 = findViewById(R.id.weatherImage0);
        ImageView weatherImage1 = findViewById(R.id.weatherImage1);
        ImageView weatherImage2 = findViewById(R.id.weatherImage2);
        ImageView weatherImage3 = findViewById(R.id.weatherImage3);
        ImageView weatherImage4 = findViewById(R.id.weatherImage4);

        localArea.setText("서울 특별시");
        /** 현재 위치를 받아오는 코드 */
        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude(); // 위도
                double latitude = location.getLatitude(); // 경도
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

        Log.e("WeatherActivityParameter", "getBaseUIDate = " + currentTime.getBaseUlDate() + ", getBaseUITime = " + currentTime.getBaseUlTime());
        Log.e("WeatherActivityParameter", "getBaseViDate = " + currentTime.getBaseViDate() + ", getBaseViTime = " + currentTime.getBaseViTime());
        Log.e("WeatherActivityParameter", "nx = " + nx + ", ny = " + ny);

        UltraSrtNcstAPI ulweather = new UltraSrtNcstAPI(currentTime.getBaseUlDate(), currentTime.getBaseUlTime(), "61", "128");
        VillageFcstAPI viweather = new VillageFcstAPI(currentTime.getBaseViDate(), currentTime.getBaseViTime(), "61", "128");
        Log.e(">>", "x = " + nx + ", y = " + ny);
        localArea.setText(String.format("%.1f", latitude) + ", " + String.format("%.1f", longitude));

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
                                }

                                int skyCnt = 0, tmpCnt = 0, layCnt = 0, cnt = 0, rainCnt = 0;
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
                                                image.setImageResource(R.drawable.weather_sunny);
                                                break;
                                            case "3":
                                                sky.setText("구름많음");
                                                image.setImageResource(R.drawable.weather_cloudy);
                                                break;
                                            case "4":
                                                sky.setText("흐림");
                                                image.setImageResource(R.drawable.weather_cloudy);
                                                break;
                                        }
                                    }
                                    if (vcg.get(i).equals("PTY") && rainCnt == 0) {
                                        rainCnt++;
                                        switch (vfv.get(i)) {
                                            case "0": break;
                                            case "1":
                                                sky.setText("비");
                                                image.setImageResource(R.drawable.weather_rain);
                                                break;
                                            case "2":
                                                sky.setText("비/눈");
                                                image.setImageResource(R.drawable.weather_rain);
                                                break;
                                            case "3":
                                                sky.setText("눈");
                                                image.setImageResource(R.drawable.weather_snow);
                                                break;
                                            case "4":
                                                sky.setText("소나기");
                                                image.setImageResource(R.drawable.weather_rain);
                                                break;
                                        }
                                    }

                                    cnt++;
                                    if (cnt == 12){
                                        cnt = 0;
                                        layCnt++;
                                    }

                                    if (layCnt == 1){
                                        String str = vft.get(i).substring(0, 2);
                                        str += "시";
                                        fcstTime0.setText(str);

                                        if (vcg.get(i).equals("TMP")){
                                            tempText0.setText(vfv.get(i) + "℃");
                                        }

                                        if (vcg.get(i).equals("SKY")) {
                                            switch (vfv.get(i)) {
                                                case "1":
                                                    weatherImage0.setImageResource(R.drawable.weather_sunny_icon);
                                                    break;
                                                case "3":
                                                    weatherImage0.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                                case "4":
                                                    weatherImage0.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                            }
                                        }
                                        if (vcg.get(i).equals("PTY")) {
                                            switch (vfv.get(i)) {
                                                case "0": break;
                                                case "1":
                                                    weatherImage0.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "2":
                                                    weatherImage0.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "3":
                                                    weatherImage0.setImageResource(R.drawable.weather_snow_icon);
                                                    break;
                                                case "4":
                                                    weatherImage0.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                            }
                                        }

                                        if (vcg.get(i).equals("REH")){
                                            REHText0.setText(vfv.get(i) + "%");
                                        }
                                        if (vcg.get(i).equals("WSD")){
                                            WSDText0.setText(vfv.get(i) + "m/s");
                                        }
                                    }
                                    else if (layCnt == 2){
                                        String str = vft.get(i).substring(0, 2);
                                        str += "시";
                                        fcstTime1.setText(str);

                                        if (vcg.get(i).equals("TMP")){
                                            tempText1.setText(vfv.get(i) + "℃");
                                        }
                                        if (vcg.get(i).equals("REH")){
                                            REHText1.setText(vfv.get(i) + "%");
                                        }
                                        if (vcg.get(i).equals("WSD")){
                                            WSDText1.setText(vfv.get(i) + "m/s");
                                        }

                                        if (vcg.get(i).equals("SKY")) {
                                            switch (vfv.get(i)) {
                                                case "1":
                                                    weatherImage1.setImageResource(R.drawable.weather_sunny_icon);
                                                    break;
                                                case "3":
                                                    weatherImage1.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                                case "4":
                                                    weatherImage1.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                            }
                                        }
                                        if (vcg.get(i).equals("PTY")) {
                                            switch (vfv.get(i)) {
                                                case "0": break;
                                                case "1":
                                                    weatherImage1.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "2":
                                                    weatherImage1.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "3":
                                                    weatherImage1.setImageResource(R.drawable.weather_snow_icon);
                                                    break;
                                                case "4":
                                                    weatherImage1.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                            }
                                        }
                                    }
                                    else if (layCnt == 3){
                                        String str = vft.get(i).substring(0, 2);
                                        str += "시";
                                        fcstTime2.setText(str);

                                        if (vcg.get(i).equals("TMP")){
                                            tempText2.setText(vfv.get(i) + "℃");
                                        }
                                        if (vcg.get(i).equals("REH")){
                                            REHText2.setText(vfv.get(i) + "%");
                                        }
                                        if (vcg.get(i).equals("WSD")){
                                            WSDText2.setText(vfv.get(i) + "m/s");
                                        }

                                        if (vcg.get(i).equals("SKY")) {
                                            switch (vfv.get(i)) {
                                                case "1":
                                                    weatherImage2.setImageResource(R.drawable.weather_sunny_icon);
                                                    break;
                                                case "3":
                                                    weatherImage2.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                                case "4":
                                                    weatherImage2.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                            }
                                        }
                                        if (vcg.get(i).equals("PTY")) {
                                            switch (vfv.get(i)) {
                                                case "0": break;
                                                case "1":
                                                    weatherImage2.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "2":
                                                    weatherImage2.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "3":
                                                    weatherImage2.setImageResource(R.drawable.weather_snow_icon);
                                                    break;
                                                case "4":
                                                    weatherImage2.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                            }
                                        }
                                    }
                                    else if (layCnt == 4){
                                        String str = vft.get(i).substring(0, 2);
                                        str += "시";
                                        fcstTime3.setText(str);

                                        if (vcg.get(i).equals("TMP")){
                                            tempText3.setText(vfv.get(i) + "℃");
                                        }
                                        if (vcg.get(i).equals("REH")){
                                            REHText3.setText(vfv.get(i) + "%");
                                        }
                                        if (vcg.get(i).equals("WSD")){
                                            WSDText3.setText(vfv.get(i) + "m/s");
                                        }

                                        if (vcg.get(i).equals("SKY")) {
                                            switch (vfv.get(i)) {
                                                case "1":
                                                    weatherImage3.setImageResource(R.drawable.weather_sunny_icon);
                                                    break;
                                                case "3":
                                                    weatherImage3.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                                case "4":
                                                    weatherImage3.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                            }
                                        }
                                        if (vcg.get(i).equals("PTY")) {
                                            switch (vfv.get(i)) {
                                                case "0": break;
                                                case "1":
                                                    weatherImage3.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "2":
                                                    weatherImage3.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "3":
                                                    weatherImage3.setImageResource(R.drawable.weather_snow_icon);
                                                    break;
                                                case "4":
                                                    weatherImage3.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                            }
                                        }
                                    }
                                    else if (layCnt == 5){
                                        String str = vft.get(i).substring(0, 2);
                                        str += "시";
                                        fcstTime4.setText(str);

                                        if (vcg.get(i).equals("TMP")){
                                            tempText4.setText(vfv.get(i) + "℃");
                                        }
                                        if (vcg.get(i).equals("REH")){
                                            REHText4.setText(vfv.get(i) + "%");
                                        }
                                        if (vcg.get(i).equals("WSD")){
                                            WSDText4.setText(vfv.get(i) + "m/s");
                                        }

                                        if (vcg.get(i).equals("SKY")) {
                                            switch (vfv.get(i)) {
                                                case "1":
                                                    weatherImage4.setImageResource(R.drawable.weather_sunny_icon);
                                                    break;
                                                case "3":
                                                    weatherImage4.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                                case "4":
                                                    weatherImage4.setImageResource(R.drawable.weather_cloudy_icon);
                                                    break;
                                            }
                                        }
                                        if (vcg.get(i).equals("PTY")) {
                                            switch (vfv.get(i)) {
                                                case "0": break;
                                                case "1":
                                                    weatherImage4.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "2":
                                                    weatherImage4.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                                case "3":
                                                    weatherImage4.setImageResource(R.drawable.weather_snow_icon);
                                                    break;
                                                case "4":
                                                    weatherImage4.setImageResource(R.drawable.weather_rain_icon);
                                                    break;
                                            }
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

