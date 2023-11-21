package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

// import com.example.myapplication.alarm.FragAlarm;
import com.example.myapplication.alarm.AlarmReceiver;
import com.example.myapplication.alarm.FragAlarm;
import com.example.myapplication.alarm.FragAlarmCalled;
import com.example.myapplication.alarm.PermissionUtils;
import com.example.myapplication.weather.api.UltraSrtNcstAPI;
import com.example.myapplication.weather.api.VillageFcstAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    //바텀 네비게이션
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();


        loadFragment(new FragAlarm());




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
        checkLocationPermission();
        runtimeCheckPermission();
        if (PermissionUtils.hasSystemAlertWindowPermission(this)) {
            // 권한이 이미 부여되어 있음
            // 오버레이를 사용하는 코드를 실행할 수 있음
        } else {
            // 권한이 부여되지 않았으므로 요청
            PermissionUtils.requestSystemAlertWindowPermission(this);
        }
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
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // 권한이 이미 있는 경우 처리할 내용
            // 예: 위치 정보 가져오기 등의 작업 수행
        }
    }
    public void runtimeCheckPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, NOTIFICATION_PERMISSION_REQUEST_CODE);
        }
    }

}