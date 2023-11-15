package com.example.myapplication.sports.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Team {
    private boolean subscribe = false;
    @SerializedName("team_id")
    private int teamId;

    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private Object code; // This may need to be adjusted to the appropriate type

    @SerializedName("logo")
    private String logo;

    @SerializedName("country")
    private String country;

    @SerializedName("is_national")
    private boolean isNational;

    @SerializedName("founded")
    private int founded;

    @SerializedName("venue_name")
    private String venueName;

    @SerializedName("venue_surface")
    private String venueSurface;

    @SerializedName("venue_address")
    private String venueAddress;

    @SerializedName("venue_city")
    private String venueCity;

    @SerializedName("venue_capacity")
    private int venueCapacity;

    public int getTeamId() {
        return teamId;
    }


    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }
    public Boolean getSub() {
        return subscribe;
    }
    public void setSub() {
        subscribe = true;
    }
}
