package com.sung.vbrowse.utils;


import android.content.Context;
import android.graphics.PixelFormat;
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
    private Context context;
    private IPlayerView playerView;

    private VideoInfo videoInfo;
    private MediaPlayer mediaPlayer;

    private boolean is_video_size_known = false;
    private boolean is_video_ready_to_play = false;
    private int video_width = 0,video_height = 0;
    private int surfaceWidth = 0,surfaceHeight = 0;

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
                if (ScreenUtils.isPortrait(context)){
                    surfaceWidth = playerView.getPlayerView().getWidth();
                    surfaceHeight = playerView.getPlayerView().getHeight();
                }else {
                    surfaceHeight = playerView.getPlayerView().getWidth();
                    surfaceWidth = playerView.getPlayerView().getHeight();
                }
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

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            playerView.showError(Error.CODE_1004);
            return;
        }

        doCleanUp();
        releaseMediaPlayer();

        //数据加载中展示加载条
        playerView.showLoading();

        try {
            // Create a new media player and set the listeners
            mediaPlayer = new MediaPlayer(context);
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(holder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    //网络视频缓冲
                    if (videoInfo.type == PlayerMode.LOCAL || percent >= 100) {
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
                    playerView.pause();
                    pause();
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "play source prepare done!");
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
                    playerView.dismissLoading();
                    playerView.showError(Error.CODE_1002);
                    return true;
                }
            });
        } catch (Exception e) {
            playerView.dismissLoading();
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
        playerView.dismissLoading();
    }

    /**
     * 暂停
     * */
    public void pause(){
        playerView.dismissLoading();
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
        playerView.dismissLoading();
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
        playerView.dismissLoading();
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
        if (video_width != 0 && video_height != 0) return;
        video_width = mediaPlayer.getVideoWidth();
        video_height = mediaPlayer.getVideoHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (ScreenUtils.isPortrait(context)) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) video_width / (float) surfaceWidth,(float) video_height / (float) surfaceHeight);
        } else{
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) video_width/(float) surfaceHeight),(float) video_height/(float) surfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        video_width = (int) Math.ceil((float) video_width / max);
        video_height = (int) Math.ceil((float) video_height / max);

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
            helper.context = this.context;
            return helper;
        }
    }
}
