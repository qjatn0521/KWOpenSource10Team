package com.example.myapplication.weather;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.weather.api.UltraSrtNcstAPI;
import com.example.myapplication.weather.api.VillageFcstAPI;
import com.example.myapplication.weather.location.GPSLocation;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    private GPSLocation location;
    private static final int MIN_TIME_BW_UPDATES = 1000; // 1초마다
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.0f; // 10 미터마다

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_weather);

        UltraSrtNcstAPI ulweather = new UltraSrtNcstAPI("20231115", "0000", "55", "127");
        VillageFcstAPI viweather = new VillageFcstAPI("20231114", "2300", "55", "127");

        TextView temperature = findViewById(R.id.temperature);
        TextView highTemp = findViewById(R.id.HighTemp);

        /** 현재 위치를 받아오는 코드 */
        location = new GPSLocation(WeatherActivity.this);

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        System.out.println("latitude = " + latitude);
        System.out.println("longitude = " + longitude);

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
                            for(int i = 0; i < ucg.size(); i++){
                                if (ucg.get(i).equals("T1H")){
                                    temperature.setText(uob.get(i) + "°C");
                                }
                            }

                            for(int i = 0; i < 112; i++){
                                if (vcg.get(i).equals("TMP")) {
                                    highTemp.setText(vfv.get(i) + "°C");
                                }

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

