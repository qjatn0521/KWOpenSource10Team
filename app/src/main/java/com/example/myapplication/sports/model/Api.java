package com.example.myapplication.sports.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Api {
    @SerializedName("results")
    private int results;

    @SerializedName("teams")
    private List<Team> teams;
    @SerializedName("fixtures")
    private List<Fixture> fixtures;

    public int getResults() {
        return results;
    }


    public void setResults(int results) {
        this.results = results;
    }

    public List<Team> getTeams() {
        return teams;
    }
    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
