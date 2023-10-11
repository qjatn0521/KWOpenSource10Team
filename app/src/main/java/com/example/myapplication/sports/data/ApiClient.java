package com.example.myapplication.sports.data;

import android.util.Log;

import com.example.myapplication.sports.model.FixtureResponse;
import com.example.myapplication.sports.model.TeamResponse;
import com.example.myapplication.sports.util.Constant;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private ApiService api = new Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService.class);

    public Single<TeamResponse> getAllTeamsOfLeague(int leagueId) {
        return api.getAllTeamsOfLeague(leagueId);
    }
    public Single<FixtureResponse> getAllFixtureOfLeague(int leagueId) {
        return api.getAllFixtureOfLeague(leagueId);
    }
    /*public Single<LeagueTableResponse> getLeagueTable(int leagueId) {
        return api.getLeagueTable(leagueId);
    }
    public Single<TopScorerResponse> getTopScorers(int leagueId) {
        return api.getTopScorers(leagueId);
    }

    public Single<PlayerResponse> getAllPlayersOfTeam(int teamId) {
        return api.getAllPlayersOfTeam(teamId);
    }
    public Single<TransferResponse> getAllTransfersOfTeam(int teamId) {
        return api.getAllTransfersOfTeam(teamId);
    }

    public Single<H2HResponse> getAllH2hItems(int homeTeamId, int awayTeamId) {
        return api.getAllH2hItems(homeTeamId, awayTeamId);
    }
    public Single<StatisticsResponse> getFixtureStatistics(int fixtureId) {
        return api.getFixtureStatistics(fixtureId);
    }*/
}
