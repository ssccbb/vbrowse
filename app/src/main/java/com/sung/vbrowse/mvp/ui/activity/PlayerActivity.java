package com.sung.vbrowse.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.sung.vbrowse.R;
import com.sung.vbrowse.base.BaseActivity;
import com.sung.vbrowse.interfaces.IPlayerView;
import com.sung.vbrowse.interfaces.MediaControllerListener;
import com.sung.vbrowse.mvp.model.VideoInfo;
import com.sung.vbrowse.mvp.presenter.PlayerPresenter;
import com.sung.vbrowse.utils.ScreenUtils;
import com.sung.vbrowse.utils.VPlayerHelper;
import com.sung.vbrowse.view.mediacontroller.MediaControllerView;

import butterknife.BindView;
import io.vov.vitamio.Vitamio;

public class PlayerActivity extends BaseActivity implements IPlayerView,MediaControllerListener {
    private VPlayerHelper mHelper;
    private PlayerPresenter mPresenter;

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
        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_player);
        Vitamio.isInitialized(this);

        mVideoInfo = (VideoInfo) this.getIntent()
                .getSerializableExtra(PlayerActivity.class.getSimpleName());
        if (mVideoInfo == null)
            return;

        mHelper = new VPlayerHelper.Builder(this)
                .addVideoInfo(mVideoInfo)
                .build();
        mPresenter = new PlayerPresenter(this, mHelper);
        //音频模式
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //初始化
        mPresenter.initPlayer();
        mProgress.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), R.color.ThemeColor),
                PorterDuff.Mode.MULTIPLY);
        mControllerView.addOnMediaContrillerListener(this);
    }

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

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.onDetroy();
    }

    public static void open(Context context, VideoInfo video) {
        if (context == null || video == null) {
            Log.e(PlayerActivity.class.getSimpleName(), "open exception " +
                    "--> context empty or video empty,please check intent data!");
            return;
        }

        Intent goTo = new Intent(context, PlayerActivity.class);
        goTo.putExtra(PlayerActivity.class.getSimpleName(), video);
        context.startActivity(goTo);
    }

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
}
