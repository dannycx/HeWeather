package com.danny.heweather.model.room;

import android.content.Context;

import com.danny.heweather.model.WeatherDatabase;
import com.danny.heweather.util.AppExecutors;

/**
 * Created by danny on 1/6/18.
 */

public class WeatherAqiInjection {
    public static WeatherAqiRepository getInstance(Context context) {
        WeatherDatabase weatherDatabase = WeatherDatabase.getInstance(context);
        return WeatherAqiRepository.getInstance(RemoteWeatherAqiDataSource.getInstance(new AppExecutors(), weatherDatabase.weatherAqiDao()));
    }
}
