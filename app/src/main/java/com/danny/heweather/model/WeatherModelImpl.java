package com.danny.heweather.model;

import android.util.Log;

import com.danny.heweather.WeatherApp;
import com.danny.heweather.model.room.WeatherAqi;
import com.danny.heweather.model.room.WeatherAqiInjection;
import com.danny.heweather.model.room.WeatherAqiRepository;
import com.danny.heweather.model.room.WeatherBasic;
import com.danny.heweather.model.room.WeatherBasicInjection;
import com.danny.heweather.model.room.WeatherBasicRepository;
import com.danny.heweather.model.weather.Weather;
import com.danny.heweather.presenter.OnWeatherListener;
import com.danny.heweather.util.HttpUtil;
import com.danny.heweather.util.Utility;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;


/**
 * Created by danny on 12/21/17.
 */

public class WeatherModelImpl implements WeatherModel {
    // bc0418b57b2d4918819d3974ac1285d9     3000次 7天预报
    // 537664b7e2124b3c845bc0b51278d4af     1000次 3天预报
    public static final String TAG = WeatherModelImpl.class.getSimpleName();
    private static String key = "537664b7e2124b3c845bc0b51278d4af";
    private WeatherBasicRepository mWeatherBasicRepository;
    private WeatherAqiRepository mWeatherAqiRepository;

    /**
     * 加载城市天气
     *
     * @param name     城市名称
     * @param listener 获取数据结果监听
     */
    @Override
    public void loadWeather(final String name, final OnWeatherListener listener) {
        mWeatherBasicRepository = WeatherBasicInjection.getNoteRepository(WeatherApp.getContext());
        mWeatherBasicRepository.deleteWeatherBasic(name + "%");
        String address = "https://free-api.heweather.com/s6/weather?location=" + name + "&key=" + key;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "loadWeather - onResponse: " + "获取天气信息失败");
                if (listener != null) {
                    try {
                        listener.onError();
                    } catch (NullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseText = response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseText);
                if (weather != null && "ok".equals(weather.status)) {
                    Log.d(TAG, "loadWeather - onResponse: " + "获取天气信息成功");
                    WeatherBasic basic = new WeatherBasic();
                    basic.cityName = name;
                    basic.weatherBasic = responseText;
                    basic.date = new Date().toString();
                    mWeatherBasicRepository.addWeatherBasic(basic);
                    if (listener != null) {
                        try {
                            listener.onSuccess(weather);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.d(TAG, "loadWeather - onResponse: " + "获取天气信息失败");
                    if (listener != null) {
                        try {
                            listener.onError();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * 加载城市空气质量
     *
     * @param cityName 城市名称
     * @param listener 获取数据结果监听
     */
    @Override
    public void loadWeatherAqi(final String cityName, final OnWeatherListener listener) {
        mWeatherAqiRepository = WeatherAqiInjection.getInstance(WeatherApp.getContext());
        mWeatherAqiRepository.deleteWeatherAqi(cityName + "%");
        String address = "https://free-api.heweather.com/s6/air/now?location=" + cityName + "&key=" + key;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "loadWeatherAqi - onResponse: " + "获取空气质量信息失败");
                if (listener != null) {
                    try {
                        listener.onError();
                    } catch (NullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseText = response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseText);
                if (weather != null && "ok".equals(weather.status)) {
                    Log.d(TAG, "loadWeatherAqi - onResponse: " + "获取空气质量信息成功");
                    WeatherAqi aqi = new WeatherAqi();
                    aqi.cityName = cityName;
                    aqi.date = new Date().toString();
                    aqi.weatherAqi = responseText;
                    mWeatherAqiRepository.addWeatherAqi(aqi);
                    if (listener != null) {
                        try {
                            listener.onSuccessAqi(weather);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.d(TAG, "loadWeatherAqi - onResponse: " + "获取空气质量信息失败");
                    if (listener != null) {
                        try {
                            listener.onError();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
