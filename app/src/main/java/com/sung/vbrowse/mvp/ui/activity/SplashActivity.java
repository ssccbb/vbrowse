package com.sung.vbrowse.mvp.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sung.vbrowse.R;
import com.sung.vbrowse.base.BaseActivity;
import com.sung.vbrowse.utils.PermissionsHelper;

public class SplashActivity extends BaseActivity {
    private final long mSkipTime = 2000;
    private Handler mUIHandler = new Handler();
    private Runnable mSkipRunnable = new Runnable() {
        @Override
        public void run() {
            goTo();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mUIHandler.postDelayed(mSkipRunnable,mSkipTime);
        checkPermission();
    }

    private void checkPermission(){
        PermissionsHelper mPermissionsHelper = new PermissionsHelper(this);
        if (!mPermissionsHelper.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            mUIHandler.removeCallbacksAndMessages(null);
            mPermissionsHelper.permissionsCheck(Manifest.permission.READ_EXTERNAL_STORAGE,100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            mUIHandler.postDelayed(mSkipRunnable,mSkipTime);
        }
    }

    private void goTo(){
        IndexActivity.open(this);
    }
}
