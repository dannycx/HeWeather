package com.danny.heweather.model.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 天气对象
 * Created by danny on 17-11-14.
 */
public class Weather {
    //返回天气状态
    public String status;
    //基本信息对象
    public Basic basic;
    //今天天气对象
    public Now now;
    //更新信息对象
    public Update update;
    //天气预报集合
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
    //生活指数集合
    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;
    //空气质量对象
    @SerializedName("air_now_city")
    public Air air;

    @Override
    public String toString() {
        return "Weather{" +
                "status='" + status + '\'' +
                ", basic=" + basic +
                ", now=" + now +
                ", update=" + update +
                ", forecastList=" + forecastList +
                ", lifestyleList=" + lifestyleList +
                ", air=" + air +
                '}';
    }
}
