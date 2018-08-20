package com.xht.androidnote.utils;

import android.util.Log;

/**
 * Created by xht on 2018/4/23.
 */

public class L {

    private static final boolean debug = true;
    private static final String TAG = "xht";


    public static void v(String tag, String msg) {
        if (debug)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (debug)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (debug)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (debug)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (debug)
            Log.e(tag, msg);
    }

    public static void v(String msg) {
        if (debug)
            Log.v(TAG, msg);
    }

    public static void d(String msg) {
        if (debug)
            Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (debug)
            Log.i(TAG, msg);
    }

    public static void w(String msg) {
        if (debug)
            Log.w(TAG, msg);
    }

    public static void e(String msg) {
        if (debug)
            Log.e(TAG, msg);
    }

}
