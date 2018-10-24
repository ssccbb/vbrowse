package com.sung.vbrowse.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sung.vbrowse.evenetbus.VolumeEvent;
import com.sung.vbrowse.utils.VPlayerUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Create by sung at 2018/10/24
 *
 * @Description: 音量检测
 */
public class VolumeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
            Log.d(VolumeChangeReceiver.class.getSimpleName(),
                    "onReceive: current-->"+VPlayerUtils.getCurrentMediaVolume(context)
                            +"/max-->"+VPlayerUtils.getMediaMaxVolume(context) );
            EventBus.getDefault().post(new VolumeEvent());
        }
    }
}
