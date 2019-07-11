package com.dong.repository.CustomView.TouTiaoTabLayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.repository.R;

/**
 * @author pd
 * time     2019/4/9 14:42
 */
public class TestFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_content,container,false);
        TextView textView = new TextView(getContext());
        textView.setText(String.valueOf(Math.random()));
        container.addView(textView);
        return view;
    }
}
