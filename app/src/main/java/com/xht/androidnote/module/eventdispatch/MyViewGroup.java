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
        this(context,null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("MyViewGroup---onClick()");
            }
        });
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
        L.i("ViewGrouup---dispatchTouchEvent()---result=" + result);
        return result;
//                return true;//ViewGroup会接收后续事件
//                return false;//ViewGroup只能接收DWON事件
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("ViewGroup---onInterceptTouchEvent()---DOWN");
                return true;
//                break;
            case MotionEvent.ACTION_MOVE:
                L.i("ViewGroup---onInterceptTouchEvent()---MOVE");
                break;
//                return true;
            case MotionEvent.ACTION_UP:
                L.i("ViewGroup---onInterceptTouchEvent()---UP");
                break;
//                return true;

            case MotionEvent.ACTION_CANCEL:
                L.i("ViewGroup---onInterceptTouchEvent()---CANCEL");
                break;
        }
        boolean result = super.onInterceptTouchEvent(ev);
        L.i("ViewGrouup---onInterceptTouchEvent()---result=" + result);

        return result;
    }

    /*

        MOVE 中拦截 return true；子view onTouchEvent()中 DOWN返回true
        2020-08-22 23:30:30.983 25624-25624/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---DOWN
2020-08-22 23:30:30.984 25624-25624/com.xht.androidnote I/xht: ViewGroup---dispatchTouchEvent()---DOWN
2020-08-22 23:30:30.984 25624-25624/com.xht.androidnote I/xht: ViewGroup---onInterceptTouchEvent()---DOWN
2020-08-22 23:30:30.984 25624-25624/com.xht.androidnote I/xht: View---dispatchTouchEvent()---DOWN
2020-08-22 23:30:30.984 25624-25624/com.xht.androidnote I/xht: View---onTouchEvent()---DOWN
2020-08-22 23:30:31.095 25624-25624/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:30:31.096 25624-25624/com.xht.androidnote I/xht: ViewGroup---dispatchTouchEvent()---MOVE
2020-08-22 23:30:31.096 25624-25624/com.xht.androidnote I/xht: ViewGroup---onInterceptTouchEvent()---MOVE
2020-08-22 23:30:31.096 25624-25624/com.xht.androidnote I/xht: View---dispatchTouchEvent()---CANCEL
2020-08-22 23:30:31.096 25624-25624/com.xht.androidnote I/xht: View---onTouchEvent()---CANCEL
2020-08-22 23:30:31.096 25624-25624/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:30:31.114 25624-25624/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:30:31.115 25624-25624/com.xht.androidnote I/xht: ViewGroup---dispatchTouchEvent()---MOVE
2020-08-22 23:30:31.115 25624-25624/com.xht.androidnote I/xht: ViewGroup---onTouchEvent()---MOVE
2020-08-22 23:30:31.115 25624-25624/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:30:31.309 25624-25624/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---UP
2020-08-22 23:30:31.309 25624-25624/com.xht.androidnote I/xht: ViewGroup---dispatchTouchEvent()---UP
2020-08-22 23:30:31.309 25624-25624/com.xht.androidnote I/xht: ViewGroup---onTouchEvent()---UP
2020-08-22 23:30:31.309 25624-25624/com.xht.androidnote I/xht: Activity---onTouchEvent()---UP

    --------- beginning of system



    父view拦截，子view不作处理
    2020-08-22 23:35:20.534 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---DOWN
2020-08-22 23:35:20.535 25820-25820/com.xht.androidnote I/xht: ViewGroup---dispatchTouchEvent()---DOWN
2020-08-22 23:35:20.535 25820-25820/com.xht.androidnote I/xht: ViewGroup---onInterceptTouchEvent()---DOWN
2020-08-22 23:35:20.535 25820-25820/com.xht.androidnote I/xht: View---dispatchTouchEvent()---DOWN
2020-08-22 23:35:20.535 25820-25820/com.xht.androidnote I/xht: View---onTouchEvent()---DOWN
2020-08-22 23:35:20.535 25820-25820/com.xht.androidnote I/xht: ViewGroup---onTouchEvent()---DOWN
2020-08-22 23:35:20.535 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---DOWN
2020-08-22 23:35:20.612 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:35:20.612 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:35:20.625 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:35:20.625 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:35:20.644 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:35:20.645 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:35:20.658 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:35:20.658 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:35:20.675 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:35:20.675 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:35:20.695 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:35:20.695 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:35:20.712 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:35:20.712 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:35:20.724 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---MOVE
2020-08-22 23:35:20.724 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---MOVE
2020-08-22 23:35:20.807 25820-25820/com.xht.androidnote I/xht: Activity---dispatchTouchEvent()---UP
2020-08-22 23:35:20.807 25820-25820/com.xht.androidnote I/xht: Activity---onTouchEvent()---UP


     */

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
        boolean result = super.onTouchEvent(event);
        L.i("ViewGrouup---onTouchEvent()---result=" + result);

        return result;
    }
    /*
        子view dispatchTouchEvent() 返回true
     */
}
