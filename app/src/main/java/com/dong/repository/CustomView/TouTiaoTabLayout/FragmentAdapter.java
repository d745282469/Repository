package com.dong.repository.CustomView.TouTiaoTabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author pd
 * time     2019/4/9 14:45
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    List<TestFragment> fragments;
    List<String> titles;

    public FragmentAdapter(FragmentManager fm,List<TestFragment> fragments,List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titles.get(position);
//    }
}
