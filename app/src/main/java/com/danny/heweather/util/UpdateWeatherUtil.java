package com.danny.heweather.util;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.danny.heweather.WeatherApp;
import com.danny.heweather.model.room.WeatherAqi;
import com.danny.heweather.model.room.WeatherAqiDataSource;
import com.danny.heweather.model.room.WeatherAqiInjection;
import com.danny.heweather.model.room.WeatherAqiRepository;
import com.danny.heweather.model.room.WeatherBasic;
import com.danny.heweather.model.room.WeatherBasicDataSource;
import com.danny.heweather.model.room.WeatherBasicInjection;
import com.danny.heweather.model.room.WeatherBasicRepository;
import com.danny.heweather.model.weather.Weather;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * 定时更新数据库工具类
 * Created by danny on 1/8/18.
 */

public class UpdateWeatherUtil {
    private static final String TAG = WeatherUtil.class.getSimpleName();
    private static WeatherBasicRepository mWeatherBasicRepository;
    private static WeatherAqiRepository mWeatherAqiRepository;

    //更新数据库数据
    public static void updateWeather(/*final String currentCity*/) {
        Log.d(TAG, "updateWeather: ");
        mWeatherBasicRepository = WeatherBasicInjection.getNoteRepository(WeatherApp.getContext());
        mWeatherBasicRepository.getWeatherBasics(new WeatherBasicDataSource.LoadWeatherBasicsListCallback() {
            @Override
            public void onWeatherBasicsLoaded(List<WeatherBasic> weatherBasic) {
                for (WeatherBasic basic : weatherBasic) {
                    Log.d(TAG, "onWeatherBasicsLoaded: ");
                    if (new Date().getTime() - new Date(basic.date).getTime() >= 1000 * 30 * 60) {
                        final String name = basic.cityName;
                        mWeatherBasicRepository.deleteWeatherBasic(name);
                        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + name + "&key=537664b7e2124b3c845bc0b51278d4af";
                        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                            @Override
                            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                final String responseText = response.body().string();
                                Weather weather = Utility.handleWeatherResponse(responseText);
                                if (weather != null && "ok".equals(weather.status)) {
                                    WeatherBasic basic = new WeatherBasic();
                                    basic.cityName = name;
                                    basic.date = new Date().toString();
                                    basic.weatherBasic = responseText;
                                    mWeatherBasicRepository.addWeatherBasic(basic);
                                    Log.d(TAG, "更新" + name + "天气信息");
                                }
                            }

                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }
                        });
                        Intent intent1 = new Intent("android.intent.action.CART_BROADCAST");
                        LocalBroadcastManager.getInstance(WeatherApp.getContext()).sendBroadcast(intent1);
                    }
                }
            }

            @Override
            public void onDataNotAvailable() {
            }
        });

        mWeatherAqiRepository = WeatherAqiInjection.getInstance(WeatherApp.getContext());
        mWeatherAqiRepository.getWeatherAqis(new WeatherAqiDataSource.LoadWeatherAqisListCallback() {
            @Override
            public void onWeatherAqisListLoaded(List<WeatherAqi> weatherAqis) {
                for (WeatherAqi weatherAqi : weatherAqis) {
                    if ((new Date().getTime() - new Date(weatherAqi.date).getTime()) >= 1000 * 60 * 30) {
                        String name = weatherAqi.cityName;
                        mWeatherAqiRepository.deleteWeatherAqi(name);
                        saveAqi(name);
                    }
                }
            }

            @Override
            public void onDataNotAvailable() {
            }
        });
    }

    //网络获取数据保存到本地
    private static void saveBasic(final String name) {
        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + name + "&key=537664b7e2124b3c845bc0b51278d4af";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String responseText = response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseText);
                if (weather != null && "ok".equals(weather.status)) {
                    WeatherBasic basic = new WeatherBasic();
                    basic.cityName = name;
                    basic.date = new Date().toString();
                    basic.weatherBasic = responseText;
                    mWeatherBasicRepository.addWeatherBasic(basic);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    //网络获取数据保存到本地
    private static void saveAqi(final String name) {
        String weatherUrl = "https://free-api.heweather.com/s6/air/now?location=" + name + "&key=537664b7e2124b3c845bc0b51278d4af";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String responseText = response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseText);
                if (weather != null && "ok".equals(weather.status)) {
                    WeatherAqi aqi = new WeatherAqi();
                    aqi.cityName = name;
                    aqi.date = new Date().toString();
                    aqi.weatherAqi = responseText;
                    mWeatherAqiRepository.addWeatherAqi(aqi);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
