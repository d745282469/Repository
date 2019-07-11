package com.dong.repository.CustomView.TouTiaoTabLayout;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dong.repository.Base.BaseActivity;
import com.dong.repository.R;

import java.util.ArrayList;
import java.util.List;

public class TouTiaoLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;

        TabLayout tabLayout = findViewById(R.id.main_tab);

        //模拟数据源
        List<TestFragment> fragments = new ArrayList<>();
        List<String> tabs = new ArrayList<>();
        for (int i = 0; i < 15; i++){
            tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab));

            //将三个textView都设置好内容
            TextView textView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_act_to_left);
            textView.setText("tab_"+i);
            textView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_act_to_right);
            textView.setText("tab_"+i);
            textView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_normal);
            textView.setText("tab_"+i);

            TestFragment testFragment = new TestFragment();
            fragments.add(testFragment);
        }

        ViewPager viewPager = findViewById(R.id.main_viewpager);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragments,tabs);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener(tabLayout,context));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    public void init() {

    }

    @Override
    public int initLayout() {
        return R.layout.activity_tou_tiao_layout;
    }
}
