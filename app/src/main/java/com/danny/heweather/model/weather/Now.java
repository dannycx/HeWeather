package com.danny.heweather.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * 当前天气信息
 * Created by danny on 17-11-14.
 */

public class Now {
    @SerializedName("tmp")
    public String tmperature;
    @SerializedName("cond_code")
    public String code;
}
