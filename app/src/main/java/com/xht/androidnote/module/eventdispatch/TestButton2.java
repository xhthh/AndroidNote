package com.xht.androidnote.module.eventdispatch;

import android.content.Context;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2020/8/22
 */
public class TestButton2 extends AppCompatButton {
    public TestButton2(Context context) {
        this(context, null);
    }

    public TestButton2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestButton2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("TestButton2------onClickListener---");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("TestButton2---dispatchTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("TestButton2---dispatchTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("TestButton2---dispatchTouchEvent---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("TestButton2---dispatchTouchEvent---CANCEL");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("TestButton2---onTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("TestButton2---onTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("TestButton2---onTouchEvent()---UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                L.i("TestButton2---onTouchEvent()---CANCEL");
                break;
        }
        return super.onTouchEvent(event);
    }
}
