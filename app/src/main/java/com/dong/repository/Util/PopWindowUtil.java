package com.dong.repository.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dong.repository.R;

import java.util.Random;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/7/11 09:34
 * 通用的PopUpWindow工具类
 * 不用再去写那些重复的代码
 */
public class PopWindowUtil {
    private static final String TAG = "PopWindowUtil";
    private static PopupWindow popupWindow;
    public static final int GRAVITY_LEFT = 0;
    public static final int GRAVITY_RIGHT = 1;
    public static final int GRAVITY_TOP = 2;
    public static final int GRAVITY_BOTTOM = 3;

    public static void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        } else {
            Log.e(TAG, "popupWindow is null!");
        }
    }

    /**
     * 模拟网易云音乐的底部列表
     *
     * @param activity   用来改变背景透明度以及加载控件
     * @param anchorView 锚点View
     */
    public static void bottomList(final Activity activity, View anchorView) {
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_menu, null);
        popupWindow = new Builder()
                .setContentView(view)
                .setAnimationStyle(R.style.PopAnim)
                .setBackgroundDrawable(activity.getDrawable(R.drawable.pop_menu_round))
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .build();
        //手动模拟变暗动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 200; i++) {
                    final float step = 0.5f / 200 * i;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtil.setBackgroundAlpha(1 - step, activity);
                        }
                    });
                    SystemClock.sleep(1);
                }
            }
        }).start();

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 200; i++) {
                            final float step = 0.5f / 200 * i;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommonUtil.setBackgroundAlpha(0.5f + step, activity);
                                }
                            });
                            SystemClock.sleep(1);
                        }
                    }
                }).start();
            }
        });
        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);
    }

    public static void demo(Context context, View anchorView) {
        dismiss();
        TextView textView = new TextView(context);
        textView.setText("Hello");
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);

        popupWindow = new Builder()
                .setContentView(textView)
                .build();
        showBaseOnAnchor(anchorView, new Random().nextInt(5));
    }

    /**
     * 根据锚点View进行偏移
     *
     * @param anchorView 锚点View
     * @param location   要放置的位置
     */
    private static void showBaseOnAnchor(View anchorView, int location) {
        dismiss();
        Log.d(TAG, "location=" + location);
        int anchorHeight = anchorView.getHeight();//锚点View的高度
        int anchorWidth = anchorView.getWidth();//锚点View的宽度
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);//强制计算布局
        //获取popupWindow的宽高
        int windowHeight = popupWindow.getHeight();
        int windowWidth = popupWindow.getWidth();
        if (windowHeight <= 0) {
            windowHeight = popupWindow.getContentView().getMeasuredHeight();
        }
        if (windowWidth <= 0) {
            windowWidth = popupWindow.getContentView().getMeasuredWidth();
        }
        int xOff = 0, yOff = 0;

        //根据位置计算偏移量
        switch (location) {
            case GRAVITY_RIGHT:
                xOff = anchorWidth;
                yOff = -anchorHeight;
                break;
            case GRAVITY_LEFT:
                xOff = -windowWidth;
                yOff = -anchorHeight;
                break;
            case GRAVITY_TOP:
                xOff = 0;
                yOff = -(anchorHeight + windowHeight);
                break;
            case GRAVITY_BOTTOM:
                xOff = 0;
                yOff = 0;
                break;
        }
//        xOff = 0;
//        yOff = 0;
        Log.d(TAG, String.format("xOff=%s,y0ff=%s,viewHeight=%s,viewWidth=%s,windowHeight=%s,windowWidth=%s", xOff, yOff, anchorHeight, anchorWidth, windowHeight, windowWidth));
        popupWindow.showAsDropDown(anchorView, xOff, yOff);
    }

    /**
     * 根据锚点View进行偏移
     *
     * @param anchorView 锚点View
     * @param xOff       x轴方向的偏移量
     * @param yOff       y轴方向的偏移量
     */
    private static void showAsDropDown(View anchorView, int xOff, int yOff) {
        dismiss();
        popupWindow.showAsDropDown(anchorView, xOff, yOff);
    }

    /**
     * PopupWindow的建造者模式
     * 可以很快的创建一个PopupWindow
     */
    public static class Builder {
        private View contentView;
        private Drawable backgroundDrawable;
        private boolean touchable;//是否响应点击事件
        private boolean outSideTouchable;//是否响应外部点击事件
        private boolean focusable;//是否能获取焦点
        private int animationStyle;
        private int width;
        private int height;

        public Builder() {
            this.contentView = null;
            this.backgroundDrawable = new ColorDrawable(Color.TRANSPARENT);//默认透明背景
            this.touchable = true;
            this.outSideTouchable = true;
            this.focusable = true;
            this.animationStyle = -1;
            this.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            this.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        public Builder setContentView(View view) {
            this.contentView = view;
            return this;
        }

        public Builder setBackgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder setTouchable(boolean touchable) {
            this.touchable = touchable;
            return this;
        }

        public Builder setOutSideTouchable(boolean outSideTouchable) {
            this.outSideTouchable = outSideTouchable;
            return this;
        }

        public Builder setFocusable(boolean focusable) {
            this.focusable = focusable;
            return this;
        }

        public Builder setAnimationStyle(int animationStyle) {
            this.animationStyle = animationStyle;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public PopupWindow build() {
            PopupWindow popupWindow = new PopupWindow(contentView, width, height, focusable);
            popupWindow.setBackgroundDrawable(backgroundDrawable);
            if (animationStyle != -1) {
                popupWindow.setAnimationStyle(animationStyle);
            }
            popupWindow.setTouchable(touchable);
            popupWindow.setOutsideTouchable(outSideTouchable);
            return popupWindow;
        }
    }
}
