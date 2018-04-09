package com.danny.heweather.model.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by danny on 17-11-14.
 */
@Entity(tableName = "weather_basic")
public class WeatherBasic {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date;
    public String cityName;
    public String weatherBasic;

    @Override
    public String toString() {
        return "WeatherBasic{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", cityName='" + cityName + '\'' +
                ", weatherBasic='" + weatherBasic + '\'' +
                '}';
    }
}
