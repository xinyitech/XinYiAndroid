package com.xinyi.xycameraview.util;

import android.util.Log;


public class SNLog {
    /**
     * 是否开启debug模式,开启则打印log
     */
    public static final boolean DEBUG = true;
    public static final String TAG = "SNLog";

    public static int v(String tag, String msg) {
        if (DEBUG) {
            return Log.v(tag, msg);
        }
        return -1;
    }

    public static int v(String msg) {
        return v(TAG, msg);
    }

    public static int d(String tag, String msg) {
        if (DEBUG) {
            return Log.d(tag, msg);
        }
        return -1;
    }

    public static int d(String msg) {
        return d(TAG, msg);
    }

    public static int i(String tag, String msg) {
        if (DEBUG) {
            return Log.i(tag, msg);
        }
        return -1;
    }

    public static int i(String msg) {
        return i(TAG, msg);
    }

    public static int w(String tag, String msg) {
        if (DEBUG) {
            return Log.w(tag, msg);
        }
        return -1;
    }

    public static int w(String msg) {
        return w(TAG, msg);
    }

    public static int e(String tag, String msg) {
        if (DEBUG) {
            return Log.e(tag, msg);
        }
        return -1;
    }

    public static int e(String msg) {
        return e(TAG, msg);
    }
}
