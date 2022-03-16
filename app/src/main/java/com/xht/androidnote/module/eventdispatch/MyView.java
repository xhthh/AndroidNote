package com.xht.androidnote.module.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xht.androidnote.utils.L;

public class MyView extends View {

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs,
                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("MyView---onClick()");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("View---dispatchTouchEvent()---DOWN");
                //                return true;
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("View---dispatchTouchEvent()---MOVE");
                break;
            //            return true;

            case MotionEvent.ACTION_UP:
                L.i("View---dispatchTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("View---dispatchTouchEvent()---CANCEL");
                break;
        }
        boolean result = super.dispatchTouchEvent(event);
        L.i("View---dispatchTouchEvent()---result=" + result);
        return result;
//        return true;
//                        return true;//父view和子view的事件都会走到，不会走子view的onTouchEvent()，如果需要两个都处理，可以在这里返回true
        //        return false;//如果返回false，子view不会接收到其它事件，父view也只能接收到DOWN事件，
        // 因为父view的dispatchTransformedTouchEvent()的返回值由子view的dispatchTouchEvent()决定，如果子view返回false，则不能对mFirstTouchTarget进行赋值
        //如果mFirstTouchTarget为空，后续事件调用 dispatchTransformedTouchEvent()时，传入的view为null，就会调用父view的dispatchTouchEvent()

        /*
            如果子view的dispatchTouchEvent()返回true，则会对mFirstTouchTarget进行赋值，然后break跳出对子view的遍历循环。
         */
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("View---onTouchEvent()---DOWN");
                //                return true;
//                return false;
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("View---onTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("View---onTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("View---onTouchEvent()---CANCEL");
                break;
        }
        boolean result = super.onTouchEvent(event);
//        result = false;
//        result = true;
        L.i("View---onTouchEvent()---result=" + result);
        return result;
    }


    /*
        子view的onTouchEvent() return false

        子view的onTouchEvent()接收DOWN事件，不再接收MOVE UP 事件，即如果不消耗，同一事件序列中，当前view无法再接收到事件
     */
}
