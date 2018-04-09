package com.danny.heweather.util;

import android.util.Log;

import com.danny.heweather.WeatherApp;
import com.danny.heweather.model.room.WeatherAqi;
import com.danny.heweather.model.room.WeatherAqiInjection;
import com.danny.heweather.model.room.WeatherAqiRepository;
import com.danny.heweather.model.room.WeatherBasic;
import com.danny.heweather.model.room.WeatherBasicInjection;
import com.danny.heweather.model.room.WeatherBasicRepository;
import com.danny.heweather.model.weather.Weather;
import com.danny.heweather.presenter.ReturnWeather;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * 语音加载天气回调工具类
 * Created by ryan on 17-12-23.
 * Email: Ryan_chan01212@yeah.net
 */

public class WeatherUtil {
    private static final String TAG = WeatherUtil.class.getSimpleName();
    private static Weather mWeather;
    private static Weather mWeatherAqi;
    //537664b7e2124b3c845bc0b51278d4af 开发    bc0418b57b2d4918198d3974ac1285d9 测试
    private static String key = "537664b7e2124b3c845bc0b51278d4af";
    private static WeatherBasicRepository mWeatherBasicRepository;
    private static WeatherAqiRepository mWeatherAqiRepository;

    public static void loadWeather(final String name, final ReturnWeather returnWeather) {
        mWeatherBasicRepository = WeatherBasicInjection.getNoteRepository(WeatherApp.getContext());
        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + name + "&key=" + key;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String responseText = response.body().string();
                mWeather = Utility.handleWeatherResponse(responseText);
                //天气获取成功，则保存
                if (mWeather != null && "ok".equals(mWeather.status)) {
                    if (returnWeather != null) {
                        returnWeather.onReturnWeather(mWeather);
                    }
                    Log.d(TAG, "loadWeather - onResponse: " + "获取天气信息成功");
                    WeatherBasic basic = new WeatherBasic();
                    basic.cityName = name;
                    basic.date = new Date().toString();
                    basic.weatherBasic = responseText;
                    mWeatherBasicRepository.addWeatherBasic(basic);
                } else {
                    if (returnWeather != null) {
                        returnWeather.onReturnWeatherError();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (returnWeather != null) {
                    returnWeather.onReturnWeatherError();
                }
            }
        });
    }

    public static void loadWeatherAqi(final String cityName, final ReturnWeather returnWeather) {
        mWeatherAqiRepository = WeatherAqiInjection.getInstance(WeatherApp.getContext());
        String weatherUrl = "https://free-api.heweather.com/s6/air/now?location=" + cityName + "&key=" + key;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String responseText = response.body().string();
                mWeatherAqi = Utility.handleWeatherResponse(responseText);
                //天气获取成功，则保存
                if (mWeatherAqi != null && "ok".equals(mWeatherAqi.status)) {
                    if (returnWeather != null) {
                        returnWeather.onReturnWeather(mWeatherAqi);
                    }
                    Log.d(TAG, "loadWeatherAqi - onResponse: " + "获取空气质量信息成功");
                    WeatherAqi aqi = new WeatherAqi();
                    aqi.cityName = cityName;
                    aqi.date = new Date().toString();
                    aqi.weatherAqi = responseText;
                    mWeatherAqiRepository.addWeatherAqi(aqi);
                } else {
                    if (returnWeather != null) {
                        returnWeather.onReturnWeatherError();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (returnWeather != null) {
                    returnWeather.onReturnWeatherError();
                }
            }
        });
    }
}
