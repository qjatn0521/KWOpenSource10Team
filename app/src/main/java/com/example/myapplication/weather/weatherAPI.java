package com.example.myapplication.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONArray.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject.*;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;

public class weatherAPI {
    public void exploreAPI() throws IOException {

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        String endKey = "WGbZ3y8YenvWEK4%2FwabF0QlpEw7Noxa3vg5aso798whVG8O7rV3ZqyP%2BmL44LY4ouI4LjZOJf8GbBgGR5kRp4g%3D%3D";

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + endKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20231105", "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0200", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("55", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/

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

        // Debug
        System.out.println("API 작동 테스트");
        System.out.println(sb.toString());
        String result = sb.toString();

        // API 테스트
        // LocalDateTime t = LocalDateTime.now().minusMinutes(30);
        //=======이 밑에 부터는 json에서 데이터 파싱해 오는 부분이다=====//

        // response 키를 가지고 데이터를 파싱
        System.out.println("API 파싱 테스트");
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
            String weather;
            String tmperature;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj_4 = jsonArray.getJSONObject(i);
                Log.d("TagTagTagasd", jsonObj_4.toString());
                String category = jsonObj_4.getString("category");
                Log.d("TagTagTag", category);
            }
        } catch (JSONException e){
            System.out.println("e = " + e);
        }
    }
}