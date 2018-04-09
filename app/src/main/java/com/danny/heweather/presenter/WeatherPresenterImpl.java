package com.danny.heweather.presenter;


import com.danny.heweather.model.WeatherModel;
import com.danny.heweather.model.WeatherModelImpl;
import com.danny.heweather.model.weather.Weather;
import com.danny.heweather.view.WeatherUi;

/**
 * Presenter作为中间层，持有View和Model的引用
 * Created by danny on 12/21/17.
 */

public class WeatherPresenterImpl implements WeatherPresenter, OnWeatherListener {
    private WeatherUi mWeatherUi;
    private WeatherModel mWeatherModel;

    public WeatherPresenterImpl(WeatherUi weatherUi) {
        this.mWeatherUi = weatherUi;
        mWeatherModel = new WeatherModelImpl();
    }

    @Override
    public void getWeather(String name) {
        mWeatherUi.showLoading();
        mWeatherModel.loadWeather(name, this);
    }

    @Override
    public void getWeatherAqi(String cityName) {
        mWeatherUi.showLoading();
        mWeatherModel.loadWeatherAqi(cityName, this);
    }

    @Override
    public void onSuccess(Weather weather) {
        mWeatherUi.hideLoading();
        mWeatherUi.setWeatherInfo(weather);
    }

    @Override
    public void onSuccessAqi(Weather weather) {
        mWeatherUi.hideLoading();
        mWeatherUi.setWeatherAqi(weather);
    }

    @Override
    public void onError() {
        mWeatherUi.hideLoading();
        mWeatherUi.showError();
    }
}
