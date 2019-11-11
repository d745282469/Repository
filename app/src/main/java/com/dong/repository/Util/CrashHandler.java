package com.dong.repository.Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/9/3 09:46
 * 崩溃异常捕捉
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static CrashHandler instance;
    private Thread.UncaughtExceptionHandler defaultHandler;
    private static PackageInfo pkInfo;
    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    public void init(PackageInfo packageInfo) {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();//记录默认的处理对象
        Thread.setDefaultUncaughtExceptionHandler(this);//替换处理对象
        pkInfo = packageInfo;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String fileName = format.format(System.currentTimeMillis()) + ".log";
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + pkInfo.packageName;
            File file = new File(filePath, fileName);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    //创建文件夹失败
                    Log.e(TAG, "Create file \"" + file.getParentFile().getName() + "\" fail");
                    return;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            StringBuilder builder = new StringBuilder("App VersionName:" + pkInfo.versionName + "\r\n" +
                    "App VersionCode:" + pkInfo.versionCode + "\r\n" +
                    "Android Version:" + Build.VERSION.RELEASE + "\r\n" +
                    "Android SDK:" + Build.VERSION.SDK_INT + "\r\n" +
                    "Vendor:" + Build.MANUFACTURER + "\r\n" +
                    "Mobile:" + Build.MODEL + "\r\n" +
                    "CPU:" + Arrays.toString(Build.SUPPORTED_ABIS) + "\r\n\r\n"+
                    e.toString()+"\r\n");
            for (StackTraceElement element : e.getStackTrace()){
                builder.append(element.toString()).append("\r\n");
            }
            writer.write(builder.toString());
            writer.flush();
            writer.close();

            if (defaultHandler != null) {
                //将后续操作交还给默认处理对象
                defaultHandler.uncaughtException(t,e);
            }else {
                //没有默认处理对象的话就将程序杀死
                Process.killProcess(Process.myPid());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
