package com.danny.heweather.model.room;

/**
 * Created by danny on 1/5/18.
 */

public class WeatherBasicRepository implements WeatherBasicDataSource {
    private static WeatherBasicRepository INSTANCE = null;
    private RemoteWeatherBasicDataSource mRemoteWeatherBasicDataSource;

    private WeatherBasicRepository(RemoteWeatherBasicDataSource remoteWeatherBasicDataSource) {
        mRemoteWeatherBasicDataSource = remoteWeatherBasicDataSource;
    }

    public static WeatherBasicRepository getInstance(RemoteWeatherBasicDataSource remoteWeatherBasicDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherBasicRepository(remoteWeatherBasicDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void addWeatherBasic(WeatherBasic weatherBasic) {
        mRemoteWeatherBasicDataSource.addWeatherBasic(weatherBasic);
    }

    @Override
    public void getWeatherBasic(String cityName, LoadWeatherBasicsCallback callback) {
        mRemoteWeatherBasicDataSource.getWeatherBasic(cityName, callback);
    }

    @Override
    public void getWeatherBasics(LoadWeatherBasicsListCallback callback) {
        mRemoteWeatherBasicDataSource.getWeatherBasics(callback);
    }

    @Override
    public void deleteWeatherBasics() {
        mRemoteWeatherBasicDataSource.deleteWeatherBasics();
    }

    @Override
    public void deleteWeatherBasic(String cityName) {
        mRemoteWeatherBasicDataSource.deleteWeatherBasic(cityName);
    }
}
