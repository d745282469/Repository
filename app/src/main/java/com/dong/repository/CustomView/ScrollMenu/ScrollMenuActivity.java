package com.dong.repository.CustomView.ScrollMenu;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dong.repository.Base.BaseActivity;
import com.dong.repository.MainActivity;
import com.dong.repository.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollMenuActivity extends BaseActivity {
    private static final String TAG = "ScrollMenuActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        RecyclerView recyclerView = findViewById(R.id.scroll_menu_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < 30; i++){
            itemList.add("Test_Data_Item_DDDDDDDDD" + i);
        }
        ScrollMenuAdapter adapter = new ScrollMenuAdapter(context,itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void init() {

    }

    @Override
    public int initLayout() {
        return R.layout.activity_scroll_menu;
    }

    @Override
    public String setTitle() {
        return "低入侵的侧滑删除控件";
    }
}
