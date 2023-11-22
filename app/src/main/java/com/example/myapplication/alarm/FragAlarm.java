package com.example.myapplication.alarm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.util.Locale;
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
    private TextView addAlarmButton;
    private TextView deleteAlarmButton;
    private Button stopAlarmButton;
    private List<Alarm> alarms= new ArrayList<>();;
    private AlarmManager alarmManager;
    private MediaPlayer mediaPlayer;
    private ArrayAdapter<Alarm> adapter;
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

                }
            });
        }
    }


    private long calculateAlarmTime(String alarmTime) {
        // 현재 시간 계산
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // 입력된 알람 시간 파싱
        String[] timeComponents = alarmTime.split(":");
        int alarmHour = Integer.parseInt(timeComponents[0].replaceAll("[^0-9]", ""));
        int alarmMinute = Integer.parseInt(timeComponents[1].replaceAll("[^0-9]", ""));

        if (alarmTime.contains("오후") && alarmHour < 12) {
            alarmHour += 12;
        }

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
        calendar.set(Calendar.MILLISECOND, 0);
        alarmTimeMillis = calendar.getTimeInMillis();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더해줌
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        // Log에 한 줄에 출력
        String logMessage = String.format("Time: %04d-%02d-%02d %02d:%02d:%02d.%03d",
                year, month, day, hour, minute, second, millisecond);
        Log.d("CalendarLogExample22", logMessage);

        SimpleDateFormat logFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        String logCurrentTime = logFormat.format(System.currentTimeMillis());
        String logAlarmTime = logFormat.format(calendar.getTimeInMillis());
        Log.d("AlarmTimeCalculator", "Current Time: " + logCurrentTime);
        Log.d("AlarmTimeCalculator", "Alarm Time: " + logAlarmTime);
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
    public void onDestroy() {
        super.onDestroy();
        saveAlarmsToSharedPreferences();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveAlarmsToSharedPreferences();
        outState.putParcelableArrayList(ALARMS_KEY, new ArrayList<>(alarms));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_alarm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alarmManager =(AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        alarmListView = view.findViewById(R.id.alarmListView);
        addAlarmButton = view.findViewById(R.id.addAlarmButton);
        deleteAlarmButton = view.findViewById(R.id.deleteAlarmButton);
        checkedStates = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_multiple_choice, alarms);
        alarmListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 단일 선택 모드로 설정
        alarmListView.setAdapter(adapter);

        // 나머지 초기화 작업 수행
        checkedStates.clear();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ALARMS_KEY)) {
                List<Alarm> savedAlarms = new ArrayList<>(savedInstanceState.getParcelableArrayList(ALARMS_KEY));
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
        updateAlarmList();

    }

    @SuppressLint("ScheduleExactAlarm")
    private void addAlarm(String alarmTime) {
        Alarm a = new Alarm(alarmTime);
        alarms.add(a);
        updateAlarmList();

        //알람 등록
        long alarmTimeMillis = calculateAlarmTime(alarmTime);
        Log.d("alarmTime",alarmTimeMillis+""+a.getAlarmId());

        Intent receiverIntent = new Intent(getContext(), AlarmReceiver.class);
        receiverIntent.putExtra("id", a.getAlarmId());
        receiverIntent.putExtra("time", alarmTime);
        //Log.d("add ID", a.getAlarmId()+"");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), a.getAlarmId(), receiverIntent, PendingIntent.FLAG_IMMUTABLE);

        //alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M (6.0, API level 23) 이상에서는 setExactAndAllowWhileIdle을 사용
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android KITKAT (4.4, API level 19) 이상에서는 setExact을 사용
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        } else {
            // 이전 버전에서는 set을 사용
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }
    }

    private void checkAlarms() {
        if (alarms.size() > 0) {
            alarmTime = alarms.get(0).getTime();
            long alarmTimeMillis = calculateAlarmTime(alarmTime);
            if (alarmTimeMillis > 0) {
                playAlarmSound(alarmTimeMillis);
            }
        }
    }

    private void deleteAlarm(Alarm selectedalarm) {
        if (alarms.contains(selectedalarm)) {
            alarms.remove(selectedalarm);
            Intent alarmIntent = new Intent(requireContext(), FragAlarmCalled.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), selectedalarm.getAlarmId(), alarmIntent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.cancel(pendingIntent);

            pendingIntent.cancel();
            updateAlarmList();
        }
    }


    private void updateAlarmList() {
        Collections.sort(alarms, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm alarm1, Alarm alarm2) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                try {
                    Date time1 = timeFormat.parse(alarm1.getTime());
                    Date time2 = timeFormat.parse(alarm2.getTime());
                    return time1.compareTo(time2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                } catch (java.text.ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        List<String> formattedAlarms = new ArrayList<>();
        for (Alarm alarm : alarms) {
            Log.d("alarm",formatAlarmTime(alarm.getTime()));
            formattedAlarms.add(formatAlarmTime(alarm.getTime()));
        }

        Collections.sort(formattedAlarms); // 시간순으로 정렬된 문자열 리스트

        AlarmAdapter adapter = new AlarmAdapter(requireContext(), formattedAlarms);
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
                    Alarm selectedAlarm = alarms.get(checkedItemPosition);
                    Intent intent = new Intent(requireContext(), AlarmReceiver.class);
                    Log.d("del ID", selectedAlarm.getAlarmId()+"");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), selectedAlarm.getAlarmId(), intent, PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.cancel(pendingIntent);

                    deleteAlarm(selectedAlarm);

                }
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
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPrefAlarm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String alarmsJson = gson.toJson(alarms);

        editor.putString(ALARMS_KEY, alarmsJson);
        editor.apply();
    }
    private String formatAlarmTimeForSave(String alarmTime) {
        // 12시간 형식의 시간을 24시간 형식으로 변환합니다.
        String[] timeComponents = alarmTime.split(":");
        int hour = Integer.parseInt(timeComponents[0].replaceAll("[^0-9]", ""));
        int minute = Integer.parseInt(timeComponents[1].replaceAll("[^0-9]", ""));
        if (alarmTime.contains("오후") && hour < 12) {
            hour += 12;
        } else if (alarmTime.contains("오전") && hour == 12) {
            hour = 0;
        }
        return String.format("%02d:%02d", hour, minute);
    }

    private void loadAlarmsFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPrefAlarm", Context.MODE_PRIVATE);
        String alarmsJson = sharedPreferences.getString(ALARMS_KEY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Alarm>>() {}.getType();
        ArrayList a = gson.fromJson(alarmsJson, type);
        alarms.clear(); // 기존 알람 리스트를 초기화
        if(a!=null)
            alarms.addAll(a); // 정렬된 알람 리스트를 추가
        updateAlarmList();
        //checkAlarms();
    }
    private String formatAlarmTimeForLoad(String alarmTime) {
        // 24시간 형식의 시간을 12시간 형식으로 변환합니다.
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


    private String formatAlarmTime(String alarmTime) {
        // 시간을 오전/오후 시간 형식으로 변환합니다.
        if (alarmTime.contains("오전") || alarmTime.contains("오후")) {
            return alarmTime; // 이미 오전/오후가 포함된 문자열이라면 그대로 반환
        }

        String[] timeComponents = alarmTime.split(":");
        int hour = Integer.parseInt(timeComponents[0].replaceAll("[^0-9]", ""));
        int minute = Integer.parseInt(timeComponents[1].replaceAll("[^0-9]", ""));
        String amPm;
        if (hour < 12) {
            amPm = "오전";
            if (hour == 0) {
                hour = 12;
            }
        } else {
            amPm = "오후";
            if (hour > 12) {
                hour -= 12;
            }
        }
        String formattedTime = String.format("%s %02d:%02d", amPm, hour, minute);
        Log.d("FormatAlarmTime", "Original: " + alarmTime + " Formatted: " + formattedTime);
        return formattedTime;
    }

    
}
