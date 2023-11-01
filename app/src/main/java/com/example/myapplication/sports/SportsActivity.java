package com.example.myapplication.sports;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
}
