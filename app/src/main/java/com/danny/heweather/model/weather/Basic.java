package com.danny.heweather.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * 天气基本信息：城市名
 * Created by danny on 17-11-14.
 */

public class Basic {
    @SerializedName("location")
    public String cityName;
    @SerializedName("parent_city")
    public String pCityName;
    @SerializedName("cid")
    public String weatherId;
}
