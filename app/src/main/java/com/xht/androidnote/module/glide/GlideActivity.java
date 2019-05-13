package com.xht.androidnote.module.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.io.File;
import java.util.concurrent.ExecutionException;

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
    @BindView(R.id.giv)
    GlideImageView mGiv;

    private Context mContext;

    private String url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";

    private String gif = "http://p1.pstatp.com/large/166200019850062839d3";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_glide;
    }

    @Override
    protected void initEventAndData() {
        mContext = this;
    }

    @OnClick(R.id.btn_load)
    public void onViewClicked() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.banner)
                .skipMemoryCache(true);
        //.diskCacheStrategy(DiskCacheStrategy.NONE);


        transformMode();

        //mGiv.setData(url);

        /*GlideApp.with(mContext)
                .load(gif)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.error)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用缓存功能（杀死进程后会重新加载占位图，否则再次点击还是会直接加载图片）
                .override(500,500)
                .into(mIvGlideTest1);*/
    }

    private void transformMode() {
        RequestOptions options = new RequestOptions()
                .circleCrop();

        Glide.with(this)
                .load(url)
                .apply(options)
                .into(mIvGlideTest1);
    }

    /**
     * 返回false就表示这个事件没有被处理，还会继续向下传递，返回true就表示这个事件已经被处理掉了，从而不会再继续向下传递。
     */
    private void loadImageWithListener() {
        Glide.with(this)
                .load("http://guolin.tech/book.png")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.i("xht","onloadFailed()");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.i("xht","onResourceReady()");
                        return false;
                    }
                })
                .into(mIvGlideTest1);
    }

    private void intoTarget() {
        SimpleTarget<Drawable> target = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                mIvGlideTest1.setImageDrawable(resource);
            }
        };

        Glide.with(this)
                .load("http://guolin.tech/book.png")
                .into(target);
    }

    /**
     * 预加载，preload() 替换 into()
     */
    private void preLoad() {
        Glide.with(this)
                .load("http://guolin.tech/book.png")
                .preload();
    }


    /**
     * 下载图片，获取下载的路径
     */
    private void submit() {
        new Thread(() -> {
            FutureTarget<File> submit = Glide.with(GlideActivity.this)
                    .asFile()
                    .load(url)
                    .submit();
            try {
                File file = submit.get();
                String absolutePath = file.getAbsolutePath();
                Log.i("xht", "path=" + absolutePath);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
