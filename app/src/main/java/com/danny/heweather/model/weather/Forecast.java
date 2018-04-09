package com.danny.heweather.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * 天气预报：日期，天气状况，最高最低温度
 * Created by danny on 17-11-14.
 */

public class Forecast {
    public String date;

    @SerializedName("cond_code_d")
    public String code;

    @SerializedName("tmp_max")
    public String max;

    @SerializedName("tmp_min")
    public String min;
}
