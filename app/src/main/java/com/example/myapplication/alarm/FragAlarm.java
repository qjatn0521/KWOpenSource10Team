package com.example.myapplication.alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
import java.util.Set;
import android.app.ActivityManager;
import androidx.core.app.NotificationManagerCompat;
import android.provider.Settings;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class FragAlarm extends Fragment {

    private ListView alarmListView;
    private Button addAlarmButton;
    private Button deleteAlarmButton;
    private Button stopAlarmButton;
    private List<String> alarms;
    private AlarmManager alarmManager;
    private PendingIntent alarmPendingIntent;
    private MediaPlayer mediaPlayer;
    private ArrayAdapter<String> adapter;
    private List<Boolean> checkedStates;
    private String alarmTime;
    private static final String ALARMS_KEY = "alarms";


    private void showTimePickerDialog() {
        try {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String time = String.format("%02d:%02d", hourOfDay, minute);
                            addAlarm(time);
                            Log.d("set","set");
                            alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, alarmPendingIntent);
                        }
                    },
                    // 초기 시간 설정 (현재 시간)
                    hour,
                    minute,
                    false
            );
            timePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAlarmSound(long delay) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarm_sound);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release(); // MediaPlayer 해제
                    mediaPlayer = null;
                    checkAlarms(); // 다음 알람 확인
                    markAlarmAsRang(alarmTime);
                    //updateAlarmList(); // 알람 리스트 갱신
                }
            });
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alarms.size() > 0) {
                    mediaPlayer.start();

                    // 앱이 꺼져 있을 때만 FragAlarmCalled 프래그먼트를 띄웁니다.
                    if (!isAppInForeground()) {
                        Fragment fragAlarmCalled = new FragAlarmCalled();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_alarm_called, fragAlarmCalled, "FragAlarmCalled")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        }, delay);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveAlarmsToSharedPreferences();
        outState.putStringArrayList(ALARMS_KEY, new ArrayList<>(alarms));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_alarm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alarmListView = view.findViewById(R.id.alarmListView);
        addAlarmButton = view.findViewById(R.id.addAlarmButton);
        deleteAlarmButton = view.findViewById(R.id.deleteAlarmButton);
        stopAlarmButton = view.findViewById(R.id.stopAlarmButton);
        alarms = new ArrayList<>();
        checkedStates = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_multiple_choice, alarms);
        alarmListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 단일 선택 모드로 설정
        alarmListView.setAdapter(adapter);
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(requireContext(), AlarmReceiver.class);
        alarmPendingIntent = PendingIntent.getBroadcast(requireContext(), 1, alarmIntent,  PendingIntent.FLAG_IMMUTABLE);


        // 나머지 초기화 작업 수행
        checkedStates.clear();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ALARMS_KEY)) {
                List<String> savedAlarms = new ArrayList<>(savedInstanceState.getStringArrayList(ALARMS_KEY));
                alarms.clear();
                alarms.addAll(savedAlarms);
                updateAlarmList();
            }
        }
        for (int i = 0; i < alarms.size(); i++) {
            checkedStates.add(false);
        }

        loadAlarmsFromSharedPreferences();

        setOnClickListeners();
        checkAlarms();

    }

    private void addAlarm(String alarmTime) {
        alarms.add(alarmTime);
        Collections.sort(alarms); // 시간순으로 정렬
        updateAlarmList();
        //checkAlarms(); // 다음 알람 확인
    }

    private void checkAlarms() {
        if (alarms.size() > 0) {
            alarmTime = alarms.get(0);
            long alarmTimeMillis = calculateAlarmTime(alarmTime);
            if (alarmTimeMillis > 0) {

                playAlarmSound(alarmTimeMillis);
            }
        }
    }

    private void deleteAlarm(String alarmTime) {
        if (alarms.contains(alarmTime)) {
            alarms.remove(alarmTime);
            updateAlarmList();
            stopAlarm();
            checkAlarms(); // 다음 알람 확인
        }
    }

    private void stopAlarm() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;

            // 알람이 멈추면 "알람이 울렸습니다." 텍스트를 제거합니다.
            removeRingingText();
        }
    }

    private void updateAlarmList() {
        List<String> formattedAlarms = new ArrayList<>();
        for (String alarm : alarms) {
            formattedAlarms.add(formatAlarmTime(alarm));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, formattedAlarms);
        alarmListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setOnClickListeners() {
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, alarms);
        alarmListView.setAdapter(adapter);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        deleteAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedItemPosition = alarmListView.getCheckedItemPosition();
                if (checkedItemPosition != AdapterView.INVALID_POSITION) {
                    String selectedAlarm = alarms.get(checkedItemPosition);
                    deleteAlarm(selectedAlarm);
                }
            }
        });

        stopAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
            }
        });

        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || position >= checkedStates.size()) {
                    return; // 범위를 벗어날 경우 무시
                }

                for (int i = 0; i < alarms.size(); i++) {
                    if (i != position) {
                        alarmListView.setItemChecked(i, false);
                        if (i < checkedStates.size()) {
                            checkedStates.set(i, false);
                        }
                    }
                }

                // 선택된 항목 체크 상태 반전
                if (!checkedStates.get(position)) {
                    alarmListView.setItemChecked(position, true);
                    checkedStates.set(position, true);
                } else {
                    alarmListView.setItemChecked(position, false);
                    checkedStates.set(position, false);
                }
            }
        });
    }

    private void saveAlarmsToSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> alarmsSet = new HashSet<>(alarms);
        editor.putStringSet(ALARMS_KEY, alarmsSet);
        editor.apply();
    }

    private void loadAlarmsFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Set<String> alarmsSet = sharedPreferences.getStringSet(ALARMS_KEY, new HashSet<>());
        alarms.clear(); // 기존 알람 리스트를 초기화
        List<String> sortedAlarms = new ArrayList<>(alarmsSet);
        Collections.sort(sortedAlarms); // 시간순으로 정렬
        alarms.addAll(sortedAlarms); // 정렬된 알람 리스트를 추가
        updateAlarmList();
    }

    private boolean isAppInForeground() {
        ActivityManager activityManager = (ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfo = activityManager.getRunningAppProcesses();
        if (processInfo != null) {
            for (ActivityManager.RunningAppProcessInfo info : processInfo) {
                if (info.processName.equals(requireContext().getPackageName()) && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

    private void markAlarmAsRang(String alarmTime) {
        int index = alarms.indexOf(alarmTime);
        if (index != -1) {
            String markedAlarm = alarms.get(index);
            if (!markedAlarm.contains("알람 ON")) {
                markedAlarm = markedAlarm + " - 알람 ON.";
                alarms.set(index, markedAlarm);
                updateAlarmList();
            }
        }
    }

    private String formatAlarmTime(String alarmTime) {
        // 시간을 오전/오후 시간 형식으로 변환합니다.
        String[] timeComponents = alarmTime.split(":");
        int hour = Integer.parseInt(timeComponents[0]);
        int minute = Integer.parseInt(timeComponents[1]);
        String amPm;
        if (hour == 0) {
            hour = 12;
            amPm = "오전";
        } else if (hour < 12) {
            amPm = "오전";
        } else if (hour == 12) {
            amPm = "오후";
        } else {
            amPm = "오후";
            hour -= 12;
        }
        return String.format("%s %02d:%02d", amPm, hour, minute);
    }

    private void removeRingingText() {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).contains("알람이 울렸습니다.")) {
                String unmarkedAlarm = alarms.get(i).replace(" - 알람이 울렸습니다.", "");
                alarms.set(i, unmarkedAlarm);
                updateAlarmList();
                break;
            }
        }

    }
}
