package com.sung.vbrowse.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.sung.vbrowse.R;
import com.sung.vbrowse.base.BaseActivity;
import com.sung.vbrowse.evenetbus.BaseEvent;
import com.sung.vbrowse.evenetbus.VolumeEvent;
import com.sung.vbrowse.interfaces.IPlayerView;
import com.sung.vbrowse.interfaces.MediaControllerListener;
import com.sung.vbrowse.mvp.model.VideoInfo;
import com.sung.vbrowse.mvp.presenter.PlayerPresenter;
import com.sung.vbrowse.receiver.VolumeChangeReceiver;
import com.sung.vbrowse.utils.ScreenUtils;
import com.sung.vbrowse.utils.VPlayerHelper;
import com.sung.vbrowse.utils.VPlayerUtils;
import com.sung.vbrowse.view.mediacontroller.MediaControllerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import io.vov.vitamio.Vitamio;

public class PlayerActivity extends BaseActivity implements IPlayerView,MediaControllerListener {
    private VPlayerHelper mHelper;
    private PlayerPresenter mPresenter;
    private AudioManager mAudioManager;
    private VolumeChangeReceiver mReceiver;

    private VideoInfo mVideoInfo;

    @BindView(R.id.surface)
    SurfaceView mPreview;
    @BindView(R.id.progress)
    ContentLoadingProgressBar mProgress;
    @BindView(R.id.mcv_controller)
    MediaControllerView mControllerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_player);
        //播放初始化
        Vitamio.isInitialized(this);
        //暗色虚拟按键
        ScreenUtils.setDarkNavigation(this);
        //event
        EventBus.getDefault().register(this);

        mVideoInfo = (VideoInfo) this.getIntent()
                .getSerializableExtra(PlayerActivity.class.getSimpleName());
        if (mVideoInfo == null)
            return;

        init();
        set();
        registReceiver();
    }

    private void init(){
        mHelper = new VPlayerHelper.Builder(this)
                .addVideoInfo(mVideoInfo)
                .build();
        mPresenter = new PlayerPresenter(this, mHelper);
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mReceiver = new VolumeChangeReceiver();
    }

    private void set(){
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mPresenter.initPlayer();//初始化
        mProgress.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), R.color.ThemeColor),
                PorterDuff.Mode.MULTIPLY);
        mControllerView.addVideoInfo(mVideoInfo);
        mControllerView.addOnMediaContrillerListener(this);
    }

    private void registReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mReceiver,intentFilter);
    }

    /********       mvp        ********/

    @Override
    public void showError(int code) {
        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void play() {
        mControllerView.play();
    }

    @Override
    public void pause() {
        mControllerView.pause();
    }

    @Override
    public void stop() {
        mControllerView.stop();
    }

    @Override
    public void replay() {
        mControllerView.replay();
    }

    @Override
    public void updateProgress(int progress) {
        mControllerView.updateProgress(progress);
    }

    @Override
    public SurfaceView getPlayerView() {
        return mPreview;
    }

    @Override
    public VideoInfo getVideoInfo() {
        return mVideoInfo;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /********       mvp        ********/

    /********       controller callback        ********/

    @Override
    public void onBackUp() {
        mHelper.stop();
        this.finish();
    }

    @Override
    public void onPlayStatusChange(boolean pause) {
        mHelper.pause();
    }

    @Override
    public void onDisplayChange(boolean isLanscape) {

    }

    @Override
    public void onLightChange(float light) {
        //改变亮度只能拿activity不能用context所以为了view不冗余不在mediacontrollerview中更改
        VPlayerUtils.setScreenBrightness(this,light);
    }

    /********       controller callback        ********/

    /*******      override method      ********/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //屏蔽音量变更弹窗
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
            return true;
        }else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceveEvent(VolumeEvent event){
        mControllerView.showVolumeDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.onDetroy();
        unregisterReceiver(mReceiver);
        EventBus.getDefault().unregister(this);
    }

    /*******      override method      ********/

    public static void open(Context context, VideoInfo video) {
        if (context == null || video == null) {
            Log.e(PlayerActivity.class.getSimpleName(), "open exception " +
                    "--> context empty or video empty,please check intent data!");
            return;
        }

        Intent goTo = new Intent(context, PlayerActivity.class);
        video.thumb = null;
        goTo.putExtra(PlayerActivity.class.getSimpleName(), video);
        context.startActivity(goTo);
    }
}
