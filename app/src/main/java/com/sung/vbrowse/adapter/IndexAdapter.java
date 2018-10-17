package com.sung.vbrowse.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.sung.vbrowse.base.BaseFragment;
import com.sung.vbrowse.mvp.ui.fragment.FileListFragment;
import com.sung.vbrowse.mvp.ui.fragment.SettingListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sung
 * @date: 2018/10/17
 * @Description:
 */
public class IndexAdapter extends FragmentPagerAdapter {
    private FragmentManager manager;
    private List<BaseFragment> fragments = new ArrayList();

    private Context context;

    public IndexAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initFragments();
    }

    private void initFragments(){
        if (fragments == null) fragments = new ArrayList();
        fragments.add(FileListFragment.getInstance());
        fragments.add(SettingListFragment.getInstance());
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
