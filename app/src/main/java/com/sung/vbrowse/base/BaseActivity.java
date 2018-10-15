package com.sung.vbrowse.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @author: sung
 * @date: 2018/10/15
 * @Description:
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
    }

    protected int getLayoutID() {
        return 0;
    }

    protected void init() {

    }

    protected void setData() {

    }

    protected Context getContext() {
        return this.getApplicationContext() != null ? this.getApplicationContext() : this;
    }

    protected void onBackPress() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        System.gc();
    }
}
