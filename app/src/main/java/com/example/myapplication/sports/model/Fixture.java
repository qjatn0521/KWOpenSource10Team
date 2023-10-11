package com.example.myapplication.sports.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Fixture implements Parcelable {
    @SerializedName("fixture_id")
    private int fixtureİd;
    @SerializedName("league_id")
    private int leagueİd;
    @SerializedName("league")
    private League league;
    @SerializedName("event_date")
    private String eventDate;
    @SerializedName("event_timestamp")
    private int eventTimestamp;
    @SerializedName("firstHalfStart")
    private int firstHalfStart;
    @SerializedName("secondHalfStart")
    private int secondHalfStart;
    @SerializedName("round")
    private String round;
    @SerializedName("status")
    private String status;
    @SerializedName("statusShort")
    private String statusShort;
    @SerializedName("elapsed")
    private int elapsed;
    @SerializedName("venue")
    private String venue;
    @SerializedName("referee")
    private String referee;
    @SerializedName("homeTeam")
    private HomeTeam homeTeam;
    @SerializedName("awayTeam")
    private AwayTeam awayTeam;
    @SerializedName("goalsHomeTeam")
    private int goalsHomeTeam;
    @SerializedName("goalsAwayTeam")
    private int goalsAwayTeam;
    @SerializedName("score")
    private Score score;

    public Fixture() {
    }

    protected Fixture(Parcel in) {
        fixtureİd = in.readInt();
        leagueİd = in.readInt();
        eventDate = in.readString();
        eventTimestamp = in.readInt();
        firstHalfStart = in.readInt();
        secondHalfStart = in.readInt();
        round = in.readString();
        status = in.readString();
        statusShort = in.readString();
        elapsed = in.readInt();
        venue = in.readString();
        referee = in.readString();
        goalsHomeTeam = in.readInt();
        goalsAwayTeam = in.readInt();
    }

    public static final Creator<Fixture> CREATOR = new Creator<Fixture>() {
        @Override
        public Fixture createFromParcel(Parcel in) {
            return new Fixture(in);
        }

        @Override
        public Fixture[] newArray(int size) {
            return new Fixture[size];
        }
    };

    public int getFixtureİd() {
        return fixtureİd;
    }

    public int getLeagueİd() {
        return leagueİd;
    }

    public League getLeague() {
        return league;
    }

    public String getEventDate() {
        return eventDate;
    }

    public int getEventTimestamp() {
        return eventTimestamp;
    }

    public int getFirstHalfStart() {
        return firstHalfStart;
    }

    public int getSecondHalfStart() {
        return secondHalfStart;
    }

    public String getRound() {
        return round;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusShort() {
        return statusShort;
    }

    public int getElapsed() {
        return elapsed;
    }

    public String getVenue() {
        return venue;
    }

    public String getReferee() {
        return referee;
    }

    public HomeTeam getHomeTeam() {
        return homeTeam;
    }

    public AwayTeam getAwayTeam() {
        return awayTeam;
    }

    public int getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    public int getGoalsAwayTeam() {
        return goalsAwayTeam;
    }

    public Score getScore() {
        return score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fixtureİd);
        dest.writeInt(leagueİd);
        dest.writeString(eventDate);
        dest.writeInt(eventTimestamp);
        dest.writeInt(firstHalfStart);
        dest.writeInt(secondHalfStart);
        dest.writeString(round);
        dest.writeString(status);
        dest.writeString(statusShort);
        dest.writeInt(elapsed);
        dest.writeString(venue);
        dest.writeString(referee);
        dest.writeInt(goalsHomeTeam);
        dest.writeInt(goalsAwayTeam);
    }
}
