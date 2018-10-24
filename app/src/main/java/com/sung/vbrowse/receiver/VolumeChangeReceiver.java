package com.sung.vbrowse.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sung.vbrowse.evenetbus.VolumeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Create by sung at 2018/10/24
 *
 * @Description:
 */
public class VolumeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
            EventBus.getDefault().post(new VolumeEvent());
        }
    }
}
