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
import android.widget.SeekBar;
import android.widget.TextView;

import com.sung.vbrowse.R;
import com.sung.vbrowse.interfaces.MediaControllerListener;
import com.sung.vbrowse.interfaces.TapTouchEvent;
import com.sung.vbrowse.utils.VPlayerUtils;

/**
 * Create by sung at 2018/10/22
 *
 * @Description: 控制界面
 */
public class MediaControllerView extends FrameLayout implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,TapTouchEvent {
    private MediaControllerListener mediaControllerListener;

    private View back,info,setting,report,share;
    private View play,lock,display;
    private TapView tapView;
    private TextView tittle,time;
    private SeekBar seekBar;

    private boolean IS_PLAY = false;
    private boolean IS_LOCK = false;

    private float currentVolume = 0f;
    private float currentLight = 0f;
    private float maxVolume = 0f;
    private float maxLight = 255f;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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

    private void init(){
        inflateLayout();
        addListener();
        getInfo();
    }

    private void inflateLayout(){
        View child = LayoutInflater.from(this.getContext())
                .inflate(R.layout.view_media_controller,this,false);
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
        this.addView(child,
                new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
    }

    private void addListener(){
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

    private void getInfo(){
        Context context = this.getContext();
        maxVolume = VPlayerUtils.getMediaMaxVolume(context);
        currentVolume = VPlayerUtils.getCurrentMediaVolume(context);
        currentLight = VPlayerUtils.getScreenBrightness(context);
    }

    public void addOnMediaContrillerListener(MediaControllerListener mediaControllerListener){
        this.mediaControllerListener = mediaControllerListener;
    }

    private void switchLockStatus(){
        if (IS_LOCK){
            //锁一直显示 其他的一律隐藏
        }else {
            //正常显示隐藏
        }
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
        if (view == back){
            //返回
            if (mediaControllerListener != null){
                mediaControllerListener.onBackUp();
            }
        }
        if (view == info){
            //详情
        }
        if (view == setting){
            //设置
        }
        if (view == report){
            //举报
        }
        if (view == share){
            //分享
        }
        if (view == play){
            //播放
            IS_PLAY = !IS_PLAY;
            play.setSelected(!IS_PLAY);
            if (mediaControllerListener != null){
                mediaControllerListener.onPlayStatusChange(!IS_PLAY);
            }
        }
        if (view == lock){
            //锁定
            IS_LOCK = !IS_LOCK;
            lock.setSelected(IS_LOCK);
            switchLockStatus();
        }
        if (view == display){
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
    public void onTouchEnd(boolean isClick) {
        Log.e(MediaControllerView.class.getSimpleName(), isClick ? "click" : "move" );
    }

    @Override
    public void onTouchMoving(boolean isLeft, boolean isSlideUp, float percent) {
        if (isLeft && isSlideUp){
            Log.e(MediaControllerView.class.getSimpleName(), "亮度增加 "+percent );
            increaseLight(percent);
        }
        if (isLeft && !isSlideUp){
            Log.e(MediaControllerView.class.getSimpleName(), "亮度减少 "+percent );
            decreaseLight(percent);
        }
        if (!isLeft && isSlideUp){
            Log.e(MediaControllerView.class.getSimpleName(), "音量增加 "+percent );
        }
        if (!isLeft && !isSlideUp){
            Log.e(MediaControllerView.class.getSimpleName(), "音量减少 "+percent );
        }
    }

    private void increaseLight(float percent){
        if (percent <= 0 || maxLight <= 0 || currentLight >= maxLight){
            return;
        }

        int increaseValue = (int) (percent * maxLight);
        currentLight = increaseValue + currentLight;
        if (currentLight >= maxLight){
            currentLight = maxLight;
        }
        if (mediaControllerListener != null){
            mediaControllerListener.onLightChange(currentLight);
        }
    }

    private void decreaseLight(float percent){
        if (percent <= 0 || maxLight <= 0 || currentLight <= 0){
            return;
        }

        int decreaseLight = (int) (percent * maxLight);
        currentLight = currentLight - decreaseLight;
        if (currentLight <= 5){
            currentLight = 5;
        }
        if (mediaControllerListener != null){
            mediaControllerListener.onLightChange(currentLight);
        }
    }

    private void increaseVolume(float percent){
        if (percent <= 0 || maxVolume <= 0 || currentVolume >= maxVolume){
            return;
        }
    }

    private void decreaseVolume(float percent){
        if (percent <= 0 || maxVolume <= 0 || currentVolume <= 0){
            return;
        }
    }
}
