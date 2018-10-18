package com.sung.vbrowse.interfaces;

import android.app.Activity;
import android.view.SurfaceView;

import com.sung.vbrowse.mvp.model.VideoInfo;

/**
 * @author: sung
 * @date: 2018/10/18
 * @Description:
 */
public interface IPlayerView {
    void showError(int code);

    void showLoading();

    void dismissLoading();

    void play();

    void pause();

    void stop();

    void replay();

    void updateProgress(int progress);

    SurfaceView getPlayerView();

    VideoInfo getVideoInfo();

    Activity getContext();
}
