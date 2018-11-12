package com.sung.vbrowse.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sung.vbrowse.R;

/**
 * Create by sung at 2018/10/23
 *
 * @Description:
 */
public class ContentLayout extends RelativeLayout {
    private View view_empty,view_error,view_loading;
    private TextView empty_hint,error_hint;

    public ContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context,attrs);
    }

    public ContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context,attrs);
    }

    private void getAttrs(Context context, AttributeSet attrs){
        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.ContentLayout);
        int view_empty = attributes.getResourceId(R.styleable.ContentLayout_layout_empty, R.layout.view_empty);
        int view_error = attributes.getResourceId(R.styleable.ContentLayout_layout_error, R.layout.view_error);
        int view_loading = attributes.getResourceId(R.styleable.ContentLayout_layout_loading, R.layout.view_loading);
        LayoutInflater inflater = LayoutInflater.from(context);
        this.view_empty = inflater.inflate(view_empty,this,false);
        this.view_error = inflater.inflate(view_error,this,false);
        this.view_loading = inflater.inflate(view_loading,this,false);
        LayoutParams params = new LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(this.view_empty,params);
        this.addView(this.view_error,params);
        this.addView(this.view_loading,params);
        attributes.recycle();
        showContent();
        init();
    }

    private void init(){
        if (view_empty == null ||
                view_error == null || view_loading == null)
            return;
        empty_hint = view_empty.findViewById(R.id.hint);
        error_hint = view_error.findViewById(R.id.hint);
    }

    public void showEmpty(){
        view_error.setVisibility(GONE);
        view_loading.setVisibility(GONE);
        view_empty.setVisibility(VISIBLE);
    }

    public void showError(){
        view_error.setVisibility(VISIBLE);
        view_loading.setVisibility(GONE);
        view_empty.setVisibility(GONE);
    }

    public void showLoading(){
        view_error.setVisibility(GONE);
        view_loading.setVisibility(VISIBLE);
        view_empty.setVisibility(GONE);
    }

    public void showContent(){
        view_error.setVisibility(GONE);
        view_loading.setVisibility(GONE);
        view_empty.setVisibility(GONE);
    }
}
