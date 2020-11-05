package com.xht.androidnote.module.eventdispatch;

import android.view.MotionEvent;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import butterknife.OnClick;

/**
 * Created by xht on 2018/6/21.
 * 事件分发机制
 */

public class EventDispatchActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_event_dispatch;
    }

    @Override
    protected void initEventAndData() {

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
}
