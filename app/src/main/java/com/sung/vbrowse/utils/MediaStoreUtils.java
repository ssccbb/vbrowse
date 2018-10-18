package com.sung.vbrowse.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import com.sung.vbrowse.mvp.model.VideoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据MediaStore查询信息
 *
 * @author chenlin
 * @version 1.0
 * @Project App_ReadCard
 * @Package com.android.ocr.util
 * @Date 2013年6月16日
 * @Note TODO
 */
public class MediaStoreUtils {

    private static final String TAG = "MediaStoreUtil";

    /**
     * 查询音频文件名称
     *
     * @param context
     * @return
     */
    public static List<String> getAudioNames(Context context) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA}, null, new String[]{}, null);
        while (cursor.moveToNext()) {
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            list.add(fileName);
        }
        return list;
    }

    /**
     * 查询图片文件名称
     *
     * @param context
     * @return
     */
    public static List<String> getImageNames(Context context) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA}, null,
                new String[]{}, null);
        while (cursor.moveToNext()) {
            Log.i(TAG, "filePath==" + MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            list.add(filePath + "/" + fileName);
        }
        return list;
    }

    /**
     * 查询图片文件
     *
     * @param context
     * @return
     */
    public static List<File> getImages(Context context) {
        List<File> list = new ArrayList<File>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA}, null,
                new String[]{}, null);
        while (cursor.moveToNext()) {
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.i(TAG, "filePath==" + filePath);
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //Log.i(TAG, "fileName==" + fileName);
            File file = new File(filePath);
            list.add(file);
        }
        return list;
    }

    /**
     * 查询文件
     *
     * @param context
     * @return
     */
    public static List<File> getAllFiles(Context context) {
        List<File> list = new ArrayList<File>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA}, null,
                new String[]{}, null);
        while (cursor.moveToNext()) {
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.i(TAG, "filePath==" + filePath);
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //Log.i(TAG, "fileName==" + fileName);
            File file = new File(filePath);
            list.add(file);
        }
        return list;
    }

    /**
     * 获取所有的缩列图
     *
     * @param context
     * @return
     */
    public static Bitmap[] getBitmaps(Context context) {
        Bitmap[] bitmaps;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
                MediaStore.Images.Media._ID);
        int count = cursor.getCount();
        int image_column_index = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        bitmaps = new Bitmap[count];
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(image_column_index);
            bitmaps[i] = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);
        }
        return bitmaps;
    }

    /**
     * 查询图片缩列文件名称
     *
     * @param context
     * @return
     */
    public static List<String> getThumbNames(Context context) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.KIND,
                        MediaStore.Images.Thumbnails.IMAGE_ID}, null, new String[]{}, null);
        while (cursor.moveToNext()) {
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            list.add(fileName);
        }
        return list;
    }

    /**
     * 获得所有视频文件
     *
     * @param context
     */
    public static ArrayList<VideoInfo> getVideoInfo(Context context) {
        String[] thumbColumns = new String[]{
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID
        };

        String[] mediaColumns = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE
        };

        //首先检索SDcard上所有的video
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);

        ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();

        if (cursor.moveToFirst()) {
            do {
                VideoInfo info = new VideoInfo();

                info.type = VPlayerHelper.PlayerMode.LOCAL;
                info.netUrl = "";
                info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                info.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                info.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));

                //获取当前Video对应的Id，然后根据该ID获取其Thumb
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";
                String[] selectionArgs = new String[]{
                        id + ""
                };
                Cursor thumbCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);

                if (thumbCursor.moveToFirst()) {
                    info.thumbPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));

                }

                //然后将其加入到videoList
                videoList.add(info);

            } while (cursor.moveToNext());
        }
        return videoList;
    }
}
