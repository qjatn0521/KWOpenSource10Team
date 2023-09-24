package com.example.myapplication.sports.model;

import com.google.gson.annotations.SerializedName;

public class Team {
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

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isNational() {
        return isNational;
    }

    public void setNational(boolean national) {
        isNational = national;
    }

    public int getFounded() {
        return founded;
    }

    public void setFounded(int founded) {
        this.founded = founded;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueSurface() {
        return venueSurface;
    }

    public void setVenueSurface(String venueSurface) {
        this.venueSurface = venueSurface;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    public int getVenueCapacity() {
        return venueCapacity;
    }

    public void setVenueCapacity(int venueCapacity) {
        this.venueCapacity = venueCapacity;
    }
}
