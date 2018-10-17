package com.sung.vbrowse.mvp.ui.activity;

import android.os.Handler;

import com.sung.vbrowse.R;
import com.sung.vbrowse.base.BaseActivity;

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
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        super.init();
        mUIHandler.postDelayed(mSkipRunnable,mSkipTime);
    }

    private void goTo(){
        IndexActivity.open(this);
    }
}
