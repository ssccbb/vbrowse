package com.sung.vbrowse.interfaces;

import android.content.Context;

import com.sung.vbrowse.mvp.model.VideoInfo;

import java.util.ArrayList;

/**
 * @author: sung
 * @date: 2018/10/17
 * @Description:
 */
public interface IFileListFragmentPresenter {
    ArrayList<VideoInfo> getVideoInfo(Context context);
}
