package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragNotiBinding;
import com.example.myapplication.sports.SportsActivity;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;

import java.util.List;

public class FragNoti extends Fragment {

    private FragNotiBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragNotiBinding.inflate((getLayoutInflater()));

        binding.btnSettingSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SportsActivity.class);
                startActivity(intent);
            }
        });

        new QueryDatabaseTask().execute();

        return binding.getRoot();
    }
    private class QueryDatabaseTask extends AsyncTask<Void, Void, List<FixtureDB>> {
        @Override
        protected List<FixtureDB> doInBackground(Void... voids) {
            // 데이터베이스에서 FixtureDB 정보 가져오기
            FixtureDBDao fixtureDao = FixtureDatabase.getInstance(getContext()).fixtureDao();
            return fixtureDao.getFixturesByTeamId(33); // 팀 ID를 적절한 값으로 바꿔주세요.
        }

        @Override
        protected void onPostExecute(List<FixtureDB> fixtureList) {
            // 데이터베이스 쿼리 결과를 처리하고 UI 업데이트
            for (FixtureDB fixture : fixtureList) {
                Log.d("FixtureInfo", "Team: " + fixture.teamId+","+fixture.homeTeamName+"vs"+fixture.awayTeamName+":"+fixture.date+", "+fixture.time);
            }
        }
    }
}
