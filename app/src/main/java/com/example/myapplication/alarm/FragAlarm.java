package com.example.myapplication.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.myapplication.R;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import java.util.Calendar;
import androidx.core.app.NotificationManagerCompat;
import android.provider.Settings;
import android.os.Build;

public class FragAlarm extends Fragment {

    private LinearLayout alarmListLayout;
    private Button addAlarmButton;
    private Button deleteAlarmButton;
    private List<String> alarms;
    private AlarmManager alarmManager;
    private PendingIntent alarmPendingIntent;
    private MediaPlayer mediaPlayer;


    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format("%02d:%02d", hourOfDay, minute);
                        addAlarm(time);
                    }
                },
                // 초기 시간 설정 (현재 시간)
                12,
                0,
                true
        );
        timePickerDialog.show();
    }

    private long calculateAlarmTime(String alarmTime) {
        // 현재 시간 계산
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // 입력된 알람 시간 파싱
        String[] timeComponents = alarmTime.split(":");
        int alarmHour = Integer.parseInt(timeComponents[0]);
        int alarmMinute = Integer.parseInt(timeComponents[1]);

        // 알람 시간을 밀리초로 변환
        long currentTimeMillis = System.currentTimeMillis();
        long alarmTimeMillis = 0;
        if (alarmHour < currentHour || (alarmHour == currentHour && alarmMinute <= currentMinute)) {
            // 알람 시간이 현재 시간보다 이전이라면 다음날로 설정
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        calendar.set(Calendar.SECOND, 0);
        alarmTimeMillis = calendar.getTimeInMillis() - currentTimeMillis;

        return alarmTimeMillis;
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
            boolean areNotificationsEnabled = notificationManager.areNotificationsEnabled();
            if (!areNotificationsEnabled) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                startActivity(intent);
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_alarm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alarmListLayout = view.findViewById(R.id.alarmListLayout);
        addAlarmButton = view.findViewById(R.id.addAlarmButton);
        deleteAlarmButton = view.findViewById(R.id.deleteAlarmButton);
        alarms = new ArrayList<>();
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(requireContext(), AlarmReceiver.class);
        alarmPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // 나머지 초기화 작업 수행

        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        deleteAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm();
            }
        });
    }

    private void addAlarm(String alarmTime) {
        alarms.add(alarmTime);
        Collections.sort(alarms); // 시간순으로 정렬
        updateAlarmList();

        if (alarms.size() == 1) {
            long alarmTimeMillis = calculateAlarmTime(alarmTime); // 알람 시간을 계산하는 함수를 가정합니다.
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmTimeMillis, alarmPendingIntent);
        }
    }

    private void deleteAlarm() {
        if (alarms.size() > 0) {
            alarms.remove(alarms.size() - 1);
            updateAlarmList();
        }
    }

    private void updateAlarmList() {
        alarmListLayout.removeAllViews();
        for (String alarm : alarms) {
            TextView textView = new TextView(getContext());
            textView.setText(alarm);
            alarmListLayout.addView(textView);
        }

        if (alarms.size() > 0) {
            requestNotificationPermission();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "default")
                    .setSmallIcon(R.drawable.icon_noti)
                    .setContentTitle("알람 시간이 되었습니다.")
                    .setContentText("알람이 울립니다.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent intent = new Intent(requireContext(), FragAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());

            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarm_sound);
            mediaPlayer.start();
        }
    }
}
