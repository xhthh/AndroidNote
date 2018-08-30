package com.xht.androidnote.module.glide;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/5/29.
 * Glide4.7.1
 * com.android.support包必须>=27
 * <p>
 * https://muyangmin.github.io/glide-docs-cn/
 */
public class GlideActivity extends BaseActivity {
    @BindView(R.id.iv_glide_test1)
    ImageView mIvGlideTest1;

    private String url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";
    private String gif = "http://p1.pstatp.com/large/166200019850062839d3";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_glide;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick(R.id.btn_load)
    public void onViewClicked() {

        Glide.with(this).load(url).into(mIvGlideTest1);


        /*GlideApp.with(mContext)
                .load(gif)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.error)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用缓存功能（杀死进程后会重新加载占位图，否则再次点击还是会直接加载图片）
                .override(500,500)
                .into(mIvGlideTest1);*/
    }
}
