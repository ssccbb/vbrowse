package com.sung.vbrowse.interfaces;

import com.sung.vbrowse.utils.VPlayerHelper;

/**
 * @author: sung
 * @date: 2018/10/18
 * @Description:
 */
public interface IPlayerPresenter {
    void prepare();
    
    void play();

    void pause();

    void replay();

    void stop();
}
