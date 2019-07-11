package com.dong.repository.Util;

/**
 * 日志工具
 * 2019年2月20日14:02:30
 */
public class Log {
    private static final String TAG = "日志";
    private static boolean isDebug = true;//日志开关

    public static void isDebug(Boolean isDebug) {
        Log.isDebug = isDebug;
        android.util.Log.v(TAG, "当前日志开关：" + (isDebug ? "开" : "关"));
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            android.util.Log.d(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            android.util.Log.i(TAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            android.util.Log.w(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            android.util.Log.e(TAG, msg);
        }
    }

}
