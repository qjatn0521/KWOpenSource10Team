package com.example.myapplication.sports;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityNotiSportsBinding;
import com.example.myapplication.sports.adapter.TeamAdapter;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;
import com.example.myapplication.sports.model.Fixture;
import com.example.myapplication.sports.model.Team;


import java.util.List;
import java.util.Objects;

public class SportsActivity extends AppCompatActivity {
    private ActivityNotiSportsBinding binding;
    private RecyclerView rvTeam;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_sports);
        binding = ActivityNotiSportsBinding.inflate(getLayoutInflater());
        CustomSharedPreferences customPreferences = new CustomSharedPreferences(this);
        int leagueId = customPreferences.getCountryId();

        TeamViewModel viewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        rvTeam=findViewById(R.id.soccre_recyclerview);

        runtimeCheckPermission();
        //팀 정보 얻어오기

        viewModel.getAllTeamsOfLeague(leagueId);
        TeamAdapter adapter =new TeamAdapter(viewModel);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        viewModel.getTeamsList().observe(this, new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teams) {
                if (teams != null) {
                    for (Team data : teams) {
                        checkFixturesForTeamInBackground(data);
                        adapter.addItem(data);
                    }
                    rvTeam.setLayoutManager(layoutManager);
                    rvTeam.setAdapter(adapter);
                }

            }
        });

    }
    private void checkFixturesForTeamInBackground(Team team) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                FixtureDBDao fixtureDao = FixtureDatabase.getInstance(SportsActivity.this).fixtureDao();
                //Log.d("sub!!!!",team.getTeamId()+" "+fixtureDao.hasFixturesForTeam(team.getTeamId())+" "+team.getSub()+"");
                if (fixtureDao.hasFixturesForTeam(team.getTeamId()) > 0) {
                    team.setSub();
                }
                return 0;
            }

        }.execute();
    }
    public void runtimeCheckPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1004);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1004:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 이미 있는 경우
                    Log.i("권한 테스트", "사용자가 권한을 부여해 줬습니다.");
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("앱 권한 설정");
                    alertDialog.setMessage("설정으로 이동합니다.");
                    alertDialog.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 이 부분은 설정으로 이동하는 코드이므로 안드로이드 운영체제 버전에 따라 상이할 수 있다.
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                    startActivity(intent);
                                    dialogInterface.cancel();
                                }
                            });

                    alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog.show();
                }
        }
    }
}
