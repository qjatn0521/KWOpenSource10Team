package com.example.myapplication.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.hasExtra("playAlarm")) {
            // FragAlarmCalled 클래스 실행
            Intent fragAlarmCalledIntent = new Intent(context, FragAlarmCalled.class);
            fragAlarmCalledIntent.putExtras(intent.getExtras()); // 추가 정보를 전달하기 위해 인텐트의 추가 정보를 알람 호출 인텐트에 넣어줍니다.
            fragAlarmCalledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 태스크에서 액티비티 시작
            context.startActivity(fragAlarmCalledIntent);
        }
    }
}
