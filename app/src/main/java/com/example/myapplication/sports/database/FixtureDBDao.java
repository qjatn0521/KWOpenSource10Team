package com.example.myapplication.sports.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FixtureDBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFixture(FixtureDB fixture);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFixtures(List<FixtureDB> fixtures);

    @Query("SELECT * FROM fixtures WHERE teamId = :teamId")
    List<FixtureDB> getFixturesByTeamId(int teamId);

    @Query("SELECT COUNT(*) FROM fixtures WHERE teamId = :teamId")
    int hasFixturesForTeam(int teamId);

    @Query("DELETE FROM fixtures WHERE teamId = :teamId")
    void deleteFixturesByTeamId(int teamId);

    @Query("SELECT * FROM fixtures WHERE dateString = :currentDate")
    List<FixtureDB> getFixturesForToday(String currentDate);

    @Query("SELECT * FROM fixtures WHERE date >= :currentDate ORDER BY date LIMIT 1")
    FixtureDB getEarliestFixture(String currentDate);

    @Query("SELECT * FROM fixtures WHERE dateString = (SELECT MIN(dateString) FROM fixtures WHERE date >= :currentDate) ORDER BY date")
    List<FixtureDB> getEarliestFixtureAndSameDateFixtures(String currentDate);


    @Delete
    void deleteFixture(FixtureDB fixture);

}