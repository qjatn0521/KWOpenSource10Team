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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityNotiSportsBinding;
import com.example.myapplication.sports.adapter.TeamAdapter;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;
import com.example.myapplication.sports.model.Fixture;
import com.example.myapplication.sports.model.Team;
import com.example.myapplication.todo.TodoActivity;


import java.util.List;
import java.util.Objects;

public class SportsActivity extends AppCompatActivity {
    private RecyclerView rvTeam;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_sports);

        ImageView loadingIv = findViewById(R.id.loading_iv);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(loadingIv);
        Glide.with(this).load(R.drawable.loading).into(gifImage);
        ImageView backbtn = findViewById(R.id.back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CustomSharedPreferences customPreferences = new CustomSharedPreferences(this);
        int leagueId = customPreferences.getCountryId();

        TeamViewModel viewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        rvTeam=findViewById(R.id.soccre_recyclerview);

        //팀 정보 얻어오기

        viewModel.getAllTeamsOfLeague(leagueId);
        TeamAdapter adapter =new TeamAdapter(viewModel,findViewById(R.id.loading_layout));
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
                findViewById(R.id.loading_layout).setVisibility(View.GONE);
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
}