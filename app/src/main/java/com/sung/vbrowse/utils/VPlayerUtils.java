package com.sung.vbrowse.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import io.vov.vitamio.MediaMetadataRetriever;

/**
 * Create by sung at 2018/10/22
 *
 * @Description: 辅助工具类
 */
public class VPlayerUtils {

    private static final int FZ_KB = 1024;
    private static final int FZ_MB = 1024 * FZ_KB;
    private static final int FZ_GB = 1024 * FZ_MB;
    private static final int FZ_PB = 1024 * FZ_GB;

    public static String formatTime(long time) {
        String hs, ms, ss, formatTime;
        long h, m, s;
        h = time / 3600;
        m = (time % 3600) / 60;
        s = (time % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        if (hs.equals("00")) {
            formatTime = ms + ":" + ss;
        } else {
            formatTime = hs + ":" + ms + ":" + ss;
        }

        return formatTime;
    }

    public static String formatSpeed(double deltaSize, double deltaMillis) {
        double speed = deltaSize * 1000 / deltaMillis / FZ_KB;
        String result = String.format(Locale.getDefault(), "%.2f KB/s", speed);
        if ((int) speed > FZ_KB) {
            result = String.format(Locale.getDefault(), "%.2f MB/s", speed
                    / FZ_KB);
        }
        return result;
    }

    public static String formatSize(long fileLength) {
        StringBuilder sb = new StringBuilder();
        if (fileLength < FZ_KB) {
            sb.append(formatDouble(fileLength, 1)).append(" B");
        } else if (fileLength <= FZ_MB) {
            sb.append(formatDouble(fileLength, FZ_KB)).append(" KB");
        } else if (fileLength <= FZ_GB) {
            sb.append(formatDouble(fileLength, FZ_MB)).append(" MB");
        } else if (fileLength <= FZ_PB) {
            sb.append(formatDouble(fileLength, FZ_GB)).append(" GB");
        } else {
            sb.append(formatDouble(fileLength, FZ_PB)).append(" PB");
        }
        return sb.toString();
    }

    public static String formatDouble(long value, int divider) {
        double result = value * 1.0 / divider;
        return String.format(Locale.getDefault(), "%.2f", result);
    }

    public static String getVideoThumb(Context context, String videoPath, long coverTime) {
        String strCoverFilePath = null;
        try {
            File videoFile = new File(videoPath);
            if (!videoFile.exists()) {
                Log.w("Utils", "record: video file is not exists when record finish");
                return null;
            }
            MediaMetadataRetriever media = new MediaMetadataRetriever(context);
            media.setDataSource(videoPath);
            Bitmap thumb = media.getFrameAtTime(coverTime);

            String fileName = "";
            int index = videoPath.lastIndexOf(".");
            if (index != -1) {
                fileName = videoPath.substring(0, index);
            }

            strCoverFilePath = fileName + ".jpg";
            File f = new File(strCoverFilePath);
            if (f.exists()) f.delete();
            FileOutputStream fOut = new FileOutputStream(f);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strCoverFilePath;
    }

    public static int getScreenBrightness(Context activity) {
        int value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
        }
        return value;
    }

    public static void setScreenBrightness(Activity activity, int value) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = value / 255f;
        activity.getWindow().setAttributes(params);
    }

    public static int getMediaMaxVolume(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
    }

    public static int getCurrentMediaVolume(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
    }

    public static void setMediaVolume(Context context, int volume){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, //音量类型
                volume, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
    }
}
