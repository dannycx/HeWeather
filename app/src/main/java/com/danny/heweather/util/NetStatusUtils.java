package com.danny.heweather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by danny on 18-3-6.
 */

public class NetStatusUtils {
    //判断当前手机是否连上Wifi.
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                if (mWiFiNetworkInfo.isAvailable()) {
                    return mWiFiNetworkInfo.isConnected();
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    //判断当前手机的网络是否可用.
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                if (mMobileNetworkInfo.isAvailable()) {
                    return mMobileNetworkInfo.isConnected();
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
