package com.example.myapplication.sports.database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fixtures")
public class FixtureDB {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int teamId;
    public String homeTeamName;
    public String awayTeamName;
    public String date;
    public String dateString;
    public String timeString;
    public String homeTeamlogo;
    public String awayTeamlogo;
    public int fixtureId;

}