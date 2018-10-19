package com.sung.vbrowse.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sung.vbrowse.BaseApplication;

import butterknife.ButterKnife;

/**
 * @author: sung
 * @date: 2018/10/15
 * @Description: activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
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
        return this;
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
