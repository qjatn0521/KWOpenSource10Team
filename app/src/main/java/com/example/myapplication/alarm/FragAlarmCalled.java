package com.example.myapplication.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import android.widget.Button;
import android.view.View.OnClickListener;

public class FragAlarmCalled extends Fragment {

    private Button goOffButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_alarm_called, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goOffButton = view.findViewById(R.id.GoOffbutton);
        goOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 클릭하면 frag_alarm 화면이 나오도록 처리
                Fragment fragAlarm = new FragAlarm();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragAlarm)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}

