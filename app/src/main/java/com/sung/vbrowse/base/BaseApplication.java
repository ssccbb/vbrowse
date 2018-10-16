package com.sung.vbrowse.base;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * @author: sung
 * @date: 2018/10/15
 * @Description:
 */
public class BaseApplication extends Application {
    private SharedPreferences mPreferences;

    private static class Holder {
        private static final BaseApplication INSTANCE = new BaseApplication();
    }

    public static BaseApplication getInstance(){
        return Holder.INSTANCE;
    }

    private BaseApplication() {
    }

    public SharedPreferences getPreferences(){
        if (mPreferences == null){
            mPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        }
        return mPreferences;
    }

}
