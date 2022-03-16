package com.xht.androidnote.module.eventdispatch;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/21.
 * 事件分发机制
 */

public class EventDispatchActivity extends BaseActivity {
    @BindView(R.id.viewgroup)
    MyViewGroup viewgroup;
    @BindView(R.id.testCount)
    MyViewGroup testCount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_event_dispatch;
    }

    @Override
    protected void initEventAndData() {


        int maxDeep = maxDeep(testCount);
        L.i("maxDeep = " + maxDeep);

        //int count = traversal2(testCount);
        int count = traversal3(testCount);
        L.i("count = " + count);
    }

    private int traversal3(View view) {
        if (!(view instanceof ViewGroup)) {
            return 0;
        }

        ViewGroup viewGroup = (ViewGroup) view;

        if (viewgroup.getChildCount() == 0) {
            return 0;
        }

        int max = 0;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            int deep = traversal3(viewGroup.getChildAt(i)) + 1;
            if (deep > max) {
                max = deep;
            }
        }

        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {

                return false;
            }
        });
    }

    /**
     * 这个方法会在我们以任意的方式开始与Activity进行交互的时候被调用。
     * 比较常见的场景就是屏保：当我们一段时间没有操作会显示一张图片，当我们开始与Activity交互的时候可在这个方法中取消屏保；
     * 另外还有没有操作自动隐藏工具栏，可以在这个方法中让工具栏重新显示。
     *
     */
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        L.e("------onUserInteraction()------");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("Activity---dispatchTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("Activity---dispatchTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("Activity---dispatchTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("Activity---dispatchTouchEvent()---CANCEL");
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("Activity---onTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("Activity---onTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("Activity---onTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("Activity---onTouchEvent()---CANCEL");
                break;
        }
        return super.onTouchEvent(event);
    }

    @OnClick(R.id.btn_test_dispatch)
    public void onViewClicked() {
    }

    private int maxDeep(View view) {
        //当前的view已经是最底层view了，不能往下累加层数了，返回0，代表view下面只有0层了
        if (!(view instanceof ViewGroup)) {
            return 0;
        }
        ViewGroup vp = (ViewGroup) view;
        //虽然是viewgroup，但是如果并没有任何子view，那么也已经是最底层view了，不能往下累加层数了，返回0，代表view下面只有0层了
        if (vp.getChildCount() == 0) {
            return 0;
        }
        //用来记录最大层数
        int max = 0;
        //广度遍历view
        for (int i = 0; i < vp.getChildCount(); i++) {
            //由于vp拥有子view，所以下面还有一层，因为可以+1，来叠加一层，然后再递归几岁算它的子view的层数
            int deep = maxDeep(vp.getChildAt(i)) + 1;
            //比较哪个大就记录哪个
            if (deep > max) {
                max = deep;
            }
        }
        return max;
    }

    public int traversal(View view) {
        if (!(view instanceof ViewGroup)) {
            return 0;
        }

        ViewGroup viewGroup = (ViewGroup) view;

        if (viewGroup.getChildCount() == 0) {
            return 0;
        }
        int max = 0;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            int deep = traversal(viewGroup.getChildAt(i)) + 1;
            if (deep > max) {
                max = deep;
            }
        }

        return max;
    }

    private int traversal2(View view) {
        if (!(view instanceof ViewGroup)) {
            return 0;
        }

        ViewGroup viewGroup = (ViewGroup) view;

        if (viewgroup.getChildCount() == 0) {
            return 0;
        }

        int max = 0;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int deep = traversal(child) + 1;
            if (deep > max) {
                max = deep;
            }
        }
        return max;
    }
}
