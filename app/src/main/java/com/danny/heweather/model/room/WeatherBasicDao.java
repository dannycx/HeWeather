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
public interface WeatherBasicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addWeatherBasic(WeatherBasic weatherBasic);

    @Query("select * from weather_basic")
    List<WeatherBasic> getWeatherBasics();

    @Query("select * from weather_basic where cityName like :cityName")
    WeatherBasic getWeatherBasic(String cityName);

    @Query("delete from weather_basic")
    void deleteWeatherBasics();

    @Query("delete from weather_basic where cityName like :cityName")
    void deleteWeatherBasic(String cityName);
}
