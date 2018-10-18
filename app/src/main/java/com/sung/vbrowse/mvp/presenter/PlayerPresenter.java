package com.sung.vbrowse.mvp.presenter;

import android.view.SurfaceView;

import com.sung.vbrowse.interfaces.IPlayerPresenter;
import com.sung.vbrowse.interfaces.IPlayerView;
import com.sung.vbrowse.mvp.contract.PlayerContract;
import com.sung.vbrowse.utils.VPlayerHelper;

/**
 * @author: sung
 * @date: 2018/10/18
 * @Description:
 */
public class PlayerPresenter {
    private PlayerContract mPlayerContract;

    private PlayerListener playerListener;

    public PlayerPresenter(IPlayerView mPlayerView, VPlayerHelper helper) {
        this.mPlayerContract = new PlayerContract();
        helper.bindPlayer(mPlayerView);
        this.mPlayerContract.bindHelper(helper);
    }

    public void initPlayer(){
        mPlayerContract.prepare();
    }

    public void play(){
        mPlayerContract.play();
    }

    public void pause(){
        mPlayerContract.pause();
    }

    public void stop(){
        mPlayerContract.stop();
    }

    public void replay(){
        mPlayerContract.replay();
    }

    public interface PlayerListener {
        void onPrepareCompelet();
    }

    public void addPlayerListener(PlayerListener playerListener){
        this.playerListener = playerListener;
    }
}
