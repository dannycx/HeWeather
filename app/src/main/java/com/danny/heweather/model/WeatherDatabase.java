package com.danny.heweather.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.danny.heweather.model.room.WeatherAqi;
import com.danny.heweather.model.room.WeatherAqiDao;
import com.danny.heweather.model.room.WeatherBasic;
import com.danny.heweather.model.room.WeatherBasicDao;


/**
 * Created by derik on 17-12-8.
 * Email: lionel.lp.wu@mail.foxconn.com
 */

@Database(entities = {WeatherBasic.class, WeatherAqi.class}, version = 1, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static WeatherDatabase INSTANCE;

    public static WeatherDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherDatabase.class, "weather.db").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract WeatherBasicDao weatherBasicDao();

    public abstract WeatherAqiDao weatherAqiDao();

    public void close() {
        if (INSTANCE != null) {
            INSTANCE.close();
        }
    }

}
