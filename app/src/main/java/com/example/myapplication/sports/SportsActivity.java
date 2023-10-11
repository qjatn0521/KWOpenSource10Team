package com.example.myapplication.sports;

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
import com.example.myapplication.sports.model.Team;

import java.util.List;

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
        viewModel.getAllTeamsOfLeague(leagueId);
        TeamAdapter adapter =new TeamAdapter();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        viewModel.getTeamsList().observe(this, new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teams) {
                //Log.d("hi","thisis good");
                if (teams != null) {
                    for (Team data : teams) {
                        Log.d("logo",data.getLogo());
                        adapter.addItem(data);
                    }
                    rvTeam.setLayoutManager(layoutManager);
                    rvTeam.setAdapter(adapter);
                }

            }
        });


    }
}
