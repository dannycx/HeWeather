package com.danny.heweather.model.room;

import java.util.List;

/**
 * 对空气质量进行操作数据库接口
 * Created by danny on 1/5/18.
 */

public interface WeatherAqiDataSource {
    void addWeatherAqi(WeatherAqi weatherAqi);

    void getWeatherAqis(LoadWeatherAqisListCallback callback);

    void getWeatherAqi(String cityName, LoadWeatherAqisCallback callback);

    void deleteWeatherAqis();

    void deleteWeatherAqi(String cityName);

    interface LoadWeatherAqisListCallback {
        void onWeatherAqisListLoaded(List<WeatherAqi> weatherAqis);

        void onDataNotAvailable();
    }

    interface LoadWeatherAqisCallback {
        void onWeatherAqisLoaded(WeatherAqi weatherAqi);

        void onDataNotAvailable();
    }
}
