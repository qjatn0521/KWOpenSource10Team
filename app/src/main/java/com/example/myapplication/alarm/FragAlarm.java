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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_alarm, container, false);

        alarmListLayout = view.findViewById(R.id.alarmListLayout);
        addAlarmButton = view.findViewById(R.id.addAlarmButton);
        deleteAlarmButton = view.findViewById(R.id.deleteAlarmButton);
        alarms = new ArrayList<>();
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(requireContext(), AlarmReceiver.class);
        alarmPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, 0);

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

        return view;
    }

    private void addAlarm(String alarmTime) {
        alarms.add(alarmTime);
        Collections.sort(alarms); // 시간순으로 정렬
        updateAlarmList();

        if (alarms.size() == 1) {
            //long alarmTimeMillis = calculateAlarmTime(alarmTime); // 알람 시간을 계산하는 함수를 가정합니다.
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
            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "default")
                    .setSmallIcon(R.drawable.icon_noti)
                    .setContentTitle("알람 시간이 되었습니다.")
                    .setContentText("알람이 울립니다.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent intent = new Intent(requireContext(), FragAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.notify(0, builder.build());

            //mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarm_sound);
            mediaPlayer.start();
        }
    }
}
