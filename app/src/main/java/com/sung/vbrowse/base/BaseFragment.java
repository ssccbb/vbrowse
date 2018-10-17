package com.sung.vbrowse.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author: sung
 * @date: 2018/10/15
 * @Description: fragment基类
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    protected Bundle mBundleData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutId() != -1 ? inflater.inflate(getLayoutId(), container, false)
                : super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null){
            mBundleData = getArguments();
        }
        init();
    }

    protected int getLayoutId() {
        return -1;
    }

    protected void init(){

    }

    protected SharedPreferences getPreferences() {
        return BaseApplication.getInstance().getPreferences();
    }

    protected FragmentManager getSupportFragmentManager() {
        return getActivity() != null ? getActivity().getSupportFragmentManager() : null;
    }

    public void onFragmentBackPressed() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
//            EventBus.getDefault().register(getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
//            EventBus.getDefault().unregister(getActivity());
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
