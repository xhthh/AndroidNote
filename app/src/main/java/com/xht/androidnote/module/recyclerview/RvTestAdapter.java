package com.xht.androidnote.module.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xht.androidnote.R;

import java.util.List;

public class RvTestAdapter extends RecyclerView.Adapter<RvTestAdapter.RvTestViewHolder> {

    private List<String> list = null;
    private Context mContext;

    public RvTestAdapter(List<String> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @NonNull
    @Override
    public RvTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_test, parent, false);
        return new RvTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvTestViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class RvTestViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public RvTestViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
