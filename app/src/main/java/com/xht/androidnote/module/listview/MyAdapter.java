package com.xht.androidnote.module.listview;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xht.androidnote.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xht on 2019/4/2.
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private List<LvEntity> mList = new ArrayList<>();

    private LayoutInflater mInflater;

    public MyAdapter(Context context, List<LvEntity> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("xht", "MyAdapter---getView()");

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.item_listview, null, true);

            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.ivPic = convertView.findViewById(R.id.iv_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(mList.get(position).title);

        holder.ivPic.setImageResource(R.mipmap.banner);


        return convertView;
    }

    class ViewHolder {
        private TextView tvTitle;
        private ImageView ivPic;
    }
}
