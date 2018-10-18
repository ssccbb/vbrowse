package com.sung.vbrowse.utils;


import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import com.sung.vbrowse.base.Error;
import com.sung.vbrowse.interfaces.IPlayerView;
import com.sung.vbrowse.mvp.model.VideoInfo;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.CenterLayout;

/**
 * @author: sung
 * @date: 2018/10/18
 *
 * 因为需要绑定IPlayerView，
 * 不绑定拿不到surfaceview一律retrun
 * 正常使用请实现IPlayerView接口
 */
public class VPlayerHelper {
    private static final String TAG = VPlayerHelper.class.getSimpleName();
    private IPlayerView playerView;

    private VideoInfo videoInfo;
    private MediaPlayer mediaPlayer;

    private boolean is_video_size_known = false;
    private boolean is_video_ready_to_play = false;
    private int video_width = 0,video_height = 0;

    private static class Holder {
        private static VPlayerHelper INSTANCE = new VPlayerHelper();
    }

    private VPlayerHelper(){
    }

    private static VPlayerHelper getInstance(){
        return Holder.INSTANCE;
    }

    /**
     * 必须绑定
     * */
    public void bindPlayer(IPlayerView view){
        if (view == null) return;
        this.playerView = view;
        if (videoInfo == null) {//防止数据错误
            this.videoInfo = view.getVideoInfo();
        }
    }

    /**
     * 准备
     * */
    public void prepare(){
        final SurfaceHolder holder = playerView.getPlayerView().getHolder();
        final String path = videoInfo.type == VPlayerHelper.PlayerMode.LOCAL
                ? videoInfo.filePath : videoInfo.netUrl;
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surface created! " );
                load(path,holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG, "surface changed! " );
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surface destroyed! " );
            }
        });
        holder.setFormat(PixelFormat.RGBA_8888);
        load(path,holder);
    }

    private void load(String path,SurfaceHolder holder){
        if (StringUtils.isEmpty(path)) {
            //数据错误
            playerView.showError(Error.CODE_1001);
            return;
        }

        if (mediaPlayer != null ){
            playerView.showError(Error.CODE_1003);
            return;
        }
        if (mediaPlayer.isPlaying()) {
            playerView.showError(Error.CODE_1004);
            return;
        }

        doCleanUp();
        releaseMediaPlayer();

        //数据加载中展示加载条
        playerView.showLoading();

        try {
            // Create a new media player and set the listeners
            mediaPlayer = new MediaPlayer(playerView.getContext());
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(holder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    //网络视频缓冲
                    if (percent >= 100) {
                        playerView.dismissLoading();
                    }else {
                        playerView.showLoading();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG, "play source complet!");
                    playerView.replay();
                    replay();
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "play source prepare done!");
                    playerView.dismissLoading();
                    playerView.play();
                    is_video_ready_to_play = true;
                    if (is_video_size_known && is_video_ready_to_play){
                        play();
                    }
                }
            });
            mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    Log.d(TAG, "player video size changed!");
                    if (width == 0 || height == 0) {
                        Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
                        return;
                    }
                    is_video_size_known = true;
                    changeVideoSize();
                    if (is_video_size_known && is_video_ready_to_play){
                        play();
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "play source error --> what="+what+" & extra="+extra+"!");
                    playerView.showError(Error.CODE_1002);
                    return true;
                }
            });
        } catch (Exception e) {
            playerView.showError(Error.CODE_1005);
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    /**
     * 播放
     * */
    public void play(){
        mediaPlayer.start();
        playerView.play();
    }

    /**
     * 暂停
     * */
    public void pause(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            playerView.pause();
        }else {
            mediaPlayer.start();
            playerView.play();
        }
    }

    /**
     * 停止
     * */
    public void stop(){
        doCleanUp();
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        mediaPlayer.seekTo(0);
        playerView.stop();
    }

    /**
     * 重新播放
     * */
    public void replay(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
        playerView.replay();
    }

    /**
     * 生命周期
     * */
    public void onStart(){
    }

    public void onResume(){
    }

    public void onPause(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    public void onDetroy(){
        doCleanUp();
        releaseMediaPlayer();
        videoInfo = null;
    }

    /**
     * 适配
     * */
    public void changeVideoSize() {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        DisplayMetrics dm = new DisplayMetrics();
        playerView.getContext().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int surfaceWidth = dm.widthPixels;
        int surfaceHeight = dm.heightPixels;
        float scaleVideo = (float) videoHeight / (float) videoWidth;
        float scaleSurface = (float) surfaceHeight / (float) surfaceWidth;

        if (scaleVideo > scaleSurface){
            float f = (float) videoWidth / (float) surfaceWidth;
            video_height = (int) (surfaceHeight / f);
            video_width = surfaceWidth;
        }
        if (scaleVideo <= scaleSurface) {
            video_width = (int) (surfaceHeight / scaleVideo);
            video_height = surfaceHeight;
        }
        playerView.getPlayerView().setLayoutParams(new CenterLayout.LayoutParams(video_width, video_height,0,0));
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void doCleanUp() {
        video_width = 0;
        video_height = 0;
        is_video_ready_to_play = false;
        is_video_size_known = false;
    }

    /**
     * enum类型
     * */
    public enum PlayerMode{
        LOCAL,NET
    }

    /**
     * builder
     * */
    public static class Builder {
        private Context context;
        private VideoInfo videoInfo;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder addVideoInfo(VideoInfo videoInfo){
            this.videoInfo = videoInfo;
            return this;
        }

        public VPlayerHelper build(){
            VPlayerHelper helper = getInstance();
            helper.videoInfo = this.videoInfo;
            return helper;
        }
    }
}
