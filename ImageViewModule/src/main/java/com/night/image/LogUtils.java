package com.night.image;

import android.util.Log;

import androidx.annotation.Nullable;

class LogUtils {
    private static final String TAG = "SmallImageView";

    public static void i(@Nullable String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void e(@Nullable String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void w(@Nullable String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, msg);
        }
    }

}
