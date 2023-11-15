package com.example.myapplication.alarm;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.sports.model.AwayTeam;
import com.example.myapplication.sports.model.HomeTeam;
import com.example.myapplication.sports.model.League;
import com.example.myapplication.sports.model.Score;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class Alarm implements Parcelable {
    private int alarmId;
    private String time;

    // 생성자, getter, setter 등의 메서드...
    public Alarm(int alarmId, String time) {
        this.alarmId = alarmId;
        this.time = time;
    }

    // Parcelable 구현
    protected Alarm(String time) {
        alarmId = (int) System.currentTimeMillis();
        this.time = time;
    }

    public Alarm(Parcel in) {
        alarmId = in.readInt(); // 변경: int에서 long으로 변경
        time = in.readString();
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String time) {
        this.time = time;
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(alarmId);
        dest.writeString(time);
    }
}
