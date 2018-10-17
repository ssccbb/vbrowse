package com.sung.vbrowse.mvp.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.sung.vbrowse.R;
import com.sung.vbrowse.base.BaseFragment;
import com.sung.vbrowse.interfaces.IFileListFragmentView;
import com.sung.vbrowse.mvp.presenter.FileListFragmentPresenter;

import butterknife.BindView;

/**
 * @author: sung
 * @date: 2018/10/17
 * @Description: 设置列表页面
 */
public class SettingListFragment extends BaseFragment {

    private static class Holder {
        private static SettingListFragment INSTANCE = new SettingListFragment();
    }

    public static SettingListFragment getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file_list;
    }

    @Override
    protected void init() {
        super.init();
    }

}
