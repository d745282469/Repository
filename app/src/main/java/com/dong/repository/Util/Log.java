package com.dong.repository.Util;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 日志工具
 * 2019年2月20日14:02:30
 */
public class Log {
    private static final String TAG = "日志";
    private static boolean isDebug = true;//日志开关
    private static boolean isShowLine = true;//是否显示调用行号
    private static String localLogPath;
    private static String fileName;

    public static void isDebug(Boolean isDebug) {
        Log.isDebug = isDebug;
        android.util.Log.v(TAG, "当前日志开关：" + (isDebug ? "开" : "关"));
    }

    public static void v(String tag, String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void v(String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.v(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static void d(String tag, String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.d(tag, msg);
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

    public static void d(String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.d(TAG, msg);
        }
    }

    public static void i(String tag, String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.i(tag, msg);
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

    public static void i(String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.i(TAG, msg);
        }
    }

    public static void w(String tag, String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.w(tag, msg);
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

    public static void w(String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.w(TAG, msg);
        }
    }

    public static void e(String tag, String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.e(tag, msg);
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

    public static void e(String msg, boolean isShowLine) {
        if (isDebug) {
            if (isShowLine) msg = msg + getLine();
            android.util.Log.e(TAG, msg);
        }
    }

    /**
     * 保存本地的日志打印语句
     */
    public static void s(String tag, String msg) {
        Log.d(tag, msg + getLine());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        saveLog(format.format(System.currentTimeMillis())
                + "/"
                + tag
                + ":"
                + msg + getLine());
    }

    /**
     * 保存日志文件
     */
    private static void saveLog(String msg) {
        if (TextUtils.isEmpty(localLogPath)) {
            localLogPath = Environment.getExternalStorageDirectory().getPath() + "/DLog";
        }
        if (TextUtils.isEmpty(fileName)) {
            fileName = "Log.log";
        }
        File file = new File(localLogPath, fileName);
        StringBuilder builder = new StringBuilder();

        try {
            if (!file.getParentFile().exists()) {
                //父级路径不存在，创建路径
                if (!file.getParentFile().mkdirs()) {
                    //创建路径失败
                    throw new IOException("Create path \"" + file.getParentFile().getPath() + "\" fail");
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.write("\r\n" + msg);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setFileName(String fileName) {
        Log.fileName = fileName;
    }

    public static void setLocalLogPath(String localLogPath) {
        Log.localLogPath = localLogPath;
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
        } else {
            return "";
        }
    }
}
