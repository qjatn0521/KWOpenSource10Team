package com.example.myapplication.weather.data;

import java.util.ArrayList;
import java.util.List;

public class UltraSrtNcstData {
    private List<String> baseDate;
    private List<String> baseTime;
    private List<String> category;
    private List<String> obsrValue;

    // 기본 생성자로 arrayList 객체 생성
    public UltraSrtNcstData(){
        List<String> baseDate = new ArrayList<>();
        List<String> baseTime = new ArrayList<>();
        List<String> category = new ArrayList<>();
        List<Double> obsrValue = new ArrayList<>();
    }

    // 필드는 생성자를 통해서만 추가될 수 있다. Setter 사용 불가
    public UltraSrtNcstData(String baseDate, String baseTime, String category,
                            String obsrValue) {
        this.baseDate.add(baseDate);
        this.baseTime.add(baseTime);
        this.category.add(category);
        this.obsrValue.add(obsrValue);
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

    public List<String> getobsrValue() {
        return obsrValue;
    }
}
