package com.xht.androidnote;

import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.eventbus.EventBusHelper;
import com.xht.androidnote.module.eventbus.TestEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xht on 2018/9/10.
 */

public class TestActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initEventAndData() {
        eventBusTest();
    }

    private void eventBusTest() {
        EventBusHelper.getInstance().register(this);
        EventBusHelper.getInstance().post(new TestEvent("卡米哈米哈"));
    }

    /**
     * 如果不写接收的方法会报错，可以想办法封装到基类当中
     * Caused by: org.greenrobot.eventbus.EventBusException: Subscriber class com.xht.androidnote.TestActivity and its super classes have no public methods with the @Subscribe annotation
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TestEvent event) {
    }

    @Override
    protected void onDestroy() {
        EventBusHelper.getInstance().unregister(this);
        super.onDestroy();
    }
}
