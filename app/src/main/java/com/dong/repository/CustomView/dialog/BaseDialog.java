package com.dong.repository.CustomView.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.dong.repository.R;

/**
 * @author pd
 * time     2019/4/8 13:38
 */
public abstract class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Normal);
        setContentView(setView());
    }

    public abstract int setView();

    public abstract void init();
}
