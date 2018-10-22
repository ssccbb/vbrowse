package com.sung.vbrowse.interfaces;

/**
 * Create by sung at 2018/10/22
 *
 * @Description: 监听器
 */
public interface MediaControllerListener {

    void onBackUp();

    void onPlayStatusChange(boolean pause);

    void onDisplayChange(boolean isLanscape);

    void onLightChange(float light);
}
