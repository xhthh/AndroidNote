package com.xht.androidnote.module.glide;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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

    private String url = "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";
    //private String url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";
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

        Glide.with(this)
                .load(url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.splash)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide","------onLoadFailed()---e.message = " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        Log.e("Glide","------onResourceReady()------");
                        return false;
                    }
                })
                .into(mIvGlideTest1);


        /*GlideApp.with(mContext)
                .load(gif)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.error)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用缓存功能（杀死进程后会重新加载占位图，否则再次点击还是会直接加载图片）
                .override(500,500)
                .into(mIvGlideTest1);*/
    }

    private void test() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(200, 100);
        Glide.with(this)
                .load("")
                .apply(options)
                .into(mIvGlideTest1);

        Bitmap bitmap = BitmapFactory.decodeFile("");
        //        BitmapFactory.decodeResource()
    }

    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        //设置 inJustDecodeBounds = true，解析图片原始宽高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //计算 inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //设置 inJustDecodeBounds = false，加载图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        //得到图片原始宽高
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            //计算最大的 inSampleSize 值，该值是 2 的指数，并且保持高度和宽度都大于请求的高度和宽度
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
