package com.xht.androidnote.module.ipc.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;
import com.xht.androidnote.utils.MyConstants;

import butterknife.OnClick;

/**
 * Created by xht on 2018/7/3.
 */

public class MessengerActivity extends BaseActivity {

    private Messenger mService;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_SERVICE:
                    L.i("MessengerActivity---receive msg from Service：" + msg.getData().getString
                            ("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "龟派气功");
            msg.setData(data);

            msg.replyTo = mGetReplyMessenger;

            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_messenger;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    @OnClick({R.id.btn_messenger_bind, R.id.btn_messenger_unbind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_messenger_bind:
                Intent intent = new Intent(mContext, MessengerService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_messenger_unbind:

                break;
        }
    }
}
