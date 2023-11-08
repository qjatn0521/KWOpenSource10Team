package com.example.myapplication.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import com.example.myapplication.R;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable stopPlayer;
    private final int PLAY_TIME = 10000; // 음악 재생 시간 (10초)

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("playAlarm")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
            mediaPlayer.start();

            handler = new Handler();
            stopPlayer = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                    // FragAlarmCalled를 띄우는 코드 추가
                    Intent fragIntent = new Intent(getApplicationContext(), FragAlarmCalled.class);
                    fragIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(fragIntent);
                }
            };
            handler.postDelayed(stopPlayer, PLAY_TIME); // 지정된 시간(10초) 후에 음악 중지 및 FragAlarmCalled 시작
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (handler != null) {
            handler.removeCallbacks(stopPlayer); // 서비스가 종료될 때 핸들러의 작업 제거
        }
    }
}

