package com.sung.vbrowse.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sung.vbrowse.adapter.FileListAdapter;
import com.sung.vbrowse.interfaces.IFileListFragmentPresenter;
import com.sung.vbrowse.interfaces.IFileListFragmentView;
import com.sung.vbrowse.mvp.contract.FileListFragmentContract;
import com.sung.vbrowse.mvp.model.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sung
 * @date: 2018/10/17
 */
public class FileListFragmentPresenter {
    private IFileListFragmentView mFileListView;
    private IFileListFragmentPresenter mFileListContract;

    private FileListAdapter mFileListAdapter;

    public FileListFragmentPresenter(IFileListFragmentView mFileListView) {
        this.mFileListView = mFileListView;
        mFileListContract = new FileListFragmentContract();
    }

    /**
     * 展示手机上视频文件列表
     */
    public void showFileList(Context context) {
        RecyclerView mList = mFileListView.getRecyclerView();
        mList.setHasFixedSize(true);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<VideoInfo> mData = mFileListContract.getVideoInfo(context);
        mFileListAdapter = new FileListAdapter(context, mData);
        mList.setAdapter(mFileListAdapter);
    }

    /**
     * 刷新列表数据
     */
    public void notifyFileList(List<VideoInfo> data, boolean clear) {
        mFileListAdapter.addData(data, clear);
    }
}
