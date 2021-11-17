package com.xht.androidnote.module.kotlin.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerArrayAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private String[] mStringArray;
    private int mtextViewResourceId;
    private float msize;

    public CustomSpinnerArrayAdapter(Context context, int textViewResourceId,
                                     String[] objects, float size) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        mContext = context;
        mStringArray = objects;
        mtextViewResourceId = textViewResourceId;
        msize = size;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //修改spinner选择后结果的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mtextViewResourceId, parent, false);
        }

        //此处Text1是spinner默认的用来显示文字的textview
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray[position]);
        tv.setTextSize(msize);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //修改Spinner展开后的字体
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mtextViewResourceId, parent, false);
        }
        //修改Text1是Spinner默认的用来显示文字的Textview
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray[position]);
        tv.setTextSize(msize + 5);

        return convertView;
    }
}