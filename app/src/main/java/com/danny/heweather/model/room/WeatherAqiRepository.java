package com.danny.heweather.model.room;

/**
 * Created by danny on 1/5/18.
 */

public class WeatherAqiRepository implements WeatherAqiDataSource {
    private static WeatherAqiRepository INSTANCE = null;
    private RemoteWeatherAqiDataSource mRemoteWeatherAqiDataSource;

    private WeatherAqiRepository(RemoteWeatherAqiDataSource remoteWeatherAqiDataSource) {
        mRemoteWeatherAqiDataSource = remoteWeatherAqiDataSource;
    }

    public static WeatherAqiRepository getInstance(RemoteWeatherAqiDataSource remoteWeatherAqiDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherAqiRepository(remoteWeatherAqiDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void addWeatherAqi(WeatherAqi weatherAqi) {
        mRemoteWeatherAqiDataSource.addWeatherAqi(weatherAqi);
    }

    @Override
    public void getWeatherAqis(LoadWeatherAqisListCallback callback) {
        mRemoteWeatherAqiDataSource.getWeatherAqis(callback);
    }

    @Override
    public void getWeatherAqi(String cityName, LoadWeatherAqisCallback callback) {
        mRemoteWeatherAqiDataSource.getWeatherAqi(cityName, callback);
    }

    @Override
    public void deleteWeatherAqis() {
        mRemoteWeatherAqiDataSource.deleteWeatherAqis();
    }

    @Override
    public void deleteWeatherAqi(String cityName) {
        mRemoteWeatherAqiDataSource.deleteWeatherAqi(cityName);
    }
}
