package com.sung.vbrowse.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.sung.vbrowse.R;
import com.sung.vbrowse.adapter.IndexAdapter;
import com.sung.vbrowse.base.BaseActivity;
import butterknife.BindView;

/**
 * @author: sung
 * @date: 2018/10/16
 * @Description:
 */
public class IndexActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    private IndexAdapter mHomeAdapter;

    @BindView(R.id.view_pager)
    ViewPager mPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        mHomeAdapter = new IndexAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(mHomeAdapter);
        mPager.setOffscreenPageLimit(mHomeAdapter.getCount());
        mPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        mPager.setCurrentItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public static void open(Activity context){
        if (context == null) return;
        context.startActivity(new Intent(context,IndexActivity.class));
    }

}
