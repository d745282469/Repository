package com.dong.repository.Show;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.dong.repository.Base.BaseActivity;
import com.dong.repository.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示BaseAdapter的用法
 */
public class ShowBaseAdapterActivity extends BaseActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(initLayout());
        context = this;
//        init();
    }

    @Override
    public void init() {
        RecyclerView recyclerView = findViewById(R.id.show_recyclerView_rv);
        //模拟数据
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < 60; i++){
            itemList.add("TestAdapter_"+i);
        }
        TestAdapter adapter = new TestAdapter(getContext(),itemList,R.layout.item_scroll_menu);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SimpleItemTouchCallBack itemTouchCallBack = new SimpleItemTouchCallBack(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_show_base_adapter;
    }
}
