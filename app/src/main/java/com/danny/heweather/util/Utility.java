package com.danny.heweather.util;

import android.util.Log;

import com.danny.heweather.model.weather.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Log.i(TAG, "handleWeatherResponse: weatherContent = " + weatherContent);
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
