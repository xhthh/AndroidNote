package com.xht.androidnote.module.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2017/2/9 09:42.
 */

public class MyViewGroup1 extends RelativeLayout {
    public MyViewGroup1(Context context) {
        super(context);
    }

    public MyViewGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup1---dispatchTouchEvent()---DOWN");
//                return true;
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup1---dispatchTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup1---dispatchTouchEvent()---UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
//                return true;
        //        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup1---onInterceptTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup1---onInterceptTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup1---onInterceptTouchEvent()---UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
//        return true;
//        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup1---onTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup1---onTouchEvent()---MOVE");
                // 反拦截
                //requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup1---onTouchEvent()---UP");
                break;
        }
//        return super.onTouchEvent(event);
        return true;
        //        return false;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

}
