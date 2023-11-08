package com.example.myapplication.weather;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.weather.api.UltraSrtNcstAPI;
import com.example.myapplication.weather.api.VillageFcstAPI;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WeatherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_weather);

        UltraSrtNcstAPI ulweather = new UltraSrtNcstAPI("20231109", "0000", "55", "127");
        VillageFcstAPI  viweather = new VillageFcstAPI("20231108", "2300", "55", "127");

        TextView temperature = findViewById(R.id.temperature);
        TextView highTemp = findViewById(R.id.HighTemp);

        // API를 받아오는 Thread
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

