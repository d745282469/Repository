package com.dong.repository.CustomView.MultiStatusLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dong.repository.Base.BaseActivity;
import com.dong.repository.R;

public class MultiStatusLayoutBaseActivity extends BaseActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btn_show_content = findViewById(R.id.multi_btn_show_content);
        Button btn_show_loading = findViewById(R.id.multi_btn_show_loading);
        Button btn_show_empty = findViewById(R.id.multi_btn_show_empty);
        Button btn_show_error = findViewById(R.id.multi_btn_show_Error);
        final MultiStatusLayout multiStatusLayout = findViewById(R.id.multi_status_layout);
        Button btn_error_try_again = multiStatusLayout.getErrorView().findViewById(R.id.test_error_btn);

        btn_show_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiStatusLayout.showContentView();
            }
        });

        btn_show_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiStatusLayout.showEmptyView();
            }
        });

        btn_show_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiStatusLayout.showLoadingView();
            }
        });

        btn_show_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiStatusLayout.showErrorView();
            }
        });

        btn_error_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiStatusLayout.showLoadingView();
                multiStatusLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        multiStatusLayout.showContentView();
                    }
                },3000);
            }
        });
    }

    @Override
    public void init() {

    }

    @Override
    public int initLayout() {
        return R.layout.activity_multi_status_layout;
    }

    @Override
    public String setTitle() {
        return "多状态布局容器";
    }
}
