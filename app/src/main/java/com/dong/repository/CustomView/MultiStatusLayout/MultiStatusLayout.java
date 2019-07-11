package com.dong.repository.CustomView.MultiStatusLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.dong.repository.R;

/**
 * @author pd
 * time     2019/4/8 15:23
 * 多状态容器，没提供什么特别的功能，主要就是方便切换不同的布局文件
 * 自定义属性在value/attr文件中，复制使用时需要将自定义属性也复制过去
 */
public class MultiStatusLayout extends FrameLayout {
    private FrameLayout rootView;
    private View content_view, empty_view, loading_view, error_view;

    public MultiStatusLayout(Context context) {
        this(context, null);
    }

    public MultiStatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiStatusLayout, defStyleAttr, 0);

        int loading_view_id = typedArray.getResourceId(R.styleable.MultiStatusLayout_loading_view, 0);
        if (loading_view_id != 0) {
            loading_view = LayoutInflater.from(context).inflate(loading_view_id, null);
            addView(loading_view);
            showLoadingView();
        }

        int error_view_id = typedArray.getResourceId(R.styleable.MultiStatusLayout_error_view, 0);
        if (error_view_id != 0) {
            error_view = LayoutInflater.from(context).inflate(error_view_id, null);
            addView(error_view);
            showErrorView();
        }

        int empty_view_id = typedArray.getResourceId(R.styleable.MultiStatusLayout_empty_view, 0);
        if (empty_view_id != 0) {
            empty_view = LayoutInflater.from(context).inflate(empty_view_id, null);
            addView(empty_view);
            showEmptyView();
        }

        int content_view_id = typedArray.getResourceId(R.styleable.MultiStatusLayout_content_view, 0);
        if (content_view_id != 0) {
            content_view = LayoutInflater.from(context).inflate(content_view_id, null);
            addView(content_view);
            showContentView();
        }

        typedArray.recycle();
    }

    public void setContentView(View v) {
        content_view = v;
        addView(v);
    }

    public View getContentView() {
        return content_view;
    }

    public void setEmptyView(View v) {
        empty_view = v;
        addView(v);
    }

    public View getEmptyView() {
        return empty_view;
    }

    public void setLoadingView(View v) {
        loading_view = v;
        addView(v);
    }

    public View getLoadingView() {
        return loading_view;
    }

    public void setErrorView(View v) {
        error_view = v;
        addView(v);
    }

    public View getErrorView() {
        return error_view;
    }

    private void hideAll() {
        for (int i = 0; i < getChildCount(); i++){
            getChildAt(i).setVisibility(GONE);
        }
    }

    public void showContentView() {
        if (content_view != null) {
            hideAll();
            content_view.setVisibility(VISIBLE);
        }
    }

    public void showEmptyView() {
        if (empty_view != null) {
            hideAll();
            empty_view.setVisibility(VISIBLE);
        }
    }

    public void showLoadingView() {
        if (loading_view != null) {
            hideAll();
            loading_view.setVisibility(VISIBLE);
        }
    }

    public void showErrorView() {
        if (error_view != null) {
            hideAll();
            error_view.setVisibility(VISIBLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
