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

    private int maxX = 0;
    private int maxY = 0;
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
        if (maxX == 0){
            maxX = this.getRight();
        }
        if (maxY == 0){
            maxY = this.getBottom();
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                //Log.e(TapView.class.getSimpleName(), "onTouchEvent: down");
                //Log.e(TapView.class.getSimpleName(), "onTouchEvent: lastx="+lastX + "/lasty=" +lastY);
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getRawX();
                int upY = (int) event.getRawY();
                if (this.event != null) this.event.onTouchEnd(
                        Math.abs(upX - lastX) <= 12 && Math.abs(upY - lastY) <= 12,false);
                //Log.e(TapView.class.getSimpleName(), "onTouchEvent: up");
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getRawX();
                int currentY = (int) event.getRawY();
                int moveX = currentX - lastX;
                int moveY = currentY - lastY;
                //Log.e(TapView.class.getSimpleName(), "onTouchEvent: moveX="+moveX + "/moveY=" +moveY);

                //屏幕最大x坐标的一半，即中间位置，小数左边大数右边
                boolean isLeftAreaTouch = currentX <= maxX / 2;
                //200表示手指在x轴方向上的偏移量，此处设大点但是不宜过大（个人感觉）
                boolean isSlideUp = moveY < -10 && Math.abs(moveX) <= 200;
                //手指移动距离相对于总高度的百分比
                float percent = (float)Math.abs(moveY) / (float)Math.abs(maxY);

                if (this.event != null && Math.abs(moveY) > 15 && percent > 0.1){
                    this.event.onVerticalSlide(isLeftAreaTouch,isSlideUp,percent);
                }
                break;
        }
        return true;
    }
}
