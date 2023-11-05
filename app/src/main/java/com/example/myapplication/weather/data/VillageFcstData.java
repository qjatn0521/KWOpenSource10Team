package com.example.myapplication.weather.data;

import java.util.ArrayList;
import java.util.List;

public class VillageFcstData {
    /** 배열로 모아서 한꺼번에 저장하자 */

    private List<String> baseDate;
    private List<String> baseTime;
    private List<String> category;
    private List<String> fcstDate;
    private List<String> fcstTime;
    private List<String> fcstValue;

    // 기본 생성자로 arrayList 객체 생성
    public VillageFcstData(){
        List<String> baseDate = new ArrayList<>();
        List<String> baseTime = new ArrayList<>();
        List<String> category = new ArrayList<>();
        List<String> fcstDate = new ArrayList<>();
        List<String> fcstTime = new ArrayList<>();
        List<String> fcstValue = new ArrayList<>();
    }

    // 필드는 생성자를 통해서만 추가될 수 있다. Setter 사용 불가
    public VillageFcstData(String baseDate, String baseTime, String category,
                           String fcstDate, String fcstTime, String fcstValue) {
        this.baseDate.add(baseDate);
        this.baseTime.add(baseTime);
        this.category.add(category);
        this.fcstDate.add(fcstDate);
        this.fcstTime.add(fcstTime);
        this.fcstValue.add(fcstValue);
    }

    public List<String> getBaseDate() {
        return baseDate;
    }

    public List<String> getBaseTime() {
        return baseTime;
    }

    public List<String> getCategory() {
        return category;
    }

    public List<String> getFcstDate() {
        return fcstDate;
    }

    public List<String> getFcstTime() {
        return fcstTime;
    }

    public List<String> getFcstValue() {
        return fcstValue;
    }
}
