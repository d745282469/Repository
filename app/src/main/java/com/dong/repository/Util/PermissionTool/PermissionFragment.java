package com.dong.repository.Util.PermissionTool;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/7/4 16:06
 * 主要为了支持非support包
 */
public class PermissionFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionTool.onResult(requestCode,permissions,grantResults);
    }
}
