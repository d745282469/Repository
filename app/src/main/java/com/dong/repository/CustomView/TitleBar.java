package com.dong.repository.CustomView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dong.repository.R;

/**
 * @author pd
 * time 2019年4月9日09:29:03
 * 自定义标题栏，需要将使用的活动主题修改为NoActionBar
 */
public class TitleBar extends LinearLayout {
    private Context context;
    private View rootView;
    private LinearLayout ll_back, ll_right;
    private TextView tv_title;
    private ImageView iv_right,iv_left;

    private OnRightIconClickListener onRightIconClickListener;
    private OnLeftIconClickListener onLeftIconClickListener;

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        rootView = LayoutInflater.from(context).inflate(R.layout.custom_title_bar, this);
        initView();
        initEvent();
    }

    private void initView() {
        ll_back = rootView.findViewById(R.id.ll_bar_back);
        ll_right = rootView.findViewById(R.id.ll_bar_right);
        tv_title = rootView.findViewById(R.id.tv_bar_title);
        iv_right = rootView.findViewById(R.id.iv_bar_right);
        iv_left = rootView.findViewById(R.id.iv_bar_left);
    }

    private void initEvent() {
        ll_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onLeftIconClickListener == null) {
                    //没设置左边按钮点击事件时，默认退出当前活动
                    Activity activity = (Activity) getContext();
                    activity.finish();
                } else {
                    onLeftIconClickListener.onClick();
                }
            }
        });

        ll_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRightIconClickListener != null) {
                    onRightIconClickListener.onClick();
                }
            }
        });
    }

    public void setOnRightIconClickListener(OnRightIconClickListener onRightIconClickListener) {
        this.onRightIconClickListener = onRightIconClickListener;
    }

    public void setOnLeftIconClickListener(OnLeftIconClickListener onLeftIconClickListener) {
        this.onLeftIconClickListener = onLeftIconClickListener;
    }

    public interface OnRightIconClickListener {
        void onClick();
    }

    public interface OnLeftIconClickListener {
        void onClick();
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void showLeftIcon(boolean b) {
        if (b) {
            ll_back.setVisibility(VISIBLE);
        } else {
            ll_back.setVisibility(INVISIBLE);
        }
    }

    public void showRightIcon(boolean b) {
        if (b) {
            ll_right.setVisibility(VISIBLE);
        } else {
            ll_right.setVisibility(INVISIBLE);
        }
    }

    public void setRightIcon(Drawable drawable) {
        iv_right.setBackground(drawable);
    }

    public void setLeftIcon(Drawable drawable){
        iv_left.setBackground(drawable);
    }
}
