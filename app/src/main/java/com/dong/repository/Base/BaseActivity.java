package com.dong.repository.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dong.repository.CustomView.TitleBar;
import com.dong.repository.R;
import com.dong.repository.Util.ActivityController;
import com.dong.repository.Util.Log;

/**
 * @author pd
 * time     2019/4/7 09:21
 */
public abstract class BaseActivity extends AppCompatActivity {
    private LinearLayout rootView;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getClass().getSimpleName()+" is create.");
        ActivityController.addActivity(this);
        context = this;

        rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        FrameLayout contentView = new FrameLayout(context);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(params);
        contentView.addView(LayoutInflater.from(context).inflate(initLayout(), null));
        rootView.addView(contentView);

        setContentView(rootView);
        initTitleBar();
        init();
    }

    /**
     * 初始化自定义标题栏
     */
    private void initTitleBar(){
        TitleBar titleBar = new TitleBar(context, null);
        titleBar.setTitle(setTitle());
        rootView.addView(titleBar,0);
    }

    public Context getContext(){
        return context;
    }

    /**
     * 一些和UI相关的初始化动作，由子类重写
     */
    public abstract void init();

    /**
     * 要加载的布局文件的ID，由子类重写
     * 因为需要使用自定义ToolBar
     *
     * @return 布局文件ID
     */
    public abstract int initLayout();

    /**
     * 要设置的标题栏标题，默认为当前应用的名字
     *
     * @return 标题
     */
    public String setTitle(){
        return getResources().getString(R.string.app_name);
    }

    /**
     * 短Toast
     * @param msg
     */
    public void toastShort(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 长Toast
     * @param msg
     */
    public void toastLong(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        Log.d(this.getClass().getName()+" is pause.");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(this.getClass().getName() + " is stop.");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ActivityController.removeActivity(this);
        super.onDestroy();
    }
}
