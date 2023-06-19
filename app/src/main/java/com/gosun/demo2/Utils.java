package com.gosun.demo2;

import android.util.Log;

public class Utils {

    public static final boolean DEBUG = true;


    public static void logD(String TAG, String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void logI(String TAG, String msg) {

        Log.i(TAG, msg);
    }

    public static void logW(String TAG, String msg) {
        Log.w(TAG, msg);
    }

    public static void logE(String TAG, String msg) {
        Log.e(TAG, msg);
    }
}
