package com.dong.repository.CustomView.TouTiaoTabLayout;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.repository.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pd
 * time     2019/4/9 16:08
 * 滑动状态：0：静止  1：手指滑动  2：自动滑动
 */
public class MyOnPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {
    private static final String TAG = "MyOnPageChangeListener";
    private List<Integer> tabsWidth;
    private List<TextView> textViewsToRight, textViewsToLeft;
    private float step;
    private Context context;
    private int scrollState, lastScrollState;
    private final int RECORD_TIMES = 8;//每间隔8次记录一次滑动位置
    private int times;

    private int lastPositionOffsetPix;

    public MyOnPageChangeListener(final TabLayout tabLayout, final Context context) {
        super(tabLayout);
        this.context = context;
        tabsWidth = new ArrayList<>();
        textViewsToRight = new ArrayList<>();
        textViewsToLeft = new ArrayList<>();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            final View v = tabLayout.getTabAt(i).getCustomView();
            //保证在view创建完成后获取到宽度
            v.post(new Runnable() {
                @Override
                public void run() {
                    tabsWidth.add(v.getWidth());
                    TextView textViewToRight = v.findViewById(R.id.tab_act_to_right);
                    TextView textViewToLeft = v.findViewById(R.id.tab_act_to_left);

                    ViewGroup.LayoutParams layoutParams = textViewToRight.getLayoutParams();
                    layoutParams.width = 0;
                    textViewToRight.setLayoutParams(layoutParams);

                    layoutParams = textViewToLeft.getLayoutParams();
                    layoutParams.width = 0;
                    textViewToLeft.setLayoutParams(layoutParams);

                    textViewsToRight.add(textViewToRight);
                    textViewsToLeft.add(textViewToLeft);
                    onPageSelected(0);//默认选中第一项
                }
            });

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        /**
         * 滑动状态，0：静止，1：手指滑动，2：自动滑动
         */
        //我们只在手指滑动和手指滑动过后的自动滑动做染色效果
        //由于没有点击监听，所以经过测试发现，点击的时候，滑动状态是才从0→2→0这样一个过程
        if (scrollState != 2 || lastScrollState != 0) {
            //手指滑动才进行染色，否则直接染色
            int currentPosition, nextPosition;

            int orientation = positionOffsetPixels - lastPositionOffsetPix;
            if (orientation > 0) {
                //向右
                currentPosition = position;
                nextPosition = position + 1;

                //操作下一个目标向右染色
                textViewsToLeft.get(nextPosition).setVisibility(View.GONE);
                textViewsToRight.get(nextPosition).setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = textViewsToRight.get(nextPosition).getLayoutParams();

                //设置宽度从0开始递增
                layoutParams.width = 0;
                textViewsToRight.get(nextPosition).setLayoutParams(layoutParams);

                step = Float.valueOf(tabsWidth.get(nextPosition)) / context.getResources().getDisplayMetrics().widthPixels;
                layoutParams.width = (int) (step * positionOffsetPixels);
                textViewsToRight.get(nextPosition).setLayoutParams(layoutParams);


                //操作当前目标向右取消染色
                textViewsToRight.get(currentPosition).setVisibility(View.GONE);
                textViewsToLeft.get(currentPosition).setVisibility(View.VISIBLE);
                layoutParams = textViewsToLeft.get(currentPosition).getLayoutParams();

                //设置宽度从满开始递减
                layoutParams.width = tabsWidth.get(currentPosition);
                textViewsToLeft.get(currentPosition).setLayoutParams(layoutParams);

                step = Float.valueOf(tabsWidth.get(currentPosition)) / context.getResources().getDisplayMetrics().widthPixels;
                layoutParams.width -= (int) (step * positionOffsetPixels);
                textViewsToLeft.get(currentPosition).setLayoutParams(layoutParams);
            } else if (orientation < 0 && position < tabsWidth.size() - 1) {
                //向左
                currentPosition = position + 1;
                nextPosition = position;

                //操作下一个目标向左染色
                textViewsToRight.get(nextPosition).setVisibility(View.GONE);
                textViewsToLeft.get(nextPosition).setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = textViewsToLeft.get(nextPosition).getLayoutParams();

                //设置宽度从0开始递增
                layoutParams.width = 0;
                textViewsToLeft.get(nextPosition).setLayoutParams(layoutParams);

                step = Float.valueOf(tabsWidth.get(nextPosition)) / context.getResources().getDisplayMetrics().widthPixels;
                layoutParams.width = (int) (tabsWidth.get(nextPosition) - (step * positionOffsetPixels));
                textViewsToLeft.get(nextPosition).setLayoutParams(layoutParams);


                //操作当前目标向左取消染色
                textViewsToLeft.get(currentPosition).setVisibility(View.GONE);
                textViewsToRight.get(currentPosition).setVisibility(View.VISIBLE);
                layoutParams = textViewsToRight.get(currentPosition).getLayoutParams();

                //设置宽度从满开始递减
                layoutParams.width = tabsWidth.get(currentPosition);
                textViewsToRight.get(currentPosition).setLayoutParams(layoutParams);

                step = Float.valueOf(tabsWidth.get(currentPosition)) / context.getResources().getDisplayMetrics().widthPixels;
                layoutParams.width = (int) (step * positionOffsetPixels);

                textViewsToRight.get(currentPosition).setLayoutParams(layoutParams);
            } else {
                //不动
                return;
            }

            //保证间隔次数间的差值够大，否则可能出现差值为0的情况
            if (times >= RECORD_TIMES) {
                lastPositionOffsetPix = positionOffsetPixels;
                times = 0;
            }
            times++;
        }
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        //将除了选中项之外的所有项的染色层的宽度都设置为0
        for (int i = 0; i < textViewsToRight.size(); i++) {
            ViewGroup.LayoutParams layoutParamsToRight = textViewsToRight.get(i).getLayoutParams();
            ViewGroup.LayoutParams layoutParamsToLeft = textViewsToLeft.get(i).getLayoutParams();
            layoutParamsToRight.width = 0;
            layoutParamsToLeft.width = 0;
            if (i == position) {
                //选中目标，将两个染色textView都设置为满宽度
                layoutParamsToRight.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParamsToLeft.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            textViewsToRight.get(i).setLayoutParams(layoutParamsToRight);
            textViewsToLeft.get(i).setLayoutParams(layoutParamsToLeft);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
        lastScrollState = scrollState;
        scrollState = state;
    }
}
