package com.sung.vbrowse.mvp.contract;

import android.content.Context;
import com.sung.vbrowse.interfaces.IFileListFragmentPresenter;
import com.sung.vbrowse.mvp.model.VideoInfo;
import com.sung.vbrowse.utils.MediaStoreUtils;

import java.util.ArrayList;

/**
 * @author: sung
 * @date: 2018/10/17
 * @Description:
 */
public class FileListFragmentContract implements IFileListFragmentPresenter {

    @Override
    public ArrayList<VideoInfo> getVideoInfo(Context context) {
        ArrayList<VideoInfo> videoInfos = MediaStoreUtils.getVideoInfo(context);
        if (videoInfos == null)
            return new ArrayList<>();
        return videoInfos;
    }
}
