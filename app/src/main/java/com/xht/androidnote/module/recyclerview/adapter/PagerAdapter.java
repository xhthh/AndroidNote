package com.xht.androidnote.module.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xht.androidnote.R;
import com.xht.androidnote.module.listview.LvEntity;

import java.util.List;

/**
 * Created by xht on 2019/4/11.
 */

public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.PagerViewHolder> {

    private List<LvEntity> mList;

    public PagerAdapter(List<LvEntity> list) {
        mList = list;
    }

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pager, parent, false);

        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {
        LvEntity entity = mList.get(position);
        holder.mIvCover.setImageResource(entity.imgId);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class PagerViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvCover;

        public PagerViewHolder(View itemView) {
            super(itemView);

            mIvCover = itemView.findViewById(R.id.iv_cover);
        }
    }

}
