package com.example.myapplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.json.JSONObject;
import org.json.*;

import static org.junit.Assert.*;

import com.example.myapplication.weather.weatherAPI;

import java.io.IOException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        weatherAPI weather = new weatherAPI();
        try {
            weather.exploreAPI();
        } catch (Exception e){
            System.out.println("e = " + e);
        }
    }
}