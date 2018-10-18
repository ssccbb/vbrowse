package com.sung.vbrowse.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sung.vbrowse.R;
import com.sung.vbrowse.mvp.model.VideoInfo;
import com.sung.vbrowse.mvp.ui.activity.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sung
 * @date: 2018/10/17
 * @Description:
 */
public class FileListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List mData;

    public FileListAdapter(Context mContext, List<VideoInfo> mData) {
        if (this.mData == null) this.mData = new ArrayList();
        if (mData != null) this.mData.addAll(mData);
        this.mContext = mContext;
    }

    public void addData(List<VideoInfo> mData, boolean clear) {
        if (mData == null || mData.isEmpty()) return;
        if (clear) {
            this.mData.clear();
        } else {
            this.mData.addAll(mData);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FileListHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_file_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof FileListHolder) {
            ((FileListHolder) viewHolder).onBind(i);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class FileListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View root;
        private TextView mTittle;
        private TextView mPath;

        public FileListHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            mTittle = itemView.findViewById(R.id.tv_tittle);
            mPath = itemView.findViewById(R.id.tv_path);
        }

        void onBind(int position) {
            try {
                VideoInfo video = (VideoInfo) mData.get(position);
                mTittle.setText(video.title);
                mPath.setText(video.filePath);
                root.setTag(video);
                root.setOnClickListener(this);
            } catch (IndexOutOfBoundsException e) {
                Log.e(FileListAdapter.class.getSimpleName(),
                        "onBind: position ---> " + position + "\nerror ---> " + e.toString());
            } catch (ClassCastException e) {
                Log.e(FileListAdapter.class.getSimpleName(),
                        "onBind: position ---> " + position + "\nerror ---> " + e.toString());
            }
        }

        @Override
        public void onClick(View view) {
            if (view == root && view.getTag() != null) {
                try {
                    VideoInfo video = (VideoInfo) view.getTag();
                    PlayerActivity.open(mContext, video);
                } catch (ClassCastException e) {
                    Log.e(FileListAdapter.class.getSimpleName(), "view " + view.getId()
                            + " click to play! get data error --> " + e.toString());
                }
            }
        }
    }
}
