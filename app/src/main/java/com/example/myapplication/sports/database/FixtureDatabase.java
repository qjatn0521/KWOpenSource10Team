package com.example.myapplication.sports.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FixtureDB.class}, version = 1)
public abstract class FixtureDatabase extends RoomDatabase {
    public abstract FixtureDBDao fixtureDao();

    private static FixtureDatabase instance;

    public static synchronized FixtureDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            FixtureDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}