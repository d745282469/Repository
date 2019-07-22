package com.dong.repository.Base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/7/22 09:11
 * Fragment基类，封装一个懒加载的生命周期
 */
public class BaseFragment extends Fragment {
    private boolean isOnCreateView = false;
    private boolean isVisibleToUser = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        isOnCreateView = true;
        lazyLoad();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        lazyLoad();
    }

    /**
     * 由子类去重写的懒加载生命周期函数
     * 该函数调用时表示Fragment已经到前台了
     */
    public void onLazyLoad(){}

    private void lazyLoad(){
        if (isOnCreateView && isVisibleToUser) {
            isVisibleToUser = false;//重置标记
            isOnCreateView = false;
            onLazyLoad();
        }
    }
}
