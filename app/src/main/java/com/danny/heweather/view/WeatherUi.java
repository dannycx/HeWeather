package com.danny.heweather.view;


import com.danny.heweather.model.weather.Weather;

/**
 * MVP：view接口
 * Created by danny on 12/22/17.
 */

public interface WeatherUi {
    void showLoading();

    void hideLoading();

    void showError();

    void setWeatherInfo(Weather weather);

    void setWeatherAqi(Weather weather);
}
