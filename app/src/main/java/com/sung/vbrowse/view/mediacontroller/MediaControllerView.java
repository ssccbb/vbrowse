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
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sung.vbrowse.R;
import com.sung.vbrowse.interfaces.MediaControllerListener;
import com.sung.vbrowse.mvp.model.VideoInfo;
import com.sung.vbrowse.utils.VPlayerUtils;

/**
 * Create by sung at 2018/10/22
 *
 * @Description: 控制界面
 */
public class MediaControllerView extends FrameLayout implements View.OnClickListener, MediaGestureBoard.VideoGestureListener, SeekBar.OnSeekBarChangeListener {
    private MediaControllerListener mediaControllerListener;

    private View back, info, setting, report, share;
    private View play, lock, display;
    private View top, bottom, maskTop, maskBottom;
    private TextView tittle, time;
    private SeekBar seekBar;
    private MediaGestureBoard gesture;
    private View dialog_light, dialog_volume;
    private ProgressBar light_progress, volume_progress;

    private VideoInfo video;

    private boolean IS_PLAY = false;
    private boolean IS_LOCK = false;

    private float currentProgress = 0f;
    private float currentVolume = 0f;
    private float currentLight = 0f;
    private float maxVolume = 0f;

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
        gesture = child.findViewById(R.id.mgb_gesture);

        dialog_light = child.findViewById(R.id.light_dialog);
        dialog_volume = child.findViewById(R.id.volume_dialog);
        light_progress = child.findViewById(R.id.light_progress);
        volume_progress = child.findViewById(R.id.volume_progress);
        this.addView(child,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void setUI() {
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
        gesture.setVideoGestureListener(this);
    }

    private void getInfo() {
        Context context = this.getContext();
        maxVolume = VPlayerUtils.getMediaMaxVolume(context);
    }

    public void addVideoInfo(VideoInfo video) {
        this.video = video;
        setUI();
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBrightnessGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float newBrightness = (e1.getY() - e2.getY()) / MediaControllerView.this.getHeight();
        newBrightness += currentLight;
        if (newBrightness < 0) {
            newBrightness = 0;
        } else if (newBrightness > 1) {
            newBrightness = 1;
        }
        if (mediaControllerListener != null) {
            mediaControllerListener.onLightChange(newBrightness);
            Log.d(MediaGestureBoard.class.getSimpleName(), "onLightGesture: " + newBrightness);
            setLight(newBrightness);
        }
    }

    @Override
    public void onVolumeGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float value = (e1.getY() - e2.getY()) / MediaControllerView.this.getHeight();
        int newVolume = (int) (value * maxVolume);
        newVolume += currentVolume;
        if (newVolume < 0) {
            newVolume = 0;
        } else if (newVolume > maxVolume) {
            newVolume = (int) maxVolume;
        }
        VPlayerUtils.setMediaVolume(MediaControllerView.this.getContext(), newVolume);
        Log.d(MediaGestureBoard.class.getSimpleName(), "onVolumeGesture: " + newVolume);
        setVolume(newVolume);
    }

    @Override
    public void onFF_REWGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onSingleTapGesture(MotionEvent e) {

    }

    @Override
    public void onDoubleTapGesture(MotionEvent e) {

    }

    @Override
    public void onDown(MotionEvent e) {
        updateValue();
    }

    @Override
    public void onEndFF_REW(MotionEvent e) {

    }

    @Override
    public void onUp() {
        hideDialog();
    }

    private void updateValue() {
        Context context = MediaControllerView.this.getContext();
        currentVolume = VPlayerUtils.getCurrentMediaVolume(context);
        currentLight = VPlayerUtils.getScreenBrightness(context) / 255f;
//            currentProgress =
    }

    private void setLight(float light) {
        if (dialog_light.getVisibility() == GONE) {
            dialog_light.setVisibility(VISIBLE);
        }
        if (light_progress.getMax() != 100) {
            light_progress.setMax(100);
        }
        light_progress.setProgress((int) (light * 100));
    }

    private void setVolume(float volume) {
        if (dialog_volume.getVisibility() == GONE) {
            dialog_volume.setVisibility(VISIBLE);
        }
        if (volume_progress.getMax() != 100) {
            volume_progress.setMax(100);
        }
        int volumeProgress = (int) (volume / Float.valueOf(maxVolume) * 100);
        volume_progress.setProgress(volumeProgress);
    }

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

    private void showAll() {
        top.setVisibility(VISIBLE);
        bottom.setVisibility(VISIBLE);
        lock.setVisibility(VISIBLE);
        maskTop.setVisibility(VISIBLE);
        maskBottom.setVisibility(VISIBLE);
    }

    private void showLock() {
        top.setVisibility(GONE);
        bottom.setVisibility(GONE);
        maskTop.setVisibility(GONE);
        maskBottom.setVisibility(GONE);
        lock.setVisibility(VISIBLE);
    }

    private void hideAll() {
        top.setVisibility(GONE);
        bottom.setVisibility(GONE);
        maskTop.setVisibility(GONE);
        maskBottom.setVisibility(GONE);
        lock.setVisibility(GONE);
    }

    private void hideDialog() {
        if (dialog_volume.getVisibility() != GONE) {
            dialog_volume.setVisibility(GONE);
        }
        if (dialog_light.getVisibility() != GONE) {
            dialog_light.setVisibility(GONE);
        }
    }

    public void showVolumeDialog() {
        currentVolume = VPlayerUtils.getCurrentMediaVolume(this.getContext());
        setVolume((int) currentVolume);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(MSG_DIALOG_HIDE, MSG_UI_DELAY);
    }
}
