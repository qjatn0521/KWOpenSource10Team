package com.example.myapplication.sports.noti;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AlarmRecevier extends BroadcastReceiver {

    public AlarmRecevier(){ }

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("received", "dsadas");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String currentDateString = dateFormat.format(currentDate);

        // ExecutorService를 사용하여 백그라운드 스레드에서 데이터베이스 작업 수행
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            FixtureDB earliestFixture = getFixtureFromDatabase(context, currentDateString);

            // 주 스레드에서 UI 업데이트를 위해 Handler 사용
            new Handler(Looper.getMainLooper()).post(() -> {
                createNotificationAndContinue(context, earliestFixture);
            });
        });
    }
    private void createNotificationAndContinue(Context context, FixtureDB earliestFixture) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        builder = null;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        // 알림창 클릭 시 activity 화면 부름
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알림창 제목
        builder.setContentTitle(earliestFixture.awayTeamName + " vs " + earliestFixture.homeTeamName);
        // 알림창 터치시 자동 삭제
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.image_sprots_soccerball);
        Notification notification = builder.build();
        manager.notify(1, notification);


        // 데이터베이스에서 항목 삭제
        delFixtureFromDatabase(context, earliestFixture);

        // 다음 알람 시간을 계산
        long nextAlarmTimeMillis = calculateNextAlarmTime(earliestFixture);

        // 다음 알람 설정
        setNextAlarm(context, nextAlarmTimeMillis);
    }
    private void delFixtureFromDatabase(Context context, FixtureDB f) {
        FixtureDBDao yourDatabase = FixtureDatabase.getInstance(context).fixtureDao();
        yourDatabase.deleteFixture(f);
    }
    private FixtureDB getFixtureFromDatabase(Context context, String currentDate) {
        FixtureDBDao yourDatabase = FixtureDatabase.getInstance(context).fixtureDao();
        return yourDatabase.getEarliestFixture(currentDate);
    }
    private long calculateNextAlarmTime(FixtureDB fixture) {
        // FixtureDB의 date를 사용하여 다음 알람 시간을 계산
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = null;

        try {
            date = dateFormat.parse(fixture.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            return date.getTime(); // 다음 알람 시간을 밀리초로 반환
        } else {
            // 파싱에 실패하면 기본값을 반환 또는 오류 처리
            return System.currentTimeMillis();
        }
    }
    private void setNextAlarm(Context context, long nextAlarmTimeMillis) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmRecevier.class);

        // PendingIntent를 생성하여 알람 리시버를 호출
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 다음 알람을 설정 (다음 알람 시간은 nextAlarmTimeMillis에 의해 결정됨)
        am.set(AlarmManager.RTC_WAKEUP, nextAlarmTimeMillis, pendingIntent);
    }
}