package com.sung.vbrowse;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

/**
 * @author: sung
 * @date: 2018/10/15
 * @Description: application
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;
    private SharedPreferences mPreferences;
    private final String PHOTO_FRESCO = "/cache/fresco";
    private final int CACHE_SIZE = 30;

    public static BaseApplication getInstance(){
        if (instance == null){
            instance = new BaseApplication();
        }
        return instance;
    }

    public BaseApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FrescoInit(getApplicationContext(),CACHE_SIZE);
    }

    /**
     * 初始化操作，建议在子线程中进行
     * @param context
     * @param cacheSizeInM  磁盘缓存的大小，以M为单位
     */
    private void FrescoInit(final Context context, int cacheSizeInM){
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setMaxCacheSize(cacheSizeInM*1024*1024)//最大缓存
                .setBaseDirectoryName(PHOTO_FRESCO)//子目录
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        //还是推荐缓存到应用本身的缓存文件夹,这样卸载时能自动清除,其他清理软件也能扫描出来
                        return context.getCacheDir();
                    }
                })
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setMainDiskCacheConfig(diskCacheConfig)
                //.setImageCacheStatsTracker(imageCacheStatsTracker)
                .setDownsampleEnabled(true)
                //Downsampling，要不要向下采样,它处理图片的速度比常规的裁剪scaling更快，
                // 并且同时支持PNG，JPG以及WEP格式的图片，非常强大,与ResizeOptions配合使用
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                //如果不是重量级图片应用,就用这个省点内存吧.默认是RGB_888
                .build();
        Fresco.initialize(context, config);
    }

    public SharedPreferences getPreferences(){
        if (mPreferences == null){
            mPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        }
        return mPreferences;
    }

}
