package com.xht.androidnote.module.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import com.xht.androidnote.R;

public class MyViewPager extends ViewGroup {

    private int[] image_id = {R.mipmap.img1, R.mipmap.img2, R.mipmap.img3};

    private GestureDetector mDetector;
    private Scroller mScroller;

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        for (int i = 0; i < image_id.length; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setBackgroundResource(image_id[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            this.addView(iv);
        }

        mScroller = new Scroller(getContext());

        mDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //scrollBy：相对滑动，相对我们当前的控件多少距离，就滑动多少距离
                //distanceX是我们手滑动的距离，即我们的手相对控件滑动了多少，所以X轴滑动这个距离，Y轴滑动0
                scrollBy((int) distanceX, 0);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }


    /**
     * 摆放子view的位置，这里是将图片一张一张排开，每一张都充满屏幕
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < image_id.length; i++) {
            this.getChildAt(i).layout(i * getWidth(), t, (i + 1) * getWidth(), b);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                //你滑动的距离加上屏幕的一半，除以屏幕宽度，如果你滑动距离超过了屏幕的一半，这个pos就加1
                int pos = (scrollX + getWidth() / 2) / getWidth();
                //滑到最后一张时，不能出界
                if (pos >= image_id.length) {
                    pos = image_id.length - 1;
                }
                //绝对滑动，直接滑到指定的x值
                //scrollTo(pos * getWidth(), 0);

                //自然滑动,从手滑到的地方开始，滑动距离是页面宽度减去滑到的距离，时间由路程的大小来决定
                mScroller.startScroll(scrollX, 0, pos * getWidth() - scrollX, 0, Math.abs(pos * getWidth()));
                invalidate();
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }
}
