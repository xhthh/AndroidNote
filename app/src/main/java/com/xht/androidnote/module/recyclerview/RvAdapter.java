package com.xht.androidnote.module.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.module.listview.LvEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xht on 2019/4/2.
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.PicViewHolder> {

    private Context mContext;
    private List<LvEntity> mList = new ArrayList<>();

    public RvAdapter(Context context, List<LvEntity> list) {
        mContext = context;
        mList = list;
    }

    public void setDatas(List<LvEntity> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public PicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false);

        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicViewHolder holder, int position) {
        holder.tvTitle.setText(mList.get(position).title);
        holder.ivPic.setImageResource(mList.get(position).imgId);
    }

    /**
     * @param holder
     * @param position
     * @param payloads getChangePayload() 中返回的数据
     */
    @Override
    public void onBindViewHolder(@NonNull PicViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Bundle payload = (Bundle) payloads.get(0);
            LvEntity entity = mList.get(position);
            for (String key : payload.keySet()) {
                switch (key) {
                    case "imgid":
                        holder.ivPic.setImageResource(payload.getInt("imgid"));
                        break;
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class PicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivPic;

        public PicViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivPic = itemView.findViewById(R.id.iv_pic);
        }
    }

}
