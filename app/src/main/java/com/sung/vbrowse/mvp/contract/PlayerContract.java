package com.sung.vbrowse.mvp.contract;

import com.sung.vbrowse.interfaces.IPlayerPresenter;
import com.sung.vbrowse.utils.VPlayerHelper;

/**
 * @author: sung
 * @date: 2018/10/18
 * @Description:
 */
public class PlayerContract implements IPlayerPresenter {
    private VPlayerHelper helper;

    public void bindHelper(VPlayerHelper helper){
        this.helper = helper;
    }

    @Override
    public void prepare() {
        helper.prepare();
    }

    @Override
    public void play() {
        helper.play();
    }

    @Override
    public void pause() {
        helper.pause();
    }

    @Override
    public void replay() {
        helper.replay();
    }

    @Override
    public void stop() {
        helper.stop();
    }
}
