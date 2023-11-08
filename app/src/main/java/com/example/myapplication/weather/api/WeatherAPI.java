package com.example.myapplication.weather.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface WeatherAPI {
    /** 코드의 반복을 줄이기 위한 인터페이스이다.
     * 초단기예보, 단기예보, 중기예보 모두 기본적인 로직은 비슷하나 디테일한 부분이 다르기 때문에
     * getAPI() 함수를 오버라이딩하여 API를 받아 올 수 있다.
     * 이 작업을 통해 원하는 예보의 객체를 코드의 반복없이 생성할 수 있다
     * ex) 초단기예보 객체 생성
     * weatherAPI weather = new ultraSrtNcstAPI();
     */
    public void getAPI() throws IOException;

    public void saveItem();

    public void parseItem(JSONObject jsonObject) throws JSONException;
}
