package com.sung.vbrowse.interfaces;

/**
 * Create by sung at 2018/10/22
 *
 * @Description:
 */
public interface TapTouchEvent {
    void onTouchEnd(boolean isClick);
    void onTouchMoving(boolean isLeft,boolean slideUp,float percent);
}
