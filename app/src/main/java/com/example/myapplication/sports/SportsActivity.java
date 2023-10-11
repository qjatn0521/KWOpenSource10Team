package com.example.myapplication.sports;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.databinding.ActivityNotiSportsBinding;
import com.example.myapplication.sports.model.Team;

import java.util.List;

public class SportsActivity extends AppCompatActivity {
    private ActivityNotiSportsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotiSportsBinding.inflate(getLayoutInflater());

        CustomSharedPreferences customPreferences = new CustomSharedPreferences(this);
        int leagueId = customPreferences.getCountryId();

        TeamViewModel viewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        viewModel.getAllTeamsOfLeague(leagueId);

        viewModel.getTeamsList().observe(this, new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teams) {
                //Log.d("hi","thisis good");
                if (teams != null) {
                    for (Team data : teams) {
                        Log.d("data", data.getName()+data.getVenueCity());
                    }
                }
            }
        });
    }
}
