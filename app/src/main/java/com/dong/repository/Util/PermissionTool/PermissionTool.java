
package com.dong.repository.Util.PermissionTool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.dong.repository.Util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 运行时权限工具
 * dong 2019年1月5日15:27:39
 */
public class PermissionTool {
    private static final String TAG = "PermissionTool";
    private static Context context;
    private static List<String> permission_list;
    private static GetPermissionResultListener GetPermissionResultListener;
    private static Fragment supportFragment;
    private static android.app.Fragment fragment;

    private static final int REQUEST_CODE = 999;//标识码

    /**
     * 给外部调用的接口，会启动一个Fragment去获取权限
     * 注意要申请的权限需要写在AndroidManifest.xml中，否则不能调起权限窗口
     *
     * @param activity        活动
     * @param permission_list 权限集合
     * @param listener        监听器
     */
    public static void getPermission(AppCompatActivity activity, List<String> permission_list, GetPermissionResultListener listener) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        supportFragment = new PermissionSupportFragment();
        transaction.add(supportFragment, null);
        transaction.commitNow();

        context = supportFragment.getContext();
        PermissionTool.permission_list = permission_list;
        setGetPermissionResultListener(listener);
    }

    /**
     * 获取单个权限
     */
    public static void getPermission(AppCompatActivity activity, String permission, GetPermissionResultListener listener) {
        ArrayList<String> list = new ArrayList<>();
        list.add(permission);
        getPermission(activity, list, listener);
    }

    /**
     * Fragment用的申请方法
     *
     * @param fragment 调用该方法的Fragment
     * @param list     要申请的权限
     * @param listener 监听器
     */
    public static void getPermission(Fragment fragment, List<String> list, GetPermissionResultListener listener) {
        FragmentManager manager = fragment.getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        supportFragment = new PermissionSupportFragment();
        transaction.add(supportFragment, null);
        transaction.commitNow();

        context = supportFragment.getContext();
        PermissionTool.permission_list = list;
        setGetPermissionResultListener(listener);
    }

    /**
     * Fragment申请单个权限的方法
     */
    public static void getPermission(Fragment fragment, String permission, GetPermissionResultListener listener) {
        List<String> list = new ArrayList<>();
        list.add(permission);
        getPermission(fragment, list, listener);
    }

    /**
     *兼容非Support包的情况
     */
    public static void getPermission(Activity activity, List<String> list, GetPermissionResultListener listener) {
        android.app.FragmentManager manager = activity.getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        fragment = new PermissionFragment();
        transaction.add(fragment, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            transaction.commitNow();

            context = fragment.getContext();
            permission_list = list;
            setGetPermissionResultListener(listener);
        } else {
            //sdki版本不对的话就直接走拒绝授权
            listener.getPermissionFail(list);
            Log.e(TAG, "SdkVersion needs 24,But current version is " + Build.VERSION.SDK_INT + ". Use other methods or change your minSdkVersion.");
            clear();
        }
    }

    public static void getPermission(Activity activity, String permission, GetPermissionResultListener listener) {
        List<String> list = new ArrayList<>();
        list.add(permission);
        getPermission(activity, list, listener);
    }

    public static void getPermission(FragmentActivity activity, List<String> list, GetPermissionResultListener listener){
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        supportFragment = new PermissionSupportFragment();
        transaction.add(supportFragment, null);
        transaction.commitNow();

        context = supportFragment.getContext();
        PermissionTool.permission_list = list;
        setGetPermissionResultListener(listener);
    }

    public static void getPermission(FragmentActivity activity, String permission, GetPermissionResultListener listener){
        List<String> list = new ArrayList<>();
        list.add(permission);
        getPermission(activity,list,listener);
    }


    /**
     * 执行获取权限动作
     */
    private static void startGetPermission() {
        for (int i = 0; i < permission_list.size(); i++) {
            //循环查是否已经获取到权限
            if (ContextCompat.checkSelfPermission(context, permission_list.get(i)) == PackageManager.PERMISSION_GRANTED) {
                //将已经获取到的权限剔除
                permission_list.remove(i);
            }
        }
        context = null;//防止内存泄露

        if (permission_list.size() <= 0) {
            //所有权限都已经获取到了
            GetPermissionResultListener.getPermissionSuccess();
            clear();
        } else {
            //将list转成数组,此时list中的所有权限都是未获取的
            String[] permission_array = new String[permission_list.size()];
            for (int i = 0; i < permission_list.size(); i++) {
                permission_array[i] = permission_list.get(i);
            }
            //开始申请权限
            if (supportFragment != null) {
                supportFragment.requestPermissions(permission_array, REQUEST_CODE);
            } else if (fragment != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fragment.requestPermissions(permission_array, REQUEST_CODE);
                } else {
                    //版本号不对
                    GetPermissionResultListener.getPermissionFail(permission_list);
                    Log.e(TAG, "Version needs 24,But current version is " + Build.VERSION.SDK_INT + ". Use other methods or change your minSdkVersion.");
                    clear();
                }
            }

        }
    }

    /**
     * 暴露给外部的监听接口,失败时将返回被拒绝的权限集合
     */
    public interface GetPermissionResultListener extends Serializable {
        void getPermissionSuccess();

        void getPermissionFail(List<String> permissions);
    }

    /**
     * 设置监听器，只有设置了监听器的才会执行获取权限的动作
     *
     * @param getPermissionResultListenerListener 监听器
     */
    private static void setGetPermissionResultListener(GetPermissionResultListener getPermissionResultListenerListener) {
        GetPermissionResultListener = getPermissionResultListenerListener;
        startGetPermission();
    }

    /**
     * 回调会传到Activity、Fragment中，所以需要在Activity的回调中调用该方法，将后续处理交给该类
     *
     * @param requestCode  在Activity的回调中的参数，原封不动的返回即可
     * @param permissions  在Activity的回调中的参数，原封不动的返回即可
     * @param grantResults 在Activity的回调中的参数，原封不动的返回即可
     */
    static void onResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //解析获取权限结果
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                List<String> permissions_fail = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //被拒绝的权限
                        permissions_fail.add(permissions[i]);
                    }
                }

                //开始通知监听者
                if (permissions_fail.size() > 0 && GetPermissionResultListener != null) {
                    //有一个权限没被允许都将走失败的分支
                    GetPermissionResultListener.getPermissionFail(permissions_fail);
                } else if (GetPermissionResultListener != null) {
                    GetPermissionResultListener.getPermissionSuccess();
                }

                //移除透明fragment
                if (supportFragment != null) {
                    FragmentManager manager = supportFragment.getFragmentManager();
                    if (manager != null) {
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.remove(supportFragment).commit();
                    }
                    supportFragment = null;
                } else if (fragment != null) {
                    android.app.FragmentManager manager = fragment.getFragmentManager();
                    if (manager != null) {
                        android.app.FragmentTransaction transaction = manager.beginTransaction();
                        transaction.remove(fragment).commit();
                    }
                    fragment = null;
                }
                clear();
            }
        }
    }

    private static void clear(){
        context = null;
        supportFragment = null;
        fragment = null;
    }
}
