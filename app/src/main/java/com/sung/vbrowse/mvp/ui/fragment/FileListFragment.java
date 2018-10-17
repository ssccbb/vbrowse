package com.sung.vbrowse.mvp.ui.fragment;

import com.sung.vbrowse.R;
import com.sung.vbrowse.base.BaseFragment;

/**
 * @author: sung
 * @date: 2018/10/17
 * @Description: 文件列表页面
 */
public class FileListFragment extends BaseFragment {

    private static class Holder {
        private static FileListFragment INSTANCE = new FileListFragment();
    }

    public static FileListFragment getInstance(){
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
