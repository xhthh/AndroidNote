package com.xht.androidnote.module.eventdispatch;

import android.view.MotionEvent;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

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

    /*@OnClick({R.id.view, R.id.view_group_2, R.id.view_group_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view:
                break;
            case R.id.view_group_2:
                break;
            case R.id.view_group_1:
                break;
        }
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.i("Activity---dispatchTouchEvent()---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                L.i("Activity---dispatchTouchEvent()---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.i("Activity---dispatchTouchEvent()---UP");
                break;
        }

        return super.dispatchTouchEvent(ev);
        //        return false;//不走onTouchEvent()
        //        return true;//消费事件
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
        }
        return super.onTouchEvent(event);
    }


    /*
    1、View dispatchTouchEvent 返回 default
        Activity---dispatchTouchEvent()---DOWN
        ViewGroup1---dispatchTouchEvent()---DOWN
        ViewGroup1---onInterceptTouchEvent()---DOWN
        View---dispatchTouchEvent()---DOWN
        View---onTouchEvent()---DOWN
        ViewGroup1---onTouchEvent()---DOWN
        Activity---onTouchEvent()---DOWN
        Activity---dispatchTouchEvent()---UP
        Activity---onTouchEvent()---UP


    2、View dispatchTouchEvent 返回true
        Activity---dispatchTouchEvent()---DOWN
        ViewGroup1---dispatchTouchEvent()---DOWN
        ViewGroup1---onInterceptTouchEvent()---DOWN
        View---dispatchTouchEvent()---DOWN
        Activity---dispatchTouchEvent()---UP
        ViewGroup1---dispatchTouchEvent()---UP
        ViewGroup1---onInterceptTouchEvent()---UP
        View---dispatchTouchEvent()---UP

    3、View dispatchTouchEvent 返回false
        Activity---dispatchTouchEvent()---DOWN
        ViewGroup1---dispatchTouchEvent()---DOWN
        ViewGroup1---onInterceptTouchEvent()---DOWN
        View---dispatchTouchEvent()---DOWN
        ViewGroup1---onTouchEvent()---DOWN
        Activity---onTouchEvent()---DOWN
        Activity---dispatchTouchEvent()---UP
        Activity---onTouchEvent()---UP


    4、ViewGroup onInterceptTouchEvent 返回false（default）
        Activity---dispatchTouchEvent()---DOWN
        ViewGroup1---dispatchTouchEvent()---DOWN
        ViewGroup1---onInterceptTouchEvent()---DOWN
        View---dispatchTouchEvent()---DOWN
        View---onTouchEvent()---DOWN
        ViewGroup1---onTouchEvent()---DOWN
        Activity---onTouchEvent()---DOWN
        Activity---dispatchTouchEvent()---UP
        Activity---onTouchEvent()---UP

    5、ViewGroup onInterceptTouchEvent 返回true
        Activity---dispatchTouchEvent()---DOWN
        ViewGroup1---dispatchTouchEvent()---DOWN
        ViewGroup1---onInterceptTouchEvent()---DOWN
        ViewGroup1---onTouchEvent()---DOWN
        Activity---onTouchEvent()---DOWN
        Activity---dispatchTouchEvent()---UP
        Activity---onTouchEvent()---UP

    6、View onTouchEvent 返回true
        Activity---dispatchTouchEvent()---DOWN
        ViewGroup1---dispatchTouchEvent()---DOWN
        ViewGroup1---onInterceptTouchEvent()---DOWN
        View---dispatchTouchEvent()---DOWN
        View---onTouchEvent()---DOWN
        Activity---dispatchTouchEvent()---UP
        ViewGroup1---dispatchTouchEvent()---UP
        ViewGroup1---onInterceptTouchEvent()---UP
        View---dispatchTouchEvent()---UP
        View---onTouchEvent()---UP

    7、View onTouchEvent 返回false（default)
        Activity---dispatchTouchEvent()---DOWN
        ViewGroup1---dispatchTouchEvent()---DOWN
        ViewGroup1---onInterceptTouchEvent()---DOWN
        View---dispatchTouchEvent()---DOWN
        View---onTouchEvent()---DOWN
        ViewGroup1---onTouchEvent()---DOWN
        Activity---onTouchEvent()---DOWN
        Activity---dispatchTouchEvent()---UP
        Activity---onTouchEvent()---UP

        public boolean dispatchTouchEvent(MotionEvent ev) {
            boolean consume = false;//事件是否被消费
            if (onInterceptTouchEvent(ev)){//调用onInterceptTouchEvent判断是否拦截事件
                consume = onTouchEvent(ev);//如果拦截则调用自身的onTouchEvent方法
            }else{
                consume = child.dispatchTouchEvent(ev);//不拦截调用子View的dispatchTouchEvent方法
            }
            return consume;//返回值表示事件是否被消费，true事件终止，false调用父View的onTouchEvent方法
        }

     */

}
