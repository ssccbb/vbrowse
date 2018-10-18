package com.sung.vbrowse.mvp.model;

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
    public String thumbPath;
    public String title;
    public VPlayerHelper.PlayerMode type;
    public String netUrl;
}
