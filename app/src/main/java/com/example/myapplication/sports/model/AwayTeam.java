package com.example.myapplication.sports.model;

import com.google.gson.annotations.SerializedName;

public class AwayTeam {
    @SerializedName("team_id")
    private int teamİd;
    @SerializedName("team_name")
    private String teamName;
    @SerializedName("logo")
    private String logo;

    // Getter and setter methods for each field

    public int getTeamİd() {
        return teamİd;
    }

    public void setTeamİd(int teamİd) {
        this.teamİd = teamİd;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
