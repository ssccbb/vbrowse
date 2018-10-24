package com.sung.vbrowse.mvp.model;

import android.graphics.Bitmap;

import com.sung.vbrowse.utils.VPlayerHelper;

import java.io.Serializable;

/**
 * @author: sung
 * @date: 2018/10/17
 * @Description:
 */
public class VideoInfo implements Serializable {
    public String filePath;
    public String mimeType;
    public Bitmap thumb;
    public String title;
    public VPlayerHelper.PlayerMode type;
    public String netUrl;
}
