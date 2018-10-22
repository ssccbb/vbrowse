package com.sung.vbrowse.view.mediacontroller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sung.vbrowse.interfaces.TapTouchEvent;

/**
 * Create by sung at 2018/10/22
 *
 * @Description: 控制器内触摸反馈的view
 */
public class TapView extends View {
    private TapTouchEvent event;

    private int lastX = 0;
    private int lastY = 0;

    public TapView(Context context) {
        super(context);
    }

    public TapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addTapTouchEvent(TapTouchEvent event){
        this.event = event;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                Log.e(TapView.class.getSimpleName(), "onTouchEvent: down");
                Log.e(TapView.class.getSimpleName(), "onTouchEvent: lastx="+lastX + "/lasty=" +lastY);
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getRawX();
                int upY = (int) event.getRawY();
                if (this.event != null) this.event.onTouchEnd(Math.abs(upX) <= 12 && Math.abs(upY) <= 12);
                Log.e(TapView.class.getSimpleName(), "onTouchEvent: up");
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) (event.getRawX() - lastX);
                int moveY = (int) (event.getRawY() - lastY);
                Log.e(TapView.class.getSimpleName(), "onTouchEvent: moveX="+moveX + "/moveY=" +moveY);
                break;
        }
        return true;
    }
}
