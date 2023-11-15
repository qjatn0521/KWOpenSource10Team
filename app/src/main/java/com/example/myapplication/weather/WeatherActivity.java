package com.example.myapplication.weather;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_weather);
        //TextView tv1 = findViewById(R.id.textView); 이런식으로 받아오면 됨
    }

}

