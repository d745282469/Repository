package com.dong.repository.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/5/5 08:54
 * 一个图片加载类，统一图片加载的入口，这样子在项目中
 * 如果突然需要更换图片框架，不用一个个去找。
 * 默认使用Glide作为加载框架
 */
public class ImgLoader {

    /**
     * 给ImageView设置图片
     *
     * @param imageView 目标Iv
     * @param drawable  Drawable图片
     * @param context   上下文环境
     */
    public static void loadForIv(ImageView imageView, Drawable drawable, Context context) {
        Glide.with(context).load(drawable).into(imageView);
    }

    /**
     * 给ImageView设置图片
     *
     * @param imageView 目标Iv
     * @param bitmap    Bitmap图片
     * @param context   上下文环境
     */
    public static void loadForIv(ImageView imageView, Bitmap bitmap, Context context) {
        Glide.with(context).load(bitmap).into(imageView);
    }

    /**
     * 给ImageView设置图片
     *
     * @param imageView 目标Iv
     * @param url       图片链接
     * @param context   上下文环境
     */
    public static void loadForIv(ImageView imageView, String url, Context context) {
        Glide.with(context).load(Uri.parse(url)).into(imageView);
    }

    /**
     * 给ImageView设置图片
     *
     * @param imageView 目标Iv
     * @param file      图片文件
     * @param context   上下文环境
     */
    public static void loadForIv(ImageView imageView, File file, Context context) {
        Glide.with(context).load(file).into(imageView);
    }

    /**
     * 异步获取网络图片
     * 回调是在子线程中
     *
     * @param context  上下文环境
     * @param url      图片链接
     * @param callback 回调
     */
    public static void getBitmap(final Context context, final String url, final GetBitmapCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(context).asBitmap().load(Uri.parse(url)).submit().get();
                    callback.success(bitmap);
                } catch (ExecutionException e) {
                    callback.fail();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    callback.fail();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface GetBitmapCallback {
        void success(Bitmap bitmap);

        void fail();
    }
}
