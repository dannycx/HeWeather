package com.danny.heweather.model.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by danny on 1/5/18.
 */
@Dao
public interface WeatherAqiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addWeatherAqi(WeatherAqi weatherAqi);

    @Query("select * from weather_aqi")
    List<WeatherAqi> getWeatherAqis();

    @Query("select * from weather_aqi where cityName like :cityName")
    WeatherAqi getWeatherAqi(String cityName);

    @Query("delete from weather_aqi")
    void deleteWeatherAqis();

    @Query("delete from weather_aqi where cityName like :cityName")
    void deleteWeatherAqi(String cityName);
}
