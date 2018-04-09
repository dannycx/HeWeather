package com.danny.heweather.presenter;


import com.danny.heweather.model.weather.Weather;

/**
 * Created by danny on 12/21/17.
 */

public interface OnWeatherListener {
    /**
     * 天气信息成功时回调
     *
     * @param weather
     */
    void onSuccess(Weather weather);

    /**
     * 天气质量成功时回调
     *
     * @param weather
     */
    void onSuccessAqi(Weather weather);

    /**
     * 失败时回调
     */
    void onError();
}
