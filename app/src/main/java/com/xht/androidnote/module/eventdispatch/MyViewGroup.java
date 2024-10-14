package com.xht.androidnote.module.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2017/2/9 09:42.
 */

public class MyViewGroup extends RelativeLayout {
    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                L.i("MyViewGroup---onClick()");
//            }
//        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup---dispatchTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup---dispatchTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup---dispatchTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("ViewGroup---dispatchTouchEvent()---CANCEL");
                break;
        }

        boolean result = super.dispatchTouchEvent(ev);
        // L.i("ViewGrouup---dispatchTouchEvent()---result=" + result);
        return result;
        // return true;//ViewGroup会接收后续事件
        // return false;//ViewGroup只能接收DWON事件
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup---onInterceptTouchEvent()---DOWN");
                //return true;
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup---onInterceptTouchEvent()---MOVE");
                break;
//                return true;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup---onInterceptTouchEvent()---UP");
                break;
            //return true;
            case MotionEvent.ACTION_CANCEL:
                L.i("ViewGroup---onInterceptTouchEvent()---CANCEL");
                break;
        }
        return super.onInterceptTouchEvent(ev);
        //boolean result = super.onInterceptTouchEvent(ev);
        //L.i("ViewGrouup---onInterceptTouchEvent()---result=" + result);
        //result = false;
        //return result;
        //return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup---onTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup---onTouchEvent()---MOVE");
                // 反拦截
                //requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup---onTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("ViewGroup---onTouchEvent()---CANCEL");
                break;
        }
        //return super.onTouchEvent(event);
        boolean result = super.onTouchEvent(event);
        //result = true;
        //L.i("ViewGrouup---onTouchEvent()---result=" + result);
        return result;
    }
    /*
        子view dispatchTouchEvent() 返回true
     */
}
