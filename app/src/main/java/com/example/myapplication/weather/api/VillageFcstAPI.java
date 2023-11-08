package com.example.myapplication.weather.api;

import android.util.Log;

import com.example.myapplication.weather.domain.VillageFcstData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class VillageFcstAPI implements WeatherAPI {
    private StringBuilder urlBuilder;
    private String baseDate, baseTime, category, fcstDate, fcstTime, fcstValue;
    private final String endKey = "WGbZ3y8YenvWEK4%2FwabF0QlpEw7Noxa3vg5aso798whVG8O7rV3ZqyP%2BmL44LY4ouI4LjZOJf8GbBgGR5kRp4g%3D%3D"; /* 인코딩 키 */

    // API에 맞는 파라미터를 적용하여 URL을 생성하는 함수
    public VillageFcstAPI(String baseDate, String baseTime, String nx, String ny) {
        this.urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /* 단기예보 URL*/

        try {
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + endKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*‘21년 6월 28일 발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /*06시 발표(정시단위) */
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/
        } catch (IOException e){
            System.out.println("e = " + e);
        }
    }

    public StringBuilder getUrlBuilder() {
        return urlBuilder;
    }

    @Override
    public void parseItem(JSONObject jsonObj_4) throws JSONException {
        this.baseDate = jsonObj_4.getString("baseDate");
        this.baseTime = jsonObj_4.getString("baseTime");
        this.category = jsonObj_4.getString("category");
        this.fcstDate = jsonObj_4.getString("fcstDate");
        this.fcstTime = jsonObj_4.getString("fcstTime");
        this.fcstValue = jsonObj_4.getString("fcstValue");
    }

    @Override
    public void saveItem(){
        VillageFcstData data = new VillageFcstData(baseDate, baseTime, category,
                fcstDate, fcstTime, fcstValue);
    }

    @Override
    public void getAPI() throws IOException {
        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        String result = sb.toString();

        // API 테스트
        // LocalDateTime t = LocalDateTime.now().minusMinutes(30);
        //=======이 밑에 부터는 json에서 데이터 파싱해 오는 부분이다=====//
        try {
            System.out.println(result);
            JSONObject jsonObj_1 = new JSONObject(result);

            System.out.println("jsonObj_1 = " + jsonObj_1);
            String response = jsonObj_1.getString("response");
            System.out.println("response = " + response);

            // response 로 부터 body 찾기
            JSONObject jsonObj_2 = new JSONObject(response);
            String body = jsonObj_2.getString("body");
            System.out.println("body = " + body);

            // body 로 부터 items 찾기
            JSONObject jsonObj_3 = new JSONObject(body);
            String items = jsonObj_3.getString("items");

            // items로 부터 itemlist 를 받기
            JSONObject jsonObj_4 = new JSONObject(items);
            JSONArray jsonArray = jsonObj_4.getJSONArray("item");

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj_4 = jsonArray.getJSONObject(i);
                Log.d("TagTagTagasd", jsonObj_4.toString());
                parseItem(jsonObj_4);
                saveItem();
            }
        } catch (JSONException e){
            System.out.println("e = " + e);
        }
    }
}
