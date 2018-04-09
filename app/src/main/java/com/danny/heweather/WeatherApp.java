package com.danny.heweather;

import android.app.Application;
import android.content.Context;

import com.dcx.style.citypickerview.CityPickerView;

/**
 * 天气app
 * Created by danny on 3/17/18.
 */

public class WeatherApp extends Application {
    private static Context context;
    private static WeatherApp instance;

    public static WeatherApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        CityPickerView.getInstance().init(this);
    }
}
