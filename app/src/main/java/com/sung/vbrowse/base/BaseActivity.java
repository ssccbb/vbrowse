package com.sung.vbrowse.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * @author: sung
 * @date: 2018/10/15
 * @Description: activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        /* ButterKnife */ButterKnife.bind(this);
        /* EventBus */EventBus.getDefault().register(this);
        init();
        setData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        System.gc();
    }

    protected int getLayoutID() {
        return -1;
    }

    protected void init() {

    }

    protected void setData() {

    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <T extends BaseFragment> T findFragment(int id) {
        return (T) getSupportFragmentManager().findFragmentById(id);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <T extends BaseFragment> T findFragment(String tag) {
        return (T) getSupportFragmentManager().findFragmentByTag(tag);
    }

    protected Context getContext() {
        Context context = BaseApplication.getInstance().getApplicationContext();
        return context != null ? context : this;
    }

    protected SharedPreferences getPreferences() {
        return BaseApplication.getInstance().getPreferences();
    }

    protected FragmentTransaction getSupportFragmentTransaction() {
        return getSupportFragmentManager().beginTransaction();
    }

    public void onBackPress() {
        try {
            super.onBackPressed();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
