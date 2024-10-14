package com.xht.androidnote.module.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xht.androidnote.utils.L;

public class TestView extends View {

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs,
                    int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                L.i("TestView---onClick()");
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("TestView---dispatchTouchEvent()---DOWN");
                //                return true;
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("TestView---dispatchTouchEvent()---MOVE");
                break;
            //            return true;

            case MotionEvent.ACTION_UP:
                L.i("TestView---dispatchTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("TestView---dispatchTouchEvent()---CANCEL");
                break;
        }
        boolean result = super.dispatchTouchEvent(event);
//        L.i("TestView---dispatchTouchEvent()---result=" + result);
        return result;
//        return false;
//        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("TestView---onTouchEvent()---DOWN");
                //                return true;
//                return false;
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("TestView---onTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("TestView---onTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("TestView---onTouchEvent()---CANCEL");
                break;
        }
        boolean result = super.onTouchEvent(event);
//        result = false;
//        result = true;
//        L.i("TestView---onTouchEvent()---result=" + result);
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(200,200);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r+100, b+100);
    }
}
