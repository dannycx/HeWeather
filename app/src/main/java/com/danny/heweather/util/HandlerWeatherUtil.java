package com.danny.heweather.util;


import com.danny.heweather.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 天气信息处理工具类
 * Created by danny on 12/15/17.
 */

public class HandlerWeatherUtil {

    /**
     * 天气代码 100，900 为晴 101-213，503-508，901 为阴 300-313为雨  400-407 为雪  500-502 为雾霾
     *
     * @param code 天气代码
     * @return 天气类型
     */
    public static String getWeatherType(int code) {
        if (code == 100 || code == 900) {
            return "晴";
        }
        if ((code >= 101 && code <= 213) || (code >= 500 && code <= 508) || (code == 901)) {
            return "阴";
        }
        if (code >= 300 && code <= 313) {
            return "雨";
        }
        if (code >= 400 && code <= 407) {
            return "雪";
        }
        if (code >= 500 && code <= 502) {
            return "雾";
        }
        return "未知";
    }

    /**
     * 天气代码 100，900 为晴 101-213，503-508，901 为阴 300-313为雨  400-407 为雪  500-502 为雾霾
     *
     * @param code 天气代码
     * @return 天气图标
     */
    public static int getWeatherImageResource(int code) {
        if (code == 100 || code == 900) {
            return R.drawable.weather_sunny;
        }
        if ((code >= 101 && code <= 213) || (code >= 500 && code <= 508) || (code == 901)) {
            return R.drawable.weather_cloudy;
        }
        if (code >= 300 && code <= 313) {
            return R.drawable.weather_thunderstorm;
        }
        if (code >= 400 && code <= 407) {
            return R.drawable.weather_snow;
        }
        if (code >= 500 && code <= 502) {
            return R.drawable.weather_smog;
        }
        return R.drawable.weather_unknown;
    }

    /**
     * 解析日期，返回指定格式
     *
     * @param date 日期××××-××-××
     * @return pm 01:30
     */
    public static String parseTime(Date date) {
        String result = new SimpleDateFormat("aa hh:mm").format(date);
        return result;
    }

    /**
     * 解析日期，返回指定格式
     *
     * @param date 日期××××-××-××
     * @return 周一 01 22
     */
    public static String parseDate(Date date) {
        String result = new SimpleDateFormat("EE MM dd").format(date);
        return result;
    }

    /**
     * 解析日期竖屏，返回指定格式
     *
     * @param date 日期××××-××-××
     * @return 周一/Mon
     */
    public static String parseDateWeek(String date) {
        String result = "";
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("EE");
        try {
            Date date1 = format1.parse(date);
            result = format2.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析日期横屏，返回指定格式
     *
     * @param date 日期××××-××-××
     * @return ××(月) ××(日)
     */
    public static String parseDateLand(String date) {
        String result = "";
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("MM dd");
        try {
            Date date1 = format1.parse(date);
            result = format2.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
