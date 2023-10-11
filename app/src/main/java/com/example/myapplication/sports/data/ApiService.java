package com.example.myapplication.sports.data;

import com.example.myapplication.sports.model.FixtureResponse;
import com.example.myapplication.sports.model.TeamResponse;
import com.example.myapplication.sports.util.Constant;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ApiService {
    @Headers(Constant.API_KEY)
    @GET(Constant.GET_ALL_TEAMS_OF_LEAGUE)
    Single<TeamResponse> getAllTeamsOfLeague(@Path("league_id") int leagueId);

    @Headers(Constant.API_KEY)
    @GET(Constant.GET_ALL_FIXTURE_OF_LEAGUE)
    Single<FixtureResponse> getAllFixtureOfLeague(@Path("league_id") int leagueId);

    /*@Headers(Constant.API_KEY)
    @GET(Constant.GET_LEAGUE_TABLE)
    Single<LeagueTableResponse> getLeagueTable(@Path("league_id") int leagueId);

    @Headers(Constant.API_KEY)
    @GET(Constant.GET_TOP_SCORERS)
    Single<TopScorerResponse> getTopScorers(@Path("league_id") int leagueId);



    @Headers(Constant.API_KEY)
    @GET(Constant.GET_ALL_PLAYERS_OF_TEAM)
    Single<PlayerResponse> getAllPlayersOfTeam(@Path("team_id") int teamId);

    @Headers(Constant.API_KEY)
    @GET(Constant.GET_ALL_TRANSFERS_OF_TEAM)
    Single<TransferResponse> getAllTransfersOfTeam(@Path("team_id") int teamId);



    @Headers(Constant.API_KEY)
    @GET(Constant.GET_ALL_H2H_ITEMS)
    Single<H2HResponse> getAllH2hItems(@Path("home_team_id") int homeTeamId, @Path("away_team_id") int awayTeamId);

    @Headers(Constant.API_KEY)
    @GET(Constant.GET_FIXTURE_STATISTICS)
    Single<StatisticsResponse> getFixtureStatistics(@Path("fixture_id") int fixtureId);*/
}
