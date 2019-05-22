package com.xht.androidnote.module.customview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.xht.androidnote.bean.ImgList;
import com.xht.androidnote.utils.ScreenUtils;

import java.util.List;

/**
 * Created by xht on 2019/5/20.
 */

public class MultiImageLayout extends LinearLayout {

    //照片数据
    private List<ImgList> imagesList;

    private int oneMaxWandH;  // 单张图最大宽高
    private int moreWandH = 0;// 多张图的宽高
    private int imagePadding = ScreenUtils.dip2px(getContext(), 3);// 图片间的间距

    public static int MAX_WIDTH = 0;//该View的最大宽度
    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数，即最大3列

    private LayoutParams onePicLP;//单图的layoutParams，图片自适应
    private LayoutParams morePicLp;//多图的layoutParams，宽高相同，（屏幕-边距）/ 3
    private LayoutParams morePicLpColumnFirst;//多图中，第一列图片的layoutParams
    private LayoutParams rowLp;//每一行的layoutParams，宽match，高wrap

    public MultiImageLayout(Context context) {
        this(context, null);
    }

    public MultiImageLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置图片数据
     *
     * @param photos
     */
    public void setPhotos(List<ImgList> photos) {
        Log.i("xht", "setPhotos()---photos.size()==" + photos.size());

        if (photos == null || photos.size() == 0) {
            return;
        }

        imagesList = photos;

        if (MAX_WIDTH > 0) {
            moreWandH = (MAX_WIDTH - imagePadding * 2) / 3;//多图时，每张图片的宽高
            oneMaxWandH = MAX_WIDTH * 2 / 3;//一张图片最大宽高为当前View的2/3
            initImageLayoutParams();
        }

        Log.i("xht", "setPhotos()---photos.size()==" + photos.size());

        initView();
    }

    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;

        onePicLP = new LayoutParams(wrap, wrap);
        rowLp = new LayoutParams(match, wrap);

        morePicLp = new LayoutParams(moreWandH, moreWandH);
        //不是第一列的，设置左边距
        morePicLp.setMargins(imagePadding, 0, 0, 0);
        morePicLpColumnFirst = new LayoutParams(moreWandH, moreWandH);
    }

    /**
     * 根据 ImageView 的数量初始化布局
     */
    private void initView() {
        Log.i("xht", "initView()");

        setOrientation(VERTICAL);
        removeAllViews();

        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量WzMultiImageLayout的最大宽度，WzMultiImageLayout的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        int imgCount = imagesList.size();

        if (imgCount == 1) {//一张图
            Log.i("xht", "initView()---一张图");
            addView(createImageView(0, false));
        } else {//多图
            if (imgCount == 4) {
                //4张图，两行两列
                MAX_PER_ROW_COUNT = 2;
            } else {
                MAX_PER_ROW_COUNT = 3;
            }

            //行数 = 图片数量/每行最大数 + 图片数%每行最大数（如果大于0，多加1行）
            int rowCount = imgCount / MAX_PER_ROW_COUNT + (imgCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);
            Log.i("xht", "initView()---rowCount=" + rowCount);

            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                //每一行创建一个横向的LinearLayout，宽match，高wrap
                LinearLayout rowLinearLayout = new LinearLayout(getContext());
                rowLinearLayout.setOrientation(HORIZONTAL);
                rowLinearLayout.setLayoutParams(rowLp);

                //如果不是第一行，设置与上一行的间距
                if (rowIndex != 0) {
                    rowLinearLayout.setPadding(0, imagePadding, 0, 0);
                }

                /**
                 * ●    ●   ●
                 *
                 * ●    ●   ●
                 *
                 * ●    ●   ●
                 */
                //最后一行列数 = 图片数 % 每行最大数，等于0即最大列数，不等于0，则取模结果即列数，比如5张图，5%3=2，columnCount=2
                int columnCount = imgCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT : imgCount % MAX_PER_ROW_COUNT;
                Log.i("xht", "initView()---1---columnCount=" + columnCount);

                //如果不是最后一行，列数即为最大列数
                if (rowIndex != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT;
                }

                Log.i("xht", "initView()---2---columnCount=" + columnCount);

                addView(rowLinearLayout);

                //行偏移，统计图片的具体位置，第一行为0，第二行为MAX_PER_ROW_COUNT
                int rowOffset = rowIndex * MAX_PER_ROW_COUNT;

                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    //计算每张图片的具体位置
                    int position = rowOffset + columnIndex;
                    Log.i("xht", "initView()---position=" + position);
                    //为每一行的LinearLayout添加ImageView
                    rowLinearLayout.addView(createImageView(position, true));
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) {
                    setPhotos(imagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //支持自适应模式
    private int measureWidth(int widthMeasureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(specSize, result);
            }
        }

        return result;
    }

    private View createImageView(int position, boolean isMultiImage) {
        Log.i("xht", "createImageView()---position=" + position + "  isMultiImage=" + isMultiImage);

        ImgList img = imagesList.get(position);
        ImageView imageView = new ImageView(getContext());
        if (isMultiImage) {//多图
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //设置layoutParams，如果不是第一列，有左边距
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? morePicLpColumnFirst : morePicLp);
        } else {//单图
            //imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            int expectW = img.imgwidth;
            int expectH = img.imgheight;

            Log.i("xht", "createImageView()---expectW=" + expectW + "  expectH=" + expectH);

            if (expectW == 0 || expectH == 0) {//传入图片没有设置宽高
                imageView.setLayoutParams(onePicLP);//设置layoutParams，图片自适应
            } else {
                int actualW;
                int actualH;

                float scale = (float) expectH / (float) expectW;//高宽比

                //根据图片传入宽高，计算实际需要展示的宽高
                if (expectW > oneMaxWandH) {//最大2/3
                    actualW = oneMaxWandH;
                    actualH = (int) (actualW * scale);
                } else if (expectW < moreWandH) {//最小1/3
                    actualW = moreWandH;
                    actualH = (int) (actualW * scale);
                } else {
                    actualW = expectW;
                    actualH = expectH;
                }

                Log.i("xht", "createImageView()---actualW=" + actualW + "  actualH=" + actualH);

                imageView.setLayoutParams(new LayoutParams(actualW, actualH));
            }
        }

        //设置图片背景、加载图片
        //ImageLoaderUtils.getInstance().display(getContext(), img.img, imageView, R.drawable.base_img_default);
        Glide.with(getContext()).load(img.img).into(imageView);

        return imageView;
    }
}
