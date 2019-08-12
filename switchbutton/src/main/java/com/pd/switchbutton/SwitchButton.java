package com.pd.switchbutton;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/8/12 08:29
 */
public class SwitchButton extends View {
    private static final String TAG = "SwitchButton";
    private int mWidth, mHeight;
    private Paint mPaint;
    private int mUnCheckBarColor;
    private int mUnCheckCircleColor;
    private static final int WX_GREEN = Color.parseColor("#0DC669");//微信绿色
    private int mCheckBarColor;//选中时，矩形的颜色
    private int mCheckCircleColor;//选中时圆的颜色
    private int mCircleRadius;
    private int mStartX, mEndX, mCircleX;
    private static final int mPadding = 8;//默认内边距
    private boolean mCheck;//默认未选择状态
    private float mLastTouchX;
    private int mAlpha;
    private GestureDetector detector;
    private Context context;
    private OnCheckListener onCheckListener;
    private long mDuration;//动画时长，毫秒

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.context = context;

        //获取xml中设置的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        mUnCheckBarColor = array.getColor(R.styleable.SwitchButton_uncheckBarColor,Color.GRAY);
        mUnCheckCircleColor = array.getColor(R.styleable.SwitchButton_uncheckCircleColor,Color.WHITE);
        mCheckBarColor = array.getColor(R.styleable.SwitchButton_checkBarColor,WX_GREEN);
        mCheckCircleColor = array.getColor(R.styleable.SwitchButton_checkCircleColor,mUnCheckCircleColor);
        mDuration = array.getInteger(R.styleable.SwitchButton_duration,300);
        mCircleRadius = array.getInteger(R.styleable.SwitchButton_radius,0);
        mCheck = array.getBoolean(R.styleable.SwitchButton_isCheck,false);
        if (mCheck){
            mAlpha = 0xff;
        }else {
            mAlpha = 0;
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mWidth <= mHeight * 2) {
            mWidth = mHeight * 2;//保证宽度始终大于高度
        }
        if (mCircleRadius >= mHeight / 2 - mPadding || mCircleRadius <= 0) {
            mCircleRadius = mHeight / 2 - mPadding;//保证圆直径始终小于矩形高度
        }

        //确定圆心坐标范围
        mStartX = mCircleRadius + mPadding;//最左边时x坐标
        mEndX = (int) (getX() + mWidth - mCircleRadius - mPadding);//最右x坐标
        if (mCheck) {
            mCircleX = mEndX;
        } else {
            mCircleX = mStartX;
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //未选中状态的圆角矩形
        mPaint.setColor(mUnCheckBarColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(0, 0, mWidth, mHeight, mHeight / 2f, mHeight / 2f, mPaint);

        //选中状态的圆角矩形
        mAlpha = (int) ((mCircleX - mStartX) * (255f / (mEndX - mStartX)));
        mPaint.setColor(mCheckBarColor);
        mPaint.setAlpha(mAlpha);//通过更改透明度达到从未选中→选中的效果
        canvas.drawRoundRect(0, 0, mWidth, mHeight, mHeight / 2f, mHeight / 2f, mPaint);

        //未选中状态的圆
        mPaint.setColor(mUnCheckCircleColor);
        canvas.drawCircle(mCircleX, mHeight / 2f, mCircleRadius, mPaint);

        //选中状态的圆
        mPaint.setColor(mCheckCircleColor);
        if (mCheckCircleColor == 0) {
            mPaint.setColor(mUnCheckCircleColor);//未设置选中颜色时使用未选中的颜色
        }
        mPaint.setAlpha(mAlpha);//通过更改透明度达到从未选中→选中的效果
        canvas.drawCircle(mCircleX, mHeight / 2f, mCircleRadius, mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        Log.d(TAG, "TouchEvent,LastTouchX=" + mLastTouchX + ",CurrentX=" + event.getX());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((mCircleX + event.getX() - mLastTouchX) < mStartX) {
                    //最小x坐标为最左边的x坐标
                    mCircleX = mStartX;
                    mAlpha = 0;
                } else if ((mCircleX + event.getX() - mLastTouchX) > mEndX) {
                    //最大x坐标为最右边的x坐标
                    mCircleX = mEndX;
                    mAlpha = 0xff;
                } else {
                    mCircleX = (int) (mCircleX + event.getX() - mLastTouchX);
                    mAlpha = (int) ((mCircleX - mStartX) * (255f / (mEndX - mStartX)));
                }
                mLastTouchX = event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if (mCircleX - mStartX >= (mEndX - mStartX) / 2){
                    //执行选中
                    if (mCheck){
                        //之前本来就选中的情况下就只执行动画
                        //不通知监听器
                        startCheck();
                    }else {
                        mCheck = true;
                        startCheckAndNotify();
                    }
                }else {
                    //执行取消选中
                    if (mCheck){
                        //之前在选中状态，此次改变需要通知监听器
                        mCheck = false;
                        startUnCheckAndNotify();
                    }else {
                        startUnCheck();
                    }
                }
                break;
        }
//        Log.d(TAG, "TouchEvent,mCircleX=" + mCircleX);
        if (detector == null) {
            detector = new GestureDetector(context, new MyGestureListener());
        }
        detector.onTouchEvent(event);
        return true;
    }

    /**
     * 执行选中效果
     */
    private void startCheck() {
        ObjectAnimator animator = ObjectAnimator
                .ofInt(this, "mCircleX", mCircleX, mEndX);
        animator.setDuration(mDuration);
        animator.start();
    }

    /**
     * 执行动画并通知监听器
     */
    private void startCheckAndNotify() {
        startCheck();
//        Log.d(TAG,"选中了");
        if (onCheckListener != null) {
            onCheckListener.onCheck(mCheck);
        }
    }

    /**
     * 执行取消选中效果
     */
    private void startUnCheck() {
        ObjectAnimator animator = ObjectAnimator
                .ofInt(this, "mCircleX", mCircleX, mStartX);
        animator.setDuration(mDuration);
        animator.start();
    }

    /**
     * 执行动画并通知监听器
     */
    private void startUnCheckAndNotify() {
         startUnCheck();
//        Log.d(TAG,"取消选中了");
        if (onCheckListener != null) {
            onCheckListener.onCheck(mCheck);
        }
    }

    /**
     * 设置圆心x坐标
     *
     * @param mCircleX 坐标
     */
    public void setMCircleX(int mCircleX) {
        this.mCircleX = mCircleX;
        invalidate();
    }

    /**
     * 设置未选中时矩形的颜色
     *
     * @param mBarColor 颜色
     */
    public void setUnCheckBarColor(int mBarColor) {
        this.mUnCheckBarColor = mBarColor;
        invalidate();
    }

    /**
     * 设置是否选中
     *
     * @param mCheck true选中/false不选中
     */
    public void setCheck(boolean mCheck) {
        this.mCheck = mCheck;
        if (mCheck) {
            startCheckAndNotify();
        } else {
            startUnCheckAndNotify();
        }
    }

    /**
     * 设置是否选中，并且不触发监听器
     *
     * @param mCheck true选中/false不选中
     */
    public void setCheckNoEvent(boolean mCheck) {
        this.mCheck = mCheck;
        if (mCheck) {
            startCheck();
        } else {
            startUnCheck();
        }
    }

    /**
     * 设置选中时，矩形的颜色
     *
     * @param mCheckBarColor 颜色
     */
    public void setCheckBarColor(int mCheckBarColor) {
        this.mCheckBarColor = mCheckBarColor;
        invalidate();
    }

    /**
     * 设置选中时，圆的颜色
     *
     * @param mCheckCircleColor 颜色
     */
    public void setCheckCircleColor(int mCheckCircleColor) {
        this.mCheckCircleColor = mCheckCircleColor;
        invalidate();
    }

    /**
     * 设置未选中时，圆的颜色
     *
     * @param mCircleColor 颜色
     */
    public void setUnCheckCircleColor(int mCircleColor) {
        this.mUnCheckCircleColor = mCircleColor;
        invalidate();
    }

    /**
     * 设置圆的半径
     *
     * @param mCircleRadius 半径
     */
    public void setCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
        invalidate();
    }

    /**
     * 设置选中状态改变监听器
     *
     * @param onCheckListener 当状态改变时会调用的监听器对象
     */
    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    /**
     * 设置动画时长
     *
     * @param duration 动画时长，单位：毫秒
     */
    public void setDuation(long duration) {
        mDuration = duration;
    }

    /**
     * 选中状态改变监听器
     */
    public interface OnCheckListener {
        void onCheck(boolean isCheck);
    }

    /**
     * 手势监听器，不用自己去判断点击
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //点击事件
            mCheck = !mCheck;
            if (mCheck) {
                startCheckAndNotify();
            } else {
                startUnCheckAndNotify();
            }
            return super.onSingleTapUp(e);
        }
    }
}
