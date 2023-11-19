package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

// import com.example.myapplication.alarm.FragAlarm;
import com.example.myapplication.alarm.AlarmReceiver;
import com.example.myapplication.alarm.FragAlarm;
import com.example.myapplication.alarm.PermissionUtils;
import com.example.myapplication.weather.api.UltraSrtNcstAPI;
import com.example.myapplication.weather.api.VillageFcstAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    //바텀 네비게이션
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        runtimeCheckPermission();

        loadFragment(new FragAlarm());
        if (PermissionUtils.hasSystemAlertWindowPermission(this)) {
            // 권한이 이미 부여되어 있음
            // 오버레이를 사용하는 코드를 실행할 수 있음
        } else {
            // 권한이 부여되지 않았으므로 요청
            PermissionUtils.requestSystemAlertWindowPermission(this);
        }


        // loadFragment(new FragAlarm());

        // 바텀 네비게이션 아이템 클릭 이벤트 처리
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;
                // 클릭된 아이템에 따라 프래그먼트를 변경합니다.
                if (item.getItemId() == R.id.alarm) {
                    fragment = new FragAlarm();
                } else if (item.getItemId() == R.id.notification) {
                    fragment = new FragNoti();
                }

                return loadFragment(fragment);
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_frame, fragment);
            transaction.commit();

            return true;
        }
        return false;
    }
    public void runtimeCheckPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 1004);
        }
    }

}