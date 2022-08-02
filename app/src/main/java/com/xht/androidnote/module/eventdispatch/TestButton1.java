package com.xht.androidnote.module.eventdispatch;

import android.content.Context;

import androidx.appcompat.widget.AppCompatButton;

import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2020/8/22
 */
public class TestButton1 extends AppCompatButton {
    public TestButton1(Context context) {
        this(context, null);
    }

    public TestButton1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestButton1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("TestButton1---dispatchTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("TestButton1---dispatchTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("TestButton1---dispatchTouchEvent---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("TestButton1---dispatchTouchEvent---CANCEL");
                break;
        }
        boolean result = super.dispatchTouchEvent(event);
        L.i("TestButton1---dispatchTouchEvent()---result=" + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("TestButton1---onTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("TestButton1---onTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("TestButton1---onTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("TestButton1---onTouchEvent()---CANCEL");
                break;
        }
        //return super.onTouchEvent(event);

        boolean result = super.onTouchEvent(event);
        L.i("TestButton1---onTouchEvent()---result=" + result);
        return result;
    }
}
