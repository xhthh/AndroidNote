package com.xht.androidnote.module.eventbus;

import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xht on 2019/8/16.
 */
public class EventBusAnotherActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_eventbus_another;
    }

    @Override
    protected void initEventAndData() {
        EventBusHelper.getInstance().register(this);

        Button btnSendMessage = findViewById(R.id.btn_send_message);
        Button btnSendSticky = findViewById(R.id.btn_send_sticky);

        btnSendMessage.setOnClickListener(this);
        btnSendSticky.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_message:
                sendMessage();
                break;
            case R.id.btn_send_sticky:
                sendStickyMessage();
                break;
        }
    }

    private void sendMessage() {
        EventBusHelper.getInstance().post(new TestEvent("普通消息"));
    }

    private void sendStickyMessage() {
        EventBusHelper.getInstance().postSticky(new TestEvent("粘性消息"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(OneEvent event) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.getInstance().unregister(this);
    }
}
