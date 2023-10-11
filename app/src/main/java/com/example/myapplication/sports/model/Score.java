package com.example.myapplication.sports.model;

import com.google.gson.annotations.SerializedName;

public class Score {
    @SerializedName("halftime")
    private String halftime;
    @SerializedName("fulltime")
    private String fulltime;
    @SerializedName("extratime")
    private Object extratime;
    @SerializedName("penalty")
    private Object penalty;

    // Getter and setter methods for each field

    public String getHalftime() {
        return halftime;
    }

    public void setHalftime(String halftime) {
        this.halftime = halftime;
    }

    public String getFulltime() {
        return fulltime;
    }

    public void setFulltime(String fulltime) {
        this.fulltime = fulltime;
    }

    public Object getExtratime() {
        return extratime;
    }

    public void setExtratime(Object extratime) {
        this.extratime = extratime;
    }

    public Object getPenalty() {
        return penalty;
    }

    public void setPenalty(Object penalty) {
        this.penalty = penalty;
    }
}
