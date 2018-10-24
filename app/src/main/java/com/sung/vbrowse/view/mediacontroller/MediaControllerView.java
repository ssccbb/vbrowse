package com.sung.vbrowse.view.mediacontroller;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sung.vbrowse.R;
import com.sung.vbrowse.interfaces.MediaControllerListener;
import com.sung.vbrowse.interfaces.TapTouchEvent;
import com.sung.vbrowse.mvp.model.VideoInfo;
import com.sung.vbrowse.utils.VPlayerUtils;

/**
 * Create by sung at 2018/10/22
 *
 * @Description: 控制界面
 */
public class MediaControllerView extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, TapTouchEvent {
    private MediaControllerListener mediaControllerListener;

    private View back, info, setting, report, share;
    private View play, lock, display;
    private View top, bottom, maskTop, maskBottom;
    private TapView tapView;
    private TextView tittle, time;
    private SeekBar seekBar;
    private View dialog_light, dialog_volume;
    private ProgressBar light_progress, volume_progress;

    private VideoInfo video;

    private boolean IS_PLAY = false;
    private boolean IS_LOCK = false;

    private float currentVolume = 0f;
    private float currentLight = 0f;
    private float maxVolume = 0f;
    private float maxLight = 255f;

    private final int MSG_UI_DISPLAY = 11;
    private final int MSG_UI_HIDE = 12;
    private final int MSG_DIALOG_HIDE = 13;
    private final int MSG_UI_DELAY = 3000;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UI_DISPLAY:
                    if (IS_LOCK) {
                        showLock();
                    } else {
                        showAll();
                    }
                    break;
                case MSG_UI_HIDE:
                    hideAll();
                    break;
                case MSG_DIALOG_HIDE:
                    hideDialog();
                    break;
            }
        }
    };

    public MediaControllerView(@NonNull Context context) {
        super(context);
        init();
    }

    public MediaControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediaControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflateLayout();
        addListener();
        getInfo();
        setUI();
    }

    private void inflateLayout() {
        View child = LayoutInflater.from(this.getContext())
                .inflate(R.layout.view_media_controller, this, false);
        top = child.findViewById(R.id.ll_top_tool);
        bottom = child.findViewById(R.id.ll_bottom_tool);
        maskTop = child.findViewById(R.id.mask_top);
        maskBottom = child.findViewById(R.id.mask_bottom);
        back = child.findViewById(R.id.iv_back);
        info = child.findViewById(R.id.iv_info);
        setting = child.findViewById(R.id.iv_setting);
        report = child.findViewById(R.id.iv_report);
        share = child.findViewById(R.id.iv_share);
        play = child.findViewById(R.id.iv_play);
        lock = child.findViewById(R.id.iv_lock);
        display = child.findViewById(R.id.iv_switch);
        tittle = child.findViewById(R.id.tv_tittle);
        time = child.findViewById(R.id.tv_time);
        seekBar = child.findViewById(R.id.sb_seek);
        tapView = child.findViewById(R.id.v_tap);

        dialog_light = child.findViewById(R.id.light_dialog);
        dialog_volume = child.findViewById(R.id.volume_dialog);
        light_progress = child.findViewById(R.id.light_progress);
        volume_progress = child.findViewById(R.id.volume_progress);
        this.addView(child,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void setUI(){
        if (video == null) return;
        tittle.setText(video.title);
    }

    private void addListener() {
        back.setOnClickListener(this);
        info.setOnClickListener(this);
        setting.setOnClickListener(this);
        report.setOnClickListener(this);
        share.setOnClickListener(this);
        play.setOnClickListener(this);
        lock.setOnClickListener(this);
        display.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        tapView.addTapTouchEvent(this);
    }

    private void getInfo() {
        Context context = this.getContext();
        maxVolume = VPlayerUtils.getMediaMaxVolume(context);
        currentVolume = VPlayerUtils.getCurrentMediaVolume(context);
        currentLight = VPlayerUtils.getScreenBrightness(context);
    }

    public void addVideoInfo(VideoInfo video){
        this.video = video;
    }

    public void addOnMediaContrillerListener(MediaControllerListener mediaControllerListener) {
        this.mediaControllerListener = mediaControllerListener;
    }

    public void play() {
        IS_PLAY = true;
        play.setSelected(!IS_PLAY);
    }

    public void pause() {
        IS_PLAY = false;
        play.setSelected(!IS_PLAY);
    }

    public void stop() {
        IS_PLAY = false;
        play.setSelected(!IS_PLAY);
    }

    public void replay() {
    }

    public void updateProgress(int progress) {
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            //返回
            if (mediaControllerListener != null) {
                mediaControllerListener.onBackUp();
            }
        }
        if (view == info) {
            //详情
        }
        if (view == setting) {
            //设置
        }
        if (view == report) {
            //举报
        }
        if (view == share) {
            //分享
        }
        if (view == play) {
            //播放
            IS_PLAY = !IS_PLAY;
            play.setSelected(!IS_PLAY);
            if (mediaControllerListener != null) {
                mediaControllerListener.onPlayStatusChange(!IS_PLAY);
            }
        }
        if (view == lock) {
            //锁定
            IS_LOCK = !IS_LOCK;
            lock.setSelected(IS_LOCK);
            show();
        }
        if (view == display) {
            //切换横竖屏
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onTouchEnd(boolean isClick, boolean isDoubleClick) {
        hideDialog();
        if (!isClick) return;
        show();
    }

    @Override
    public void onVerticalSlide(boolean isLeft, boolean isSlideUp, float percent) {
        if (IS_LOCK) return;
        if (isLeft && isSlideUp) {
            increaseLight(percent);
        }
        if (isLeft && !isSlideUp) {
            decreaseLight(percent);
        }
        if (!isLeft && isSlideUp) {
            increaseVolume(percent);
        }
        if (!isLeft && !isSlideUp) {
            decreaseVolume(percent);
        }
    }

    @Override
    public void onHorizontalSlide(boolean right2left, float percent) {
        if (IS_LOCK) return;
        if (right2left) {

        }
    }

    /******    亮度/音量     ******/
    private void increaseLight(float percent) {
        if (percent <= 0 || maxLight <= 0 || currentLight >= maxLight) {
            return;
        }

        int increaseValue = (int) (percent * maxLight);
        currentLight = increaseValue + currentLight;
        if (currentLight >= maxLight) {
            currentLight = maxLight;
        }
        if (mediaControllerListener != null) {
            mediaControllerListener.onLightChange(currentLight);
            lightDialogController();
        }
    }

    private void decreaseLight(float percent) {
        if (percent <= 0 || maxLight <= 0 || currentLight <= 0) {
            return;
        }

        int decreaseLight = (int) (percent * maxLight);
        currentLight = currentLight - decreaseLight;
        if (currentLight <= 5) {
            currentLight = 5;
        }
        if (mediaControllerListener != null) {
            mediaControllerListener.onLightChange(currentLight);
            lightDialogController();
        }
    }

    private void lightDialogController() {
        if (dialog_light.getVisibility() == GONE) {
            dialog_light.setVisibility(VISIBLE);
        }
        if (light_progress.getMax() != maxLight) {
            light_progress.setMax((int) maxLight);
        }
        light_progress.setProgress((int) currentLight);
    }

    private void increaseVolume(float percent) {
        if (percent <= 0 || maxVolume <= 0 || currentVolume >= maxVolume) {
            return;
        }

        int increaseVolume = (int) (percent * maxVolume);
        currentVolume = currentVolume + increaseVolume;
        if (currentVolume >= maxVolume) {
            currentVolume = maxVolume;
        }
        VPlayerUtils.setMediaVolume(this.getContext(), (int) currentVolume);
    }

    private void decreaseVolume(float percent) {
        if (percent <= 0 || maxVolume <= 0 || currentVolume <= 0) {
            return;
        }
        int decreaseVolume = (int) (percent * maxVolume);
        currentVolume = currentVolume - decreaseVolume;
        if (currentVolume <= 0) {
            currentVolume = 0;
        }
        VPlayerUtils.setMediaVolume(this.getContext(), (int) currentVolume);
    }

    private void volumeDialogController() {
        if (dialog_volume.getVisibility() == GONE) {
            dialog_volume.setVisibility(VISIBLE);
        }
        if (volume_progress.getMax() != maxVolume) {
            volume_progress.setMax((int) maxVolume);
        }
        volume_progress.setProgress((int) currentVolume);
    }
    /******    亮度/音量     ******/

    /******    界面显隐     ******/
    private void show() {
        //根据锁状态
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(MSG_UI_HIDE, MSG_UI_DELAY);
        if (IS_LOCK) {
            showLock();
        } else {
            showAll();
        }
    }

    /**
     * 显示全部
     */
    private void showAll() {
        top.setVisibility(VISIBLE);
        bottom.setVisibility(VISIBLE);
        lock.setVisibility(VISIBLE);
        maskTop.setVisibility(VISIBLE);
        maskBottom.setVisibility(VISIBLE);
    }

    /**
     * 只显示锁
     */
    private void showLock() {
        top.setVisibility(GONE);
        bottom.setVisibility(GONE);
        maskTop.setVisibility(GONE);
        maskBottom.setVisibility(GONE);
        lock.setVisibility(VISIBLE);
    }

    /**
     * 隐藏所有工具（上部、下部、遮罩、锁）
     */
    private void hideAll() {
        top.setVisibility(GONE);
        bottom.setVisibility(GONE);
        maskTop.setVisibility(GONE);
        maskBottom.setVisibility(GONE);
        lock.setVisibility(GONE);
    }

    /**
     * 隐藏所有控制提示（音量、亮度、快进、快退）
     */
    private void hideDialog() {
        if (dialog_volume.getVisibility() != GONE) {
            dialog_volume.setVisibility(GONE);
        }
        if (dialog_light.getVisibility() != GONE) {
            dialog_light.setVisibility(GONE);
        }
    }

    public void showVolumeDialog(){
        currentVolume = VPlayerUtils.getCurrentMediaVolume(this.getContext());
        Log.e(MediaControllerView.class.getSimpleName(), "showVolumeDialog: "+currentVolume );
        volumeDialogController();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(MSG_DIALOG_HIDE,MSG_UI_DELAY);
    }
    /******    界面显隐     ******/
}
