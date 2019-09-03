package com.dong.repository;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.dong.repository.Base.BaseActivity;
import com.dong.repository.CustomView.MultiStatusLayout.MultiStatusLayoutBaseActivity;
import com.dong.repository.CustomView.ScrollMenu.ScrollMenuActivity;
import com.dong.repository.CustomView.TouTiaoTabLayout.TouTiaoLayoutActivity;
import com.dong.repository.Show.ShowBaseAdapterActivity;
import com.dong.repository.Show.ShowFloatBtnActivity;
import com.dong.repository.Util.ActivityController;
import com.dong.repository.Util.DialogUtil;
import com.dong.repository.Util.Log;
import com.dong.repository.Util.PermissionTool.PermissionTool;
import com.dong.repository.Util.PopWindowUtil;
import com.pd.switchbutton.SwitchButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private LinearLayout ll_root;
    private Context context;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "触发onNewIntent，当前活动栈：" + ActivityController.activityList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ImageView imageView = findViewById(R.id.main_iv_test);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("d", "通知接口：d", NotificationManager.IMPORTANCE_DEFAULT);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), FLAG_ONE_SHOT);
            Notification notification = new Notification.Builder(context, channel.getId())
                    .setContentText("测试通知")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .build();
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            manager.notify(0, notification);
        }
//        ImgLoader.getBitmap(this, "https://upload.jianshu.io/users/upload_avatars/15776699/25764712-223e-4d6b-be5f-f06c98d25927?imageMogr2/auto-orient/strip|imageView2/1/w/120/h/120", new ImgLoader.GetBitmapCallback() {
//            @Override
//            public void success(Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void fail() {
//                Toast.makeText(MainActivity.this,"getBitmapFail",Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void getPermission() {
        Button btn_permission = findViewById(R.id.main_btn_permission);
        btn_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> permissions = new ArrayList<>();
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                PermissionTool.getPermission(MainActivity.this, permissions, new PermissionTool.GetPermissionResultListener() {
                    @Override
                    public void getPermissionSuccess() {
                        Log.d("success");
                    }

                    @Override
                    public void getPermissionFail(List<String> permissions) {
                        Log.e("fail");
                    }
                });
            }
        });

    }

    @Override
    public void init() {
        context = this;
        getPermission();
        Button btn_show_floatBtn = findViewById(R.id.main_btn_show_floatBtn);
        btn_show_floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ShowFloatBtnActivity.class));
            }
        });

        SwitchButton switchButton = findViewById(R.id.sb);
        switchButton.setOnCheckListener(new SwitchButton.OnCheckListener() {
            @Override
            public void onCheck(boolean b) {
                Log.d(TAG, b ? "选中了" : "取消选中");
            }
        });

//        ll_root = findViewById(R.id.main_ll_root);
        Button btn_multi_status_layout = findViewById(R.id.main_btn_multi_status_layout);
        btn_multi_status_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MultiStatusLayoutBaseActivity.class));
            }
        });

        Button btn_toutiao = findViewById(R.id.main_btn_toutiao);
        btn_toutiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, TouTiaoLayoutActivity.class));
            }
        });

        Button btn_scroll_menu = findViewById(R.id.main_btn_scroll_menu);
        btn_scroll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ScrollMenuActivity.class));
            }
        });

        Button btn_recycler_view = findViewById(R.id.main_btn_recyclerView);
        btn_recycler_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ShowBaseAdapterActivity.class));
            }
        });

        Button btn_dialog = findViewById(R.id.main_btn_dialog);
        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogUtil.alert(context, "提示", "这是一个自定义的提醒弹窗", new DialogUtil.onConfirmListener() {
//                    @Override
//                    public void onConfirm() {
//                        toastShort("confirm");
//                        DialogUtil.dismiss();
//                    }
//                });
                DialogUtil.loading(context, "加载中");
            }
        });

        TextView btn_popupWindow = findViewById(R.id.main_btn_popwindow);
        btn_popupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopWindowUtil.demo1(context, v);
            }
        });

        //监听WIFI链接改变广播
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.isConnected()) {
                    android.util.Log.d(TAG, "wifi ssid : " + networkInfo.getExtraInfo());
                }
            }
        }, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));


        NumberPicker numberPicker = findViewById(R.id.test_nump);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(0);
        Log.d(TAG, "子View数量：" + numberPicker.getChildCount());

        try {
            Field field = NumberPicker.class.getDeclaredField("mSelectionDivider");
            field.setAccessible(true);
            field.set(numberPicker, new ColorDrawable(Color.GREEN));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        });
    }
}
