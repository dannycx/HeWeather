package com.danny.heweather.presenter;


import com.danny.heweather.model.weather.Weather;

/**
 * Created by danny on 18-1-15.
 */
//监听从网络获取Weather接口
public interface ReturnWeather {
    void onReturnWeather(Weather weather);

    void onReturnWeatherError();
}
