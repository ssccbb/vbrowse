package com.sung.vbrowse.view.indecator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.sung.vbrowse.R;
import com.sung.vbrowse.base.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2018/4/24.
 * 首页底部指示器（配合TabIndicator）
 */

public class TabIndicatorView extends LinearLayout implements Animation.AnimationListener {
    public static final String TAG = TabIndicatorView.class.getSimpleName();
    private OnTabIndicatorSelectListener onTabIndicatorSelectListener;
    private List<TabIndicatorModel> models = new ArrayList();
    private List<TabIndicator> indicators = new ArrayList();

    private Animation show;
    private Animation hide;

    private boolean isShow = true;
    private boolean animationing = false;

    public TabIndicatorView(Context context) {
        super(context);
        initView();
    }

    public TabIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TabIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initData() {
        TabIndicatorModel tab1 = new TabIndicatorModel(
                getResources().getString(Constants.INDICATOR_TEXT[0]), R.drawable.ic_index_display_seletor);
        TabIndicatorModel tab2 = new TabIndicatorModel(
                getResources().getString(Constants.INDICATOR_TEXT[1]), R.drawable.ic_index_reccord_selector);
        models.add(tab1);
        models.add(tab2);
    }

    private void initView() {
        initData();
        if (models == null || models.isEmpty()) return;
        this.setGravity(LinearLayout.HORIZONTAL | Gravity.CENTER);
        for (int i = 0; i < models.size(); i++) {
            addIndicator(i);
        }

        this.select(0);
    }

    private void addIndicator(final int position) {
        if (models == null) models = new ArrayList<>();

        TabIndicator indicator = new TabIndicator(this.getContext(), models.get(position));
        indicator.setTag(position);
        indicator.bindParent(this);
        indicators.add(indicator);

        LayoutParams params =
                new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        this.addView(indicator, params);

        indicator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                select(position);
            }
        });
    }

    public void select(int position) {
        for (int i = 0; i < indicators.size(); i++) {
            TabIndicator indicator = indicators.get(i);
            if (i == position) {
                indicator.selected();
                if (onTabIndicatorSelectListener != null) {
                    onTabIndicatorSelectListener.onTabSelect(position);
                }
            } else {
                indicator.unselected();
            }
        }
    }

    public void show() {
        if (isShow || animationing) return;
        if (show == null) {
            show = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_slide_down);
            show.setFillAfter(true);
            show.setAnimationListener(this);
        }
        isShow = true;
        this.startAnimation(show);
    }

    public void hide() {
        if (!isShow || animationing) return;
        if (hide == null) {
            hide = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_slide_up);
            hide.setFillAfter(true);
            hide.setAnimationListener(this);
        }
        isShow = false;
        this.startAnimation(hide);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        animationing = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animationing = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void setShowState(boolean show) {
        isShow = show;
    }

    public boolean isShow() {
        return isShow;
    }

    public interface OnTabIndicatorSelectListener {
        void onTabSelect(int position);
    }

    public void addOnTabIndicatorSelectListener(OnTabIndicatorSelectListener onTabIndicatorSelectListener) {
        this.onTabIndicatorSelectListener = onTabIndicatorSelectListener;
    }

    class TabIndicatorModel {
        public String tag;
        public int res;

        public TabIndicatorModel(String tag, int res) {
            this.tag = tag;
            this.res = res;
        }
    }
}
