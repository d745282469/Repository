package com.dong.repository.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.dong.repository.Util.CommonUtil;
import com.dong.repository.Util.UnitUtil;

import java.lang.reflect.Field;

import io.reactivex.annotations.Nullable;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/7/12 08:32
 */
public class GuideView extends View {
    private static final String TAG = "GuideView";
    private View targetView;
    private Paint paint;
    private Paint textPaint;
    private int bgColor = Color.parseColor("#7F2B2B2B");
    private WindowManager windowManager = null;
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    private Rect targetRect;
    private int[] screenWH;
    private float textMaxUsedHeight, textMaxUsedWidth;
    private int lineMaxChar;

    /*引导字的各种大小、颜色、内容、位置*/
    private float textSize;
    private int textColor;
    private String text;
    private TextLocation textLocation;
    private int textPadding;

    /*空白区域的内边距*/
    private int paddingLeft, paddingRight, paddingTop, paddingBottom;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        getScreenWH();
        textSize = 60;
        textColor = Color.WHITE;
//        text = "引导层文字如果过长的话会变成什么样子呢？再加上数字123和字母abc看看？";
        text = "引导文字";
        textPadding = 10;
        paddingLeft = 10;
        paddingRight = 10;
        paddingTop = 10;
        paddingBottom = 10;

        paint = new Paint();
        targetRect = new Rect();
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "陈晓江哈哈手写体.ttf"));

    }

    /**
     * 计算绘制字体需要使用多少的高度
     */
    private void computeTextUsedHeight() {
        if (text == null) {
            return;
        }
        char[] chars = text.toCharArray();

        switch (textLocation) {
            case LEFT:
                textMaxUsedWidth = targetRect.left;
                break;
            case RIGHT:
                textMaxUsedWidth = screenWH[0] - targetRect.right;
                break;
            case TOP:
            case BOTTOM:
                textMaxUsedWidth = screenWH[0] - (2 * textPadding);
        }

        //计算出一行最多允许可以绘制多少字
        lineMaxChar = textPaint.breakText(chars, 0, chars.length,
                textMaxUsedWidth, null);
        float usedHeight = 0f;
        for (int i = 0; i < chars.length; i++) {
            char item = chars[i];
            if (i == lineMaxChar + 1) {
                //绘制到超过最大允许的字数了，换行
                usedHeight += textSize;
            } else if (item == '\n') {
                //匹配到换行符
                usedHeight += textSize;
            }
        }
        usedHeight += textSize;
        textMaxUsedHeight = usedHeight;
        Log.d(TAG, "textMaxUsedWidth=" + textMaxUsedWidth + ",textMaxUsedHeight=" + textMaxUsedHeight +
                ",lineMaxChar=" + lineMaxChar + ",textSize=" + textSize);
    }

    /**
     * 目标View的四周哪个留出的区域最多
     * 就把引导字体放在那边
     */
    private void autoSetTextLocation() {
        int left = targetRect.left;
        int right = screenWH[0] - targetRect.right;
        int top = targetRect.top;
        int bottom = screenWH[1] - targetRect.bottom;

        int max = Math.max(left, right);
        max = Math.max(max, top);
        max = Math.max(max, bottom);

        if (max == left) {
            textLocation = TextLocation.LEFT;
        } else if (max == right) {
            textLocation = TextLocation.RIGHT;
        } else if (max == top) {
            textLocation = TextLocation.TOP;
        } else {
            textLocation = TextLocation.BOTTOM;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (targetView != null) {
            //绘制缺口
            targetView.getGlobalVisibleRect(targetRect);//获得可以包住View的矩形
            //设置一个内边距
            targetRect.left -= paddingLeft;
            targetRect.right += paddingRight;
            targetRect.top -= paddingTop;
            targetRect.bottom += paddingBottom;
            targetRect.offset(0, -getSystemStatusBarHeight());//减去状态栏高度
            canvas.drawRect(targetRect, paint);

            if (textLocation == null) {
                autoSetTextLocation();
            }

            //绘制背景
            paint.setStyle(Paint.Style.FILL);
            paint.setXfermode(xfermode);//设置两个图层相交的地方透明
            paint.setColor(bgColor);
            canvas.drawRect(0, 0, screenWH[0], screenWH[1], paint);

            computeTextUsedHeight();
            switch (textLocation) {
                case RIGHT:
                    drawRight(canvas);
                    break;
                case LEFT:
                    drawLeft(canvas);
                    break;
                case BOTTOM:
                    drawBottom(canvas);
                    break;
                case TOP:
                    drawTop(canvas);
                    break;
            }
        }
    }

    /**
     * 引导字体在左边
     */
    private void drawLeft(Canvas canvas) {
        char[] chars = text.toCharArray();
        float currentHeight = (targetRect.bottom - targetRect.top) / 2f - (textMaxUsedHeight / 2) + targetRect.top;
        float currentWidth = textPadding;
        if (chars.length <= lineMaxChar) {
            //如果只够一行的话就不用那么复杂了
            currentWidth = targetRect.left / 2f - ((textSize * chars.length) / 2);
            canvas.drawText(text, currentWidth, currentHeight + textSize, textPaint);
        } else {
            for (int i = 0; i < chars.length; i++) {
                char item = chars[i];
                Log.d(TAG, "准备绘制：" + String.valueOf(item) + "，index=" + i);
                if (i % lineMaxChar == 0 && i != 0) {
                    //该次绘制的字体会超过最大宽度，换行
                    Log.d(TAG, "超过最大允许字数，换行");
                    currentHeight += textSize;

                    //判断剩余的字数够不够一行
                    if (chars.length - i < lineMaxChar) {
                        //不够一行，那么这一行需要水平居中一下
                        currentWidth = (textMaxUsedWidth - (textSize * (chars.length - i))) / 2;
                    } else {
                        currentWidth = textPadding;
                    }
                } else if (item == '\n') {
                    //换行符
                    currentHeight += textSize;
                    //判断剩余的字数够不够一行
                    if (chars.length - i < lineMaxChar) {
                        //不够一行，那么这一行需要水平居中一下
                        currentWidth = (textMaxUsedWidth - (textSize * (chars.length - i))) / 2;
                    } else {
                        currentWidth = textPadding;
                    }
                }
                canvas.drawText(String.valueOf(item), currentWidth, currentHeight + textSize, textPaint);
                currentWidth += textSize;
            }
        }
    }

    /**
     * 引导字体在右边
     */
    private void drawRight(Canvas canvas) {
        char[] chars = text.toCharArray();
        float currentHeight = (targetRect.bottom - targetRect.top) / 2f - (textMaxUsedHeight / 2) + targetRect.top;
        float currentWidth = targetRect.right + textPadding;
        if (chars.length <= lineMaxChar) {
            //只有一行的话，直接水平居中就好啦
            currentWidth = targetRect.right;
            currentWidth += (screenWH[0] - targetRect.right) / 2f - ((textSize * chars.length) / 2);
            canvas.drawText(text, currentWidth, currentHeight + textSize, textPaint);
        } else {
            //多行文字，所以逐字绘制
            for (int i = 0; i < chars.length; i++) {
                char item = chars[i];
                Log.d(TAG, "准备绘制：" + String.valueOf(item) + "，index=" + i);
                if (i % lineMaxChar == 0 && i != 0) {
                    //该次绘制的字体会超过最大宽度，换行
                    Log.d(TAG, "超过最大允许字数，换行");
                    currentHeight += textSize;

                    //判断剩余的字数够不够一行
                    if (chars.length - i < lineMaxChar) {
                        //不够一行，那么这一行需要水平居中一下
                        currentWidth = targetRect.right;
                        currentWidth += (textMaxUsedWidth - (textSize * (chars.length - i))) / 2;
                    } else {
                        currentWidth = targetRect.right + textPadding;
                    }
                } else if (item == '\n') {
                    //换行符
                    currentHeight += textSize;
                    //判断剩余的字数够不够一行
                    if (chars.length - i < lineMaxChar) {
                        //不够一行，那么这一行需要水平居中一下
                        currentWidth = targetRect.right;
                        currentWidth += (textMaxUsedWidth - (textSize * (chars.length - i))) / 2;
                    } else {
                        currentWidth = targetRect.right + textPadding;
                    }
                }
                canvas.drawText(String.valueOf(item), currentWidth, currentHeight + textSize, textPaint);
                currentWidth += textSize;
            }
        }
    }

    /**
     * 引导文字在底部
     */
    private void drawBottom(Canvas canvas) {
        char[] chars = text.toCharArray();
        float currentHeight = targetRect.bottom;
        currentHeight += (screenWH[1] - targetRect.bottom) / 2f - (textMaxUsedHeight / 2);
        float currentWidth = textPadding;
        if (chars.length <= lineMaxChar) {
            //只有一行的话，直接水平居中，紧挨引导View就好啦
            currentWidth += screenWH[0] / 2f - ((textSize * chars.length) / 2);
            currentHeight = targetRect.bottom + UnitUtil.dip2px(getContext(), 5);
            canvas.drawText(text, currentWidth, currentHeight + textSize, textPaint);
        } else {
            //多行文字，所以逐字绘制
            for (int i = 0; i < chars.length; i++) {
                char item = chars[i];
                Log.d(TAG, "准备绘制：" + String.valueOf(item) + "，index=" + i);
                if (i % lineMaxChar == 0 && i != 0) {
                    //该次绘制的字体会超过最大宽度，换行
                    Log.d(TAG, "超过最大允许字数，换行");
                    currentHeight += textSize;

                    //判断剩余的字数够不够一行
                    if (chars.length - i < lineMaxChar) {
                        //不够一行，那么这一行需要水平居中一下
                        currentWidth = textPadding;
                        currentWidth += (textMaxUsedWidth - (textSize * (chars.length - i))) / 2;
                    } else {
                        currentWidth = textPadding;
                    }
                } else if (item == '\n') {
                    //换行符
                    currentHeight += textSize;
                    //判断剩余的字数够不够一行
                    if (chars.length - i < lineMaxChar) {
                        //不够一行，那么这一行需要水平居中一下
                        currentWidth = textPadding;
                        currentWidth += (textMaxUsedWidth - (textSize * (chars.length - i))) / 2;
                    } else {
                        currentWidth = textPadding;
                    }
                }
                canvas.drawText(String.valueOf(item), currentWidth, currentHeight + textSize, textPaint);
                currentWidth += textSize;
            }
        }
    }

    /**
     * 引导文字在顶部
     */
    private void drawTop(Canvas canvas) {
        char[] chars = text.toCharArray();
        float currentHeight = targetRect.top / 2f - (textMaxUsedHeight / 2);
        float currentWidth = textPadding;
        if (chars.length <= lineMaxChar) {
            //只有一行的话，直接水平居中，紧挨引导View就好啦
            currentWidth += screenWH[0] / 2f - ((textSize * chars.length) / 2);
            currentHeight = targetRect.top - UnitUtil.dip2px(getContext(), 5);
            canvas.drawText(text, currentWidth, currentHeight - textSize, textPaint);
        } else {
            //多行文字，所以逐字绘制
            for (int i = 0; i < chars.length; i++) {
                char item = chars[i];
                Log.d(TAG, "准备绘制：" + String.valueOf(item) + "，index=" + i);
                if (i % lineMaxChar == 0 && i != 0) {
                    //该次绘制的字体会超过最大宽度，换行
                    Log.d(TAG, "超过最大允许字数，换行");
                    currentHeight += textSize;

                    //判断剩余的字数够不够一行
                    if (chars.length - i < lineMaxChar) {
                        //不够一行，那么这一行需要水平居中一下
                        currentWidth = textPadding;
                        currentWidth += (textMaxUsedWidth - (textSize * (chars.length - i))) / 2;
                    } else {
                        currentWidth = textPadding;
                    }
                } else if (item == '\n') {
                    //换行符
                    currentHeight += textSize;
                    //判断剩余的字数够不够一行
                    if (chars.length - i < lineMaxChar) {
                        //不够一行，那么这一行需要水平居中一下
                        currentWidth = textPadding;
                        currentWidth += (textMaxUsedWidth - (textSize * (chars.length - i))) / 2;
                    } else {
                        currentWidth = textPadding;
                    }
                }
                canvas.drawText(String.valueOf(item), currentWidth, currentHeight + textSize, textPaint);
                currentWidth += textSize;
            }
        }
    }

    /**
     * 引导文字的位置
     */
    public enum TextLocation {
        TOP, //在目标View的上方垂直水平居中
        BOTTOM, //在目标View的下方垂直水平居中
        LEFT, //在目标View的左方垂直水平居中
        RIGHT //在目标View的右方垂直水平居中
    }

    private int[] getScreenWH() {
        if (screenWH == null) {
            screenWH = CommonUtil.getRealScreenWH(getContext());
        }
        return screenWH;
    }

    /**
     * 反射获取android系统状态栏的高度
     *
     * @return 单位: 像素px
     */
    private int getSystemStatusBarHeight() {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public void setTextLocation(TextLocation textLocation) {
        this.textLocation = textLocation;
        invalidate();
    }
}
