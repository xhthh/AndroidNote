package com.xht.androidnote.module.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        RvTestViewHolder holder = new RvTestViewHolder(view);
        Log.i("xht", "Adapter---onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RvTestViewHolder holder, int position) {
        Log.i("xht", "Adapter---onBindViewHolder---position=" + position);
        holder.tvTitle.setText(list.get(position));
        holder.tvPosition.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    class RvTestViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvPosition;

        public RvTestViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPosition = itemView.findViewById(R.id.tvPosition);
        }
    }
}
