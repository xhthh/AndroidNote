package com.xht.androidnote.module.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.LruCache;
import android.widget.ImageView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by xht on 2018/7/10.
 */

public class BitmapActivity extends BaseActivity {
    @BindView(R.id.iv_bitmap_round_test)
    ImageView ivBitmapRoundTest;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bitmap;
    }

    @Override
    protected void initEventAndData() {


        test();

        testRoundCorner();
    }

    private void testRoundCorner() {
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.video_cover));

        roundedDrawable.getPaint().setAntiAlias(true);
        roundedDrawable.setCornerRadius(30);
        ivBitmapRoundTest.setImageDrawable(roundedDrawable);
    }

    private void test() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };


        //============LinkedHashMap=======================//
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>(0, 0.75f,
                true);

        map.put(0, 0);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);
        map.get(1);
        map.get(2);

        //当设置为true时，最近访问的最后输出
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            L.i(entry.getKey() + "：" + entry.getValue());
        }
    }

}
