package com.dong.repository.Util;

/**
 * 日志工具
 * 2019年2月20日14:02:30
 */
public class Log {
    private static final String TAG = "日志";
    private static boolean isDebug = true;//日志开关
    private static boolean isShowLine = true;//是否显示调用行号

    public static void isDebug(Boolean isDebug) {
        Log.isDebug = isDebug;
        android.util.Log.v(TAG, "当前日志开关：" + (isDebug ? "开" : "关"));
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            android.util.Log.v(tag, msg + getLine());
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            android.util.Log.v(TAG, msg + getLine());
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            android.util.Log.d(tag, msg + getLine());
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            android.util.Log.d(TAG, msg + getLine());
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            android.util.Log.i(tag, msg + getLine());
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            android.util.Log.i(TAG, msg + getLine());
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            android.util.Log.w(tag, msg + getLine());
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            android.util.Log.w(TAG, msg + getLine());
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            android.util.Log.e(tag, msg + getLine());
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            android.util.Log.e(TAG, msg + getLine());
        }
    }

    /**
     * 获取调用日志的行号
     *
     * @return 调用位置
     */
    private static String getLine() {
        if (isShowLine) {
            return "(" + new Throwable().getStackTrace()[2].getFileName()
                    + ":" + new Throwable().getStackTrace()[2].getLineNumber() + ")";
        }else {
            return "";
        }
    }
}
