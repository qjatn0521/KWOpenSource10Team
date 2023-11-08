package com.example.myapplication.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.hasExtra("playAlarm")) {
            Log.d("Alarm","goodddddddddddddddddddddd");
            // FragAlarmCalled 클래스 실행
            Intent fragAlarmCalledIntent = new Intent(context, FragAlarmCalled.class);
            fragAlarmCalledIntent.putExtras(intent.getExtras()); // 추가 정보를 전달하기 위해 인텐트의 추가 정보를 알람 호출 인텐트에 넣어줍니다.
            fragAlarmCalledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 태스크에서 액티비티 시작
            context.startActivity(fragAlarmCalledIntent);
        }
    }
    public static void setAlarm(Context context, long triggerTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        // RTC_WAKEUP 알람 설정
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
    }
}
