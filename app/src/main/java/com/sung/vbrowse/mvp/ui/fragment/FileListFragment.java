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
 * @Description: 文件列表页面
 */
public class FileListFragment extends BaseFragment implements IFileListFragmentView {
    private FileListFragmentPresenter mPresenter;

    @BindView(R.id.rc_list)
    RecyclerView mList;

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
        mPresenter = new FileListFragmentPresenter(this);
        mPresenter.showFileList(getContext());
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mList;
    }
}
