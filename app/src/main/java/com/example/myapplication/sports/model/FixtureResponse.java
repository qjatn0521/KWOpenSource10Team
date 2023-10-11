package com.example.myapplication.sports.model;

import com.google.gson.annotations.SerializedName;

public class FixtureResponse {
    @SerializedName("api")
    private Api api;

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }
}
