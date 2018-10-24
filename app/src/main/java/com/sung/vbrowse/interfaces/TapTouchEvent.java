package com.sung.vbrowse.interfaces;

/**
 * Create by sung at 2018/10/22
 *
 * @Description:
 */
public interface TapTouchEvent {
    void onTouchEnd(boolean isClick,boolean isDoubleClick);
    void onVerticalSlide(boolean isLeft,boolean slideUp,float percent);
    void onHorizontalSlide(boolean right2left,float percent);
}
