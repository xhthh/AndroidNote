package com.xht.androidnote.module.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2017/2/9 09:42.
 */

public class MyViewGroup2 extends RelativeLayout {
    public MyViewGroup2(Context context) {
        super(context);
    }

    public MyViewGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup2---dispatchTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup2---dispatchTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup2---dispatchTouchEvent()---UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup2---onInterceptTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup2---onInterceptTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup2---onInterceptTouchEvent()---UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup2---onTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup2---onTouchEvent()---MOVE");
                // 反拦截
                //requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup2---onTouchEvent()---UP");
                break;
        }
        return super.onTouchEvent(event);
    }
}
