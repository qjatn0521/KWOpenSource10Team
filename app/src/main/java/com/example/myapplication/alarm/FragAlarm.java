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

public class FragAlarm extends Fragment {

    private LinearLayout alarmListLayout;
    private Button addAlarmButton;
    private Button deleteAlarmButton;
    private List<String> alarms;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_alarm, container, false);

        alarmListLayout = view.findViewById(R.id.alarmListLayout);
        addAlarmButton = view.findViewById(R.id.addAlarmButton);
        deleteAlarmButton = view.findViewById(R.id.deleteAlarmButton);
        alarms = new ArrayList<>();

        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm("새로운 알람");
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
    }
}
