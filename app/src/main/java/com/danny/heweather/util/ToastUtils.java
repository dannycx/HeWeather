package com.danny.heweather.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by danny on 17-12-26.
 */

public class ToastUtils {
    public static Toast mToasts = null;

    public static void showMessage(Context context, String msg) {
        mToasts = mToasts != null ? mToasts : Toast.makeText(context, msg, Toast.LENGTH_LONG);
        mToasts.show();
    }

    public static void showError(Context context, String errorMsg) {
        mToasts = mToasts != null ? mToasts : Toast.makeText(context, errorMsg, Toast.LENGTH_LONG);
        mToasts.show();
    }

    public void onDestroy() {
        mToasts = null;
    }
}
