package com.example.myapplication.sports.noti;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.alarm.FragAlarmCalled;

public class NotificationReceiver extends BroadcastReceiver {
    private String TAG = this.getClass().getSimpleName();

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    //수신되는 인텐트 - The Intent being received.
    @Override
    public void onReceive(Context context, Intent intent) {
        String titleValue = intent.getStringExtra("title");
        String timeValue = intent.getStringExtra("time");
        int idValue = intent.getIntExtra("id",0);
        builder = null;

        //푸시 알림을 보내기위해 시스템에 권한을 요청하여 생성
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //안드로이드 오레오 버전 대응
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }



        //알림창 제목
        builder.setContentTitle(titleValue) //회의명노출
            .setContentText(timeValue) //회의 내용
            .setSound(null)
        //알림창 아이콘
            .setSmallIcon(R.drawable.image_sprots_soccerball)
        //알림창 터치시 자동 삭제
            .setAutoCancel(true);

        //NotificationManager를 이용하여 푸시 알림 보내기
        manager.notify(idValue,builder.build());
    }

}
