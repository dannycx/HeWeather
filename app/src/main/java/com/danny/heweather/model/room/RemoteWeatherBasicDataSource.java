package com.danny.heweather.model.room;


import com.danny.heweather.util.AppExecutors;

import java.util.List;

/**
 * Created by danny on 1/5/18.
 */

public class RemoteWeatherBasicDataSource implements WeatherBasicDataSource {
    private static volatile RemoteWeatherBasicDataSource INSTANCE;

    private WeatherBasicDao mWeatherBasicDao;
    private AppExecutors mAppExecutors;
    private List<WeatherBasic> mWeatherBasics;
    private WeatherBasic mWeatherBasic;

    private RemoteWeatherBasicDataSource(AppExecutors appExecutors, WeatherBasicDao weatherBasicDao) {
        mAppExecutors = appExecutors;
        mWeatherBasicDao = weatherBasicDao;
    }

    public static RemoteWeatherBasicDataSource getInstance(AppExecutors appExecutors, WeatherBasicDao weatherBasicDao) {
        if (INSTANCE == null) {
            synchronized (RemoteWeatherBasicDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteWeatherBasicDataSource(appExecutors, weatherBasicDao);
                }
            }
        }
        return INSTANCE;
    }

    //添加天气基本信息
    @Override
    public void addWeatherBasic(final WeatherBasic weatherBasic) {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherBasicDao.addWeatherBasic(weatherBasic);
            }
        });
    }

    //查看所有天气基本信息
    @Override
    public void getWeatherBasics(final LoadWeatherBasicsListCallback callback) {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherBasics = mWeatherBasicDao.getWeatherBasics();
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mWeatherBasics.isEmpty()) {
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
                                    callback.onWeatherBasicsLoaded(mWeatherBasics);
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

    //获取指定城市天气基本信息
    @Override
    public void getWeatherBasic(final String cityName, final LoadWeatherBasicsCallback callback) {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherBasic = mWeatherBasicDao.getWeatherBasic(cityName);
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mWeatherBasic != null) {
                            if (callback != null) {
                                try {
                                    callback.onWeatherBasicsLoaded(mWeatherBasic);
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

    //删除天气基本信息
    @Override
    public void deleteWeatherBasics() {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherBasicDao.deleteWeatherBasics();
            }
        });
    }

    @Override
    public void deleteWeatherBasic(final String cityName) {
        mAppExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWeatherBasicDao.deleteWeatherBasic(cityName);
            }
        });
    }
}
