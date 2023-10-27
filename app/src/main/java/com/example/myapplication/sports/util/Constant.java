package com.example.myapplication.sports.util;

public class Constant {
    public static final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v2/";
    public static final String API_KEY = "x-rapidapi-key: 2fab3ab28bmshe055fe955ba3da7p1339b8jsnfcf13e442db0";
    public static final String GET_LEAGUE_TABLE = "leagueTable/{league_id}";
    public static final String GET_TOP_SCORERS = "topscorers/{league_id}";
    public static final String GET_ALL_TEAMS_OF_LEAGUE = "teams/league/{league_id}";
    public static final String GET_ALL_PLAYERS_OF_TEAM = "players/squad/{team_id}/2019";
    public static final String GET_ALL_TRANSFERS_OF_TEAM = "transfers/team/{team_id}";
    public static final String GET_TEAM_FIXTURE_OF_LEAGUE = "fixtures/team/{team_id}?timezone=Asia%2FSeoul";
    public static final String GET_ALL_H2H_ITEMS = "fixtures/h2h/{home_team_id}/{away_team_id}";
    public static final String GET_FIXTURE_STATISTICS = "statistics/fixture/{fixture_id}";

    public static final String TEAM_ID = "team_id";
    public static final String FIXTURE_TEAM_IDS = "h2h_team_ids";
}
