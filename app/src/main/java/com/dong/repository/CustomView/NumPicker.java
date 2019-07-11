package com.dong.repository.CustomView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/6/26 16:35
 */
public class NumPicker extends NumberPicker {
    private static final String TAG = "NumPicker";
    private Drawable selectDivier;

    public NumPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        updateView(child);
    }

    private void updateView(View child){
        if (child instanceof EditText){
            EditText editText = (EditText) child;
            editText.setTextColor(Color.RED);
            editText.setTextSize(20);
        }
    }
}
