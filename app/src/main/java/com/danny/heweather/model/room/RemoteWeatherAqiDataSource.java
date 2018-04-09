package com.danny.heweather.model.room;


import com.danny.heweather.util.AppExecutors;

import java.util.List;

/**
 * Created by danny on 1/5/18.
 */

public class RemoteWeatherAqiDataSource implements WeatherAqiDataSource {
    private static volatile RemoteWeatherAqiDataSource INSTANCE = null;
    private WeatherAqiDao mWeatherAqiDao;
    private List<WeatherAqi> mWeatherAqis;
    private WeatherAqi mWeatherAqi;
    private AppExecutors mAppExecutors;

    private RemoteWeatherAqiDataSource(AppExecutors appExecutors, WeatherAqiDao weatherAqiDao) {
        mAppExecutors = appExecutors;
        mWeatherAqiDao = weatherAqiDao;
    }

    public static RemoteWeatherAqiDataSource getInstance(AppExecutors appExecutors, WeatherAqiDao weatherAqiDao) {
        if (INSTANCE == null) {
            synchronized (RemoteWeatherAqiDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteWeatherAqiDataSource(appExecutors, weatherAqiDao);
                }
            }
        }
        return INSTANCE;
    }

    //添加空气质量信息
    @Override
    public void addWeatherAqi(final WeatherAqi weatherAqi) {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherAqiDao.addWeatherAqi(weatherAqi);
            }
        });
    }

    //获取所有空气质量信息
    @Override
    public void getWeatherAqis(final LoadWeatherAqisListCallback callback) {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherAqis = mWeatherAqiDao.getWeatherAqis();
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mWeatherAqis.isEmpty()) {
                            if (callback != null) {
                                try {
                                    callback.onDataNotAvailable();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (callback != null) {
                                try {
                                    callback.onWeatherAqisListLoaded(mWeatherAqis);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    //获取指定城市空气质量信息
    @Override
    public void getWeatherAqi(final String cityName, final LoadWeatherAqisCallback callback) {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherAqi = mWeatherAqiDao.getWeatherAqi(cityName);
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mWeatherAqi != null) {
                            if (callback != null) {
                                try {
                                    callback.onWeatherAqisLoaded(mWeatherAqi);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (callback != null) {
                                try {
                                    callback.onDataNotAvailable();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    //删除空气质量信息
    @Override
    public void deleteWeatherAqis() {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherAqiDao.deleteWeatherAqis();
            }
        });
    }

    @Override
    public void deleteWeatherAqi(final String cityName) {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherAqiDao.deleteWeatherAqi(cityName);
            }
        });
    }
}
