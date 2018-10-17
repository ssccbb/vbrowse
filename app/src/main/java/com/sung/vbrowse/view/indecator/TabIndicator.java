package com.sung.vbrowse.view.indecator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sung.vbrowse.R;

/**
 * Created by sung on 2018/4/24.
 */
public class TabIndicator extends LinearLayout {
    private TabIndicatorView parent;
    private TabIndicatorView.TabIndicatorModel indicator;

    private int default_color = R.color.view_indicator_default_color;
    private int selecte_color = R.color.view_indicator_select_color;

    private TextView text;
    private ImageView icon;

    public TabIndicator(Context context, TabIndicatorView.TabIndicatorModel model) {
        super(context);
        this.indicator = model;
        initView();
    }

    public TabIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TabIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.view_tab_indicator, null, false);
        text = view.findViewById(R.id.text);
        icon = view.findViewById(R.id.icon);
        this.addView(view,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setGravity(Gravity.CENTER);
        setData();
    }

    private void setData() {
        if (indicator == null) return;
        text.setText(indicator.tag);
        icon.setImageResource(indicator.res);

//        this.setOnClickListener(this);
    }

    public void bindParent(TabIndicatorView parent) {
        this.parent = parent;
    }

    public void setIndicator(TabIndicatorView.TabIndicatorModel indicator) {
        this.indicator = indicator;
        setData();
    }

    public void selected() {
        text.setTextColor(getResources().getColor(selecte_color));
        icon.setSelected(true);

//        if (null != parent) {
//            parent.select((int) this.getTag());
//        }
    }

    public void unselected() {
        text.setTextColor(getResources().getColor(default_color));
        icon.setSelected(false);
    }

//    @Override
//    public void onClick(View v) {
//        if (indicator == null) return;
//        if (v == this) {
//            selected();
//        }
//    }
}
