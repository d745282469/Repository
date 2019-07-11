package com.dong.repository.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dong.repository.R;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/7/11 08:34
 * 通用的dialog工具类
 */
public class DialogUtil {
    private static final String TAG = "DialogUtil";
    private static Dialog dialog;
    private static final int NORMAL_WIDTH = 300;//常用宽度，dp

    public static boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    public static void dismiss() {
        if (isShowing()) {
            dialog.dismiss();
        }
    }

    public static void show() {
        if (dialog != null) {
            dismiss();
            dialog.show();
        } else {
            Log.e(TAG, "dialog is null!");
        }
    }

    /**
     * 提醒弹窗
     *
     * @param context  上下文
     * @param title    标题
     * @param msg      提醒内容
     * @param listener 确定监听
     */
    public static void alert(Context context, String title, String msg, final onConfirmListener listener) {
        //加载布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_alert, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView btnConfirm = view.findViewById(R.id.btn_confirm);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);

        //设置布局属性
        tvTitle.setText(title);
        tvContent.setText(msg);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm();
                }
            }
        });
        initNormalDialog(context, view, false);
        show();
    }

    /**
     * 加载中Dialog
     * 使用系统自带的ProgressDialog
     *
     * @param context 上下文
     * @param msg     内容
     */
    public static void loading(Context context, String msg) {
        dialog = new ProgressDialog(context);
        ((ProgressDialog) dialog).setMessage(msg);
        dialog.setCanceledOnTouchOutside(false);//点击外部不消失
        show();
    }

    /**
     * 常见的Dialog初始化
     *
     * @param context              上下文
     * @param contentView          要显示的View
     * @param canceledTouchOutside 是否允许点击外部消失
     */
    private static void initNormalDialog(Context context, View contentView, boolean canceledTouchOutside) {
        dialog = new Dialog(context, R.style.Dialog_Normal);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(canceledTouchOutside);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = UnitUtil.dip2px(context, NORMAL_WIDTH);
        window.setAttributes(layoutParams);
    }

    /**
     * 可自定义Dialog的Window属性
     *
     * @param context      上下文
     * @param contentView  要显示的View
     * @param layoutParams 属性参数
     */
    private void initCustomDialog(Context context, View contentView, WindowManager.LayoutParams layoutParams) {
        dialog = new Dialog(context, R.style.Dialog_Normal);
        dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setAttributes(layoutParams);
    }

    /*---------------------监听器开始---------------------*/

    /**
     * 确认监听器
     */
    public interface onConfirmListener {
        void onConfirm();
    }
}
