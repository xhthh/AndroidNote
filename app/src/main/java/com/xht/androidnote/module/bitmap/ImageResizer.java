package com.xht.androidnote.module.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xht.androidnote.utils.L;

import java.io.FileDescriptor;

/**
 * Created by xht on 2018/7/10.
 */

public class ImageResizer {

    public ImageResizer() {
    }

    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth,
                                                        int reqHeight) {
        // 设置inJustDecodeBounds=true，解析图片原始宽高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // 计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 设置inJustDecodeBounds=false，加载图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public Bitmap decodeSampleBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int
            reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
            reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        // 得到图片原始宽高
        int width = options.outWidth;
        int height = options.outHeight;

        L.i("origin，width=" + width + "  height=" + height);

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            //计算最大的inSampleSize值，该值为2的指数，并且保持高度和宽度都大于请求的高度和宽度。
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        L.i("sampleSize:" + inSampleSize);
        return inSampleSize;
    }

}
