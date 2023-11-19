package com.example.myapplication.weather.time;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime {
    private LocalDateTime currentDateTime;
    private LocalDate currentDate;
    private String baseUlDate;
    private String baseUlTime;
    private String baseViDate;
    private String baseViTime;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public CurrentTime() {
        currentDateTime = LocalDateTime.now();
        currentDate = currentDateTime.toLocalDate();
        baseUlTime = changeTimeInUltra(currentDateTime.getHour());
        baseUlDate = currentDate.format(dateFormatter);
        baseViDate = currentDate.format(dateFormatter);

        changeTimeInVillage(currentDateTime.getHour());
    }


    private String changeTimeInUltra(int hour) {
        String time = "";

        if (hour < 10){
            time += "0" + String.valueOf(hour) + "00";
        }
        else if (hour > 10){
            time += String.valueOf(hour) + "00";
        }

        return time;
    }

    private void changeTimeInVillage(int hour) {

        if (hour >= 2 && hour < 5) {
            baseViTime = "0200";
        } else if (hour >= 5 && hour < 8) {
            baseViTime = "0500";
        } else if (hour >= 8 && hour < 11) {
            baseViTime = "0800";
        } else if (hour >= 11 && hour < 14) {
            baseViTime = "1100";
        } else if (hour >= 14 && hour < 17) {
            baseViTime = "1400";
        } else if (hour >= 17 && hour < 20) {
            baseViTime = "1700";
        } else if (hour >= 20 && hour < 23) {
            baseViTime = "2000";
        } else if (hour >= 23 && hour < 24) {
            baseViTime = "2300";
        } else if (hour >= 0 && hour < 2) {
            baseViTime = "2300";
            baseViDate = LocalDate.now().minusDays(1).format(dateFormatter);
        }
    }

    public String getBaseUlDate() {
        return baseUlDate;
    }

    public String getBaseUlTime() {
        return baseUlTime;
    }

    public String getBaseViDate() {
        return baseViDate;
    }

    public String getBaseViTime() {
        return baseViTime;
    }
}
