package dong.floatbuttonlayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/8/28 08:53
 */
public class FloatButtonLayout extends RelativeLayout {
    private static final String TAG = "FloatButton";
    private int itemMargin;//间距
    private boolean isShow;//是否展开，true已展开
    private int direction;//展开的方向
    private int duration;//动画时间

    private AnimatorManager animatorManager;

    public static final int DIR_TOP = 0;
    public static final int DIR_BOTTOM = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;

    public FloatButtonLayout(Context context) {
        this(context, null);
    }

    public FloatButtonLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatButtonLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FloatButtonLayout);
        itemMargin = array.getInteger(R.styleable.FloatButtonLayout_itemMargin, 40);
        direction = array.getInteger(R.styleable.FloatButtonLayout_direction, DIR_TOP);
        duration = array.getInteger(R.styleable.FloatButtonLayout_duration, 150);
        array.recycle();

        isShow = false;

        //允许子元素超出父元素显示
        setClipChildren(false);
        setClipToPadding(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //如果不设置内边距，会出现阴影被矩形拦截的情况
        int padding = 20;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            padding = (int) getChildAt(0).getElevation();
            padding += 20;
        }
        setPadding(padding, padding, padding, padding);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View item = getChildAt(i);
            if (i < count - 1) {
//                item.setAlpha(0);//先隐藏，不然会出现阴影叠加的情况
            }

            LayoutParams lp = (LayoutParams) item.getLayoutParams();
            //根据展开方向确定未展开时的位置
            if (direction == DIR_TOP) {
                //展开方向为向上，则未展开时在父元素的底部
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            } else if (direction == DIR_BOTTOM) {
                //展开方向为向下，则未展开时在父元素的顶部
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            } else if (direction == DIR_LEFT) {
                //展开方向为向左，则未展开时在父元素的右边
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            } else if (direction == DIR_RIGHT) {
                //展开方向为向右，则未展开时在父元素的左边
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            }
            item.setLayoutParams(lp);
            item.setClickable(true);//保证按钮的点击效果可以出来
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View childView = getChildAt(getChildCount() - 1);
        childView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //由于第一次加载的时候所有子View都在一个位置
                //宽高不够，因此要计算展开后的宽高
                if (isHorizontal()) {
                    int width = v.getWidth();
                    int maxWidth = getChildCount() * width +
                            ((getChildCount() - 1) * itemMargin) +
                            getPaddingLeft() + getPaddingRight();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setWidth((int) (maxWidth + v.getElevation()));
                    } else {
                        setWidth(maxWidth);
                    }
                } else if (isVertical()) {
                    int itemHeight = v.getHeight();
                    int maxHeight = getChildCount() * itemHeight +
                            ((getChildCount() - 1) * itemMargin) +
                            getPaddingTop() + getPaddingBottom();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setHeight((int) (maxHeight + v.getElevation()));
                    } else {
                        setHeight(maxHeight);
                    }
                }

                if (isShow) {
                    startCloseAnimate();
                } else {
                    startShowAnimate();
                }

            }
        });
    }

    public boolean isHorizontal() {
        return direction == DIR_RIGHT || direction == DIR_LEFT;
    }

    public boolean isVertical() {
        return direction == DIR_TOP || direction == DIR_BOTTOM;
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.width = width;
        setLayoutParams(lp);
    }

    public int getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
    }

    /**
     * 执行展开动画
     */
    public void startShowAnimate() {
        if (animatorManager == null) {
            startDefaultShowAnimate(getChildCount() - 2);
            startDefaultSwitchShowAnimate();
            return;
        }
        isShow = true;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            AnimatorSet set;
            if (i == 0) {
                set = animatorManager.getSwitchShowAnimator(getChildAt(i),this);
            } else {
                set = animatorManager.getShowAnimator(i,getChildAt(i),this);
            }
            if (set != null) {
                set.start();
            }
        }
    }

    /**
     * 执行关闭动画
     */
    public void startCloseAnimate(){
        if (animatorManager == null) {
            startDefaultSwitchCloseAnimate();
            startDefaultCloseAnimate(getChildCount() - 2);
            return;
        }
        isShow = false;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            AnimatorSet set;
            if (i == 0) {
                set = animatorManager.getSwitchCloseAnimator(getChildAt(i),this);
            } else {
                set = animatorManager.getCloseAnimator(i,getChildAt(i),this);
            }
            if (set != null) {
                set.start();
            }
        }
    }

    public void setAnimatorManager(AnimatorManager animatorManager) {
        this.animatorManager = animatorManager;
    }

    private void startDefaultSwitchShowAnimate(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(getChildAt(getChildCount() - 1),
                "Rotation",45);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    private void startDefaultSwitchCloseAnimate(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(getChildAt(getChildCount() - 1),
                "Rotation",0);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 默认展开时的动画
     *
     * @param childIndex 子元素对应的索引
     */
    private void startDefaultShowAnimate(final int childIndex) {
        isShow = true;
        AnimatorSet set = new AnimatorSet();
        if (childIndex < 0) {
            return;
        }

        ObjectAnimator tranObj;
        if (direction == DIR_TOP || direction == DIR_BOTTOM) {
            //计算Y轴要移动的距离
            int itemHeight = getChildAt(childIndex).getHeight();
            float moveY = (getChildCount() - 1 - childIndex) * itemHeight +
                    (getChildCount() - 1 - childIndex) * itemMargin;
            if (direction == DIR_TOP) {
                moveY = moveY * -1;
            }
            Log.d(TAG, "childIndex=" + childIndex + ",moveY=" + moveY);
            tranObj = ObjectAnimator.ofFloat(getChildAt(childIndex), "translationY", moveY);
        } else {
            //计算X轴要移动的距离
            int itemWidth = getChildAt(childIndex).getWidth();
            float moveX = (getChildCount() - 1 - childIndex) * itemWidth +
                    (getChildCount() - 1 - childIndex) * itemMargin;
            if (direction == DIR_LEFT) {
                moveX = moveX * -1;
            }
            Log.d(TAG, "childIndex=" + childIndex + ",moveX=" + moveX);
            tranObj = ObjectAnimator.ofFloat(getChildAt(childIndex), "translationX", moveX);
        }
        tranObj.setDuration(duration * (getChildCount() - childIndex));

        ObjectAnimator alphaObj = ObjectAnimator.ofFloat(getChildAt(childIndex), "alpha", 1);
        alphaObj.setDuration((duration + 100) * (getChildCount() - childIndex));

        set.play(tranObj).with(alphaObj);
//        Log.d(TAG, "open time=" + set.getDuration() + ",childIndex=" + childIndex);
        set.start();
        startDefaultShowAnimate(childIndex - 1);
    }

    private void startDefaultCloseAnimate(int childIndex) {
        isShow = false;
        if (childIndex < 0) {
            return;
        }

        //垂直方向移动到原点
        ObjectAnimator tranYObj = ObjectAnimator.ofFloat(getChildAt(childIndex),
                "translationY", 0);
        tranYObj.setDuration(duration * (getChildCount() - childIndex));

        //水平方向移动到原点
        ObjectAnimator tranXObj = ObjectAnimator.ofFloat(getChildAt(childIndex),
                "translationX", 0);
        tranXObj.setDuration(duration * (getChildCount() - childIndex));

        //透明度变为0
        ObjectAnimator alphaObj = ObjectAnimator.ofFloat(getChildAt(childIndex), "alpha", 1, 0);
        alphaObj.setDuration((duration + 100) * (getChildCount() - childIndex));

        AnimatorSet set = new AnimatorSet();
        set.play(tranYObj).with(alphaObj).with(tranXObj);
//        Log.d(TAG, "close time=" + tranObj.getDuration() + ",childIndex=" + childIndex);
        set.start();
        startDefaultCloseAnimate(childIndex - 1);
    }

    public void setOnItemClickListener(final FloatButtonLayout.onItemClickListener onItemClickListener) {
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final int finalI = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, finalI);
                    startCloseAnimate();
                }
            });
        }
    }

    public interface onItemClickListener {
        void onClick(View v, int index);
    }
}
