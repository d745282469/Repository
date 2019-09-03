package com.dong.repository.Base;

import android.app.Application;
import android.content.pm.PackageManager;

import com.dong.repository.Util.CrashHandler;
import com.dong.repository.Util.Log;

/**
 * @author pd
 * time     2019/4/7 09:42
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.isDebug(true);//日志开关
        try {
            CrashHandler.getInstance().init(getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
