package com.example.myapplication.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("receiced!","dasdas");

        int id = intent.getIntExtra("id",0);
        String timeValue = intent.getStringExtra("time");



        Intent alarmIntent = new Intent(context, FragAlarmCalled.class);
        alarmIntent.putExtra("id", id);
        alarmIntent.putExtra("time", timeValue);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}