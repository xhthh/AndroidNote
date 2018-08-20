package com.xht.androidnote.module.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xht.androidnote.utils.L;

public class MyView extends View {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs,
                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("View---dispatchTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("View---dispatchTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("View---dispatchTouchEvent()---UP");
                break;
        }
//        return super.dispatchTouchEvent(event);
                return false;
        //        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("View---onTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("View---onTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("View---onTouchEvent()---UP");
                break;
        }
        return super.onTouchEvent(event);
//        return true;
//        return false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        L.i("MyView---onMeasure()");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        L.i("MyView---onLayout()");
    }
}
