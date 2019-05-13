package com.xht.androidnote.module.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by xht on 2019/5/7.
 */

@SuppressLint("AppCompatCustomView")
public class GlideImageView extends ImageView {
    public GlideImageView(Context context) {
        this(context, null);
    }

    public GlideImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlideImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setData(String url) {
        //Glide.with(getContext()).load(url).into(this);
    }
}
