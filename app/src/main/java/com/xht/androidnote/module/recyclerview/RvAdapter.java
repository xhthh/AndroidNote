package com.xht.androidnote.module.recyclerview;

import android.content.Context;
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

    @NonNull
    @Override
    public PicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false);

        Log.i("xht","RvAdapter------onCreateViewHolder()");

        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicViewHolder holder, int position) {
        Log.i("xht","RvAdapter------onBindViewHolder()");

        holder.tvTitle.setText(mList.get(position).title);
        holder.ivPic.setImageResource(R.mipmap.banner);
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
