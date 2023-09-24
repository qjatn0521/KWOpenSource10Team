package com.example.myapplication.sports;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class CustomSharedPreferences {
    private static final String COUNTRY_ID = "country_id";
    private static final String PREFERENCES_TIME = "preferences_time";
    private static final String RB_ID = "rb_id";
    private static SharedPreferences sharedPreferences;
    private static volatile CustomSharedPreferences instance;
    private static final Object lock = new Object();

    CustomSharedPreferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static CustomSharedPreferences getInstance(Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new CustomSharedPreferences(context);
                }
            }
        }
        return instance;
    }

    public void saveCountryId(int countryId) {
        sharedPreferences.edit().putInt(COUNTRY_ID, countryId).apply();
    }

    public Integer getCountryId() {
        return sharedPreferences.getInt(COUNTRY_ID, 524);
    }

    public void saveRbCountry(int rb) {
        sharedPreferences.edit().putInt(RB_ID, rb).apply();
    }

    public Integer getRbCountry() {
        return sharedPreferences.getInt(RB_ID, 0);
    }

    public void saveTime(long time) {
        sharedPreferences.edit().putLong(PREFERENCES_TIME, time).apply();
    }

    public Long getTime() {
        return sharedPreferences.getLong(PREFERENCES_TIME, 0);
    }
}
