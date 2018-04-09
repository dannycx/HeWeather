package com.danny.heweather.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danny.heweather.R;
import com.danny.heweather.model.room.WeatherAqi;
import com.danny.heweather.model.room.WeatherAqiDataSource;
import com.danny.heweather.model.room.WeatherAqiInjection;
import com.danny.heweather.model.room.WeatherAqiRepository;
import com.danny.heweather.model.room.WeatherBasic;
import com.danny.heweather.model.room.WeatherBasicDataSource;
import com.danny.heweather.model.room.WeatherBasicInjection;
import com.danny.heweather.model.room.WeatherBasicRepository;
import com.danny.heweather.model.weather.Weather;
import com.danny.heweather.presenter.WeatherPresenter;
import com.danny.heweather.presenter.WeatherPresenterImpl;
import com.danny.heweather.util.HandlerWeatherUtil;
import com.danny.heweather.util.NetStatusUtils;
import com.danny.heweather.util.ToastUtils;
import com.danny.heweather.util.Utility;
import com.dcx.Interface.OnCityItemClickListener;
import com.dcx.bean.CityBean;
import com.dcx.bean.DistrictBean;
import com.dcx.bean.ProvinceBean;
import com.dcx.citywheel.CityConfig;
import com.dcx.style.citypickerview.CityPickerView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherActivity extends AppCompatActivity implements WeatherUi {
    private static final String TAG = WeatherActivity.class.getSimpleName();
    private static String mSelectProvince = "北京";
    private static String mSelectCity = "北京";
    private static String mSelectCounty = "北京";
    public SwipeRefreshLayout mRefreshWeather;
    public WeatherPresenter mWeatherPresenter;
    private ImageView mWeatherSelectCity;
    private ImageView mWeatherNowIcon, mTomorrowIcon, mAfterIcon;
    private Dialog loadingDialog;
    private TextView mCond, mTemperature,
            mDate, mTime, mTitle, mTomorrow, mTomorrowTem, mAfter, mAfterTem, mLifestyleClothes, mLifestyleCar,
            mLifestyleAir, mPM25, mNO2, mCO, mVTomorrowWeek, mVAfterWeek;
    private LinearLayout mAirLayout;
    private String mCurrentCounty = "深圳";
    private String mCurrentCity = "深圳";
    private Resources mResources;

    private Timer mTimer;
    private TimerTask mTask;

    private WeatherBasicRepository mWeatherBasicRepository;
    private WeatherAqiRepository mWeatherAqiRepository;

    /**
     * 定时更新天气界面
     */
    private void initTimer() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(mCurrentCounty)) {
                    getWeatherBasic(mCurrentCity);
                } else {
                    getWeatherBasic(mCurrentCounty);
                }
                if (judgeCityAqiIsQuery()) {
                    getWeatherAqi(mCurrentCity);
                }
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 60 * 1000, 5 * 60 * 1000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mResources = getResources();
        initView();
        //判断当前屏幕方向
        if (2 != getResources().getConfiguration().orientation) {//竖屏
            mVTomorrowWeek = findViewById(R.id.weather_vertical_forecast_tomorrow_week);
            mVAfterWeek = findViewById(R.id.weather_vertical_forecast_after_week);
        }

        if (savedInstanceState != null) {
            mCurrentCity = savedInstanceState.getString("city");
            mCurrentCounty = savedInstanceState.getString("county");
            Log.d(TAG, "onCreate: " + mCurrentCounty + ":" + mCurrentCity);
            mSelectProvince = savedInstanceState.getString("selectProvince");
            mSelectCity = savedInstanceState.getString("selectCity");
            mSelectCounty = savedInstanceState.getString("selectCounty");
        }
        mWeatherPresenter = new WeatherPresenterImpl(this);
        getWeatherBasic(mCurrentCounty);
        getWeatherAqi(mCurrentCity);
        judgeAirIsShow();
        mWeatherSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wheel();
            }
        });
    }

    /**
     * 判断空气质量信息是否显示
     */
    private void judgeAirIsShow() {
        if (!(mCurrentCity.equals("香港") || mCurrentCity.equals("澳门") || mCurrentCity.equals("台北") ||
                mCurrentCity.equals("高雄") ||
                mCurrentCity.equals("台中"))) {
            mAirLayout.setVisibility(View.VISIBLE);
        } else {
            mAirLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否可以查询城市空气质量
     *
     * @return true:可以查询 false:不可以查询
     */
    private boolean judgeCityAqiIsQuery() {
        boolean flag = false;
        if (mCurrentCity != null) {
            if (!(mCurrentCity.equals("香港") || mCurrentCity.equals("澳门") || mCurrentCity.equals("台北") ||
                    mCurrentCity.equals("高雄") ||
                    mCurrentCity.equals("台中"))) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * view初始化
     */
    private void initView() {
        mWeatherSelectCity = findViewById(R.id.weather_choose_city);
        mRefreshWeather = findViewById(R.id.weather_swipe_refresh);
        mWeatherNowIcon = findViewById(R.id.weather_now_icon);
        mCond = findViewById(R.id.weather_now_cond_txt_n);
        mTemperature = findViewById(R.id.weather_temp);
        mDate = findViewById(R.id.weather_date);
        mTime = findViewById(R.id.weather_time);
        mTitle = findViewById(R.id.weather_city);
        mTomorrow = findViewById(R.id.weather_forecast_tomorrow_time);
        mTomorrowTem = findViewById(R.id.weather_forecast_tomorrow_tem);
        mTomorrowIcon = findViewById(R.id.weather_forecast_tomorrow_icon);
        mAfter = findViewById(R.id.weather_forecast_after_time);
        mAfterTem = findViewById(R.id.weather_forecast_after_tem);
        mAfterIcon = findViewById(R.id.weather_forecast_after_icon);
        mLifestyleClothes = findViewById(R.id.weather_lifestyle_clothes_text);
        mLifestyleCar = findViewById(R.id.weather_lifestyle_car_text);
        mLifestyleAir = findViewById(R.id.weather_lifestyle_air_text);
        mAirLayout = findViewById(R.id.weather_air);
        mPM25 = findViewById(R.id.weather_air_pm25);
        mNO2 = findViewById(R.id.weather_air_no2);
        mCO = findViewById(R.id.weather_air_co);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle(mResources.getString(R.string.weather_loading_dialog));
    }

    /**
     * 刷新天气
     */
    private void refresh() {
        mRefreshWeather.setColorSchemeResources(R.color.colorPrimary);
        mRefreshWeather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetStatusUtils.isMobileConnected(getApplicationContext()) || NetStatusUtils.isWifiConnected(getApplicationContext())) {
                    loading();
                    mWeatherPresenter.getWeather(mCurrentCounty);
                    if (judgeCityAqiIsQuery()) {
                        mWeatherPresenter.getWeatherAqi(mCurrentCity);
                    } else {
                        ToastUtils.showMessage(getApplicationContext(), mResources.getString(R.string.weather_air_not_support));
                    }
                } else {
                    compelete();
                    mRefreshWeather.setRefreshing(false);
                    ToastUtils.showMessage(getApplicationContext(), mResources.getString(R.string.network_not_connected));
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city", mCurrentCity);
        outState.putString("county", mCurrentCounty);
        outState.putString("selectProvince", mSelectProvince);
        outState.putString("selectCity", mSelectCity);
        outState.putString("selectCounty", mSelectCounty);
    }

    /**
     * 显示加载对话框
     */
    public void loading() {
        try {
            loadingDialog.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 隱藏加载对话框
     */
    public void compelete() {
        try {
            loadingDialog.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {
        loading();
    }

    @Override
    public void hideLoading() {
        compelete();
    }

    @Override
    public void showError() {
        compelete();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshWeather.setRefreshing(false);
                ToastUtils.showError(getApplicationContext(), mResources.getString(R.string.get_weather_info_error));
            }
        });
    }

    @Override
    public void setWeatherInfo(final Weather weather) {
        compelete();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (weather != null) {
                    Log.i(TAG, "run: setWeatherInfo = " + weather.basic.cityName);
                    updateWeatherInfo(weather);
                    mRefreshWeather.setRefreshing(false);
                    mTitle.setText(weather.basic.cityName);
                }
            }
        });
    }

    /**
     * 更新天气信息
     *
     * @param weather 天气
     */
    public void updateWeatherInfo(Weather weather) {
        mCurrentCounty = weather.basic.cityName;
        if (TextUtils.isEmpty(weather.basic.pCityName)) {
            mCurrentCity = weather.basic.cityName;
        } else {
            mCurrentCity = weather.basic.pCityName;
        }
        Log.d(TAG, "updateWeatherInfo: " + mCurrentCity + ":" + mCurrentCounty);
        mWeatherNowIcon.setImageResource(HandlerWeatherUtil.getWeatherImageResource(Integer.parseInt(weather.now.code)));
        mCond.setText(HandlerWeatherUtil.getWeatherType(Integer.parseInt(weather.now.code)));
        if (weather.forecastList != null) {
            mTemperature.setText(weather.forecastList.get(0).max + "℃ / " + weather.forecastList.get(0).min + "℃");

            if (mVTomorrowWeek != null) {
                mVTomorrowWeek.setText(HandlerWeatherUtil.parseDateWeek(weather.forecastList.get(1).date));
                mVAfterWeek.setText(HandlerWeatherUtil.parseDateWeek(weather.forecastList.get(2).date));
            }
            String tomorrow = HandlerWeatherUtil.parseDateLand(weather.forecastList.get(1).date);
            mTomorrow.setText(tomorrow);
            mTomorrowTem.setText(weather.forecastList.get(1).max + "℃ / " + weather.forecastList.get(1).min + "℃");
            mTomorrowIcon.setImageResource(HandlerWeatherUtil.getWeatherImageResource(Integer.parseInt(weather.forecastList.get(1).code)));

            String after = HandlerWeatherUtil.parseDateLand(weather.forecastList.get(2).date);
            mAfter.setText(after);
            mAfterTem.setText(weather.forecastList.get(2).max + "℃ / " + weather.forecastList.get(2).min + "℃");
            mAfterIcon.setImageResource(HandlerWeatherUtil.getWeatherImageResource(Integer.parseInt(weather.forecastList.get(2).code)));

        }
        if (weather.lifestyleList != null) {
            mLifestyleClothes.setText(weather.lifestyleList.get(1).brf);
            mLifestyleCar.setText(weather.lifestyleList.get(6).brf);
            mLifestyleAir.setText(weather.lifestyleList.get(7).brf);
        }
        mDate.setText(HandlerWeatherUtil.parseDate(new Date()));
        mTime.setText(HandlerWeatherUtil.parseTime(new Date()));
        mTitle.setText(weather.basic.cityName);
    }

    @Override
    public void setWeatherAqi(final Weather weather) {
        compelete();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (weather != null) {
                    updateWeatherAqi(weather);
                    mRefreshWeather.setRefreshing(false);
                }
            }
        });
    }

    /**
     * 更新空气质量信息
     *
     * @param weather 天气
     */
    public void updateWeatherAqi(Weather weather) {
        mPM25.setText(weather.air.pm25);
        mNO2.setText(weather.air.no2);
        mCO.setText(weather.air.co);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        initTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask = null;
        }
        if (loadingDialog != null) {
            loadingDialog = null;
        }
        if (mWeatherPresenter != null) {
            mWeatherPresenter = null;
        }
        if (mWeatherBasicRepository != null) {
            mWeatherBasicRepository = null;
        }
        if (mWeatherAqiRepository != null) {
            mWeatherAqiRepository = null;
        }
    }

    /**
     * 加载本地天气数据,未找到则加载网络天气信息
     *
     * @param cityName 城市名
     */
    private void getWeatherBasic(final String cityName) {
        mWeatherBasicRepository = WeatherBasicInjection.getNoteRepository(this);
        mWeatherBasicRepository.getWeatherBasic(cityName + "%", new WeatherBasicDataSource.LoadWeatherBasicsCallback() {
            @Override
            public void onWeatherBasicsLoaded(WeatherBasic weatherBasic) {
                Log.d(TAG, "onWeatherBasicsLoaded: 加载数据库天气数据");
                String weatherBasicInfo = weatherBasic.weatherBasic;
                Weather voice_weather = Utility.handleWeatherResponse(weatherBasicInfo);
                setWeatherInfo(voice_weather);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "onDataNotAvailable: 加载网络天气数据");
                if (NetStatusUtils.isWifiConnected(getApplicationContext()) || NetStatusUtils.isMobileConnected(getApplicationContext())) {
                    mWeatherPresenter.getWeather(cityName);
                } else {
                    compelete();
                    ToastUtils.showMessage(getApplicationContext(), mResources.getString(R.string.network_not_connected));
                }
            }
        });
    }

    /**
     * 加载本地空气质量数据,未找到则加载网络空气质量信息
     *
     * @param cityName 城市名
     */
    private void getWeatherAqi(final String cityName) {
        mWeatherAqiRepository = WeatherAqiInjection.getInstance(this);
        mWeatherAqiRepository.getWeatherAqi(cityName + "%", new WeatherAqiDataSource.LoadWeatherAqisCallback() {
            @Override
            public void onWeatherAqisLoaded(WeatherAqi weatherAqi) {
                Log.d(TAG, "onWeatherAqisLoaded: 加载数据库空气质量信息");
                String weatherAqiInfo = weatherAqi.weatherAqi;
                Weather voice_aqi = Utility.handleWeatherResponse(weatherAqiInfo);
                setWeatherAqi(voice_aqi);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "onDataNotAvailable: 加载网络空气质量信息");
                if (NetStatusUtils.isWifiConnected(getApplicationContext()) || NetStatusUtils.isMobileConnected(getApplicationContext())) {
                    if (judgeCityAqiIsQuery()) {
                        mWeatherPresenter.getWeatherAqi(cityName);
                    } else {
                        ToastUtils.showMessage(getApplicationContext(), mResources.getString(R.string.weather_air_not_support));
                    }
                } else {
                    compelete();
                    ToastUtils.showMessage(getApplicationContext(), mResources.getString(R.string.network_not_connected));
                }
            }
        });
    }

    /**
     * 地址选择器
     */
    private void wheel() {
        Log.d(TAG, "wheel: 打开城市对话框");
        CityConfig cityConfig = new CityConfig.Builder(this)
                .title(mResources.getString(R.string.weather_add_city))
                .confirmText(mResources.getString(R.string.weather_add_city_sure))
                .cancelText(mResources.getString(R.string.weather_add_city_cancel))
                .province(mSelectProvince)
                .city(mSelectCity)
                .district(mSelectCounty)
                .build();

        CityPickerView.getInstance().setConfig(cityConfig);
        CityPickerView.getInstance().setOnCityItemClickListener(new OnCityItemClickListener() {

            /**
             * 选择城市回调
             * @param province 选择省对应bean
             * @param city 选择市对应bean
             * @param district 选择县（区）对应bean
             */
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                if (province != null && city != null && district != null) {
                    mSelectProvince = province.getName();
                    mSelectCity = city.getName();
                    mSelectCounty = district.getName();
                    Log.d(TAG, "onSelected: 选择省:" + province.getName() + ",选择市:" + city.getName() + ",选择区:" + district.getName());
                    if (NetStatusUtils.isWifiConnected(getApplicationContext()) || NetStatusUtils.isMobileConnected(getApplicationContext())) {
                        Log.d(TAG, "onSelected: 市：" + city.getName() + ",县：" + district.getName());
                        getWeatherBasic(district.getName());
                        if (!(province.getName().equals("香港") || province.getName().equals("澳门") ||
                                province.getName().equals("台湾省"))) {
                            mAirLayout.setVisibility(View.VISIBLE);
                            getWeatherAqi(city.getName());
                        } else {
                            mAirLayout.setVisibility(View.GONE);
                            ToastUtils.showMessage(getApplicationContext(), mResources.getString(R.string.weather_air_not_support));
                        }
                    } else {
                        compelete();
                        ToastUtils.showError(getApplicationContext(), getResources().getString(R.string.network_not_connected));
                    }
                }
            }

            /**
             * 取消选择城市
             */
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), mResources.getString(R.string.weather_cancel_select_city), Toast.LENGTH_SHORT).show();
            }
        });
        CityPickerView.getInstance().showCityPicker(WeatherActivity.this);
    }
}
