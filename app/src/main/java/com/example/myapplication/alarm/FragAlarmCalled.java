package com.example.myapplication.alarm;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class FragAlarmCalled extends Activity {
    private MediaPlayer mediaPlayer;

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

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopAlarmSound();
                finish();
            }
        });

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
