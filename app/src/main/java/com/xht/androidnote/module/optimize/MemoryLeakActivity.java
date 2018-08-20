package com.xht.androidnote.module.optimize;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.xht.androidnote.MainActivity;
import com.xht.androidnote.R;

import java.lang.ref.WeakReference;

/**
 * Created by xht on 2018/7/19.
 */

public class MemoryLeakActivity extends AppCompatActivity {


    private static TestModule sTestModule = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak);

       /* if (null == sTestModule) {
            sTestModule = new TestModule(this);
        }*/

        testHandler();


        new Thread(new Runnable() {
            @Override
            public void run() {
                // 模拟相应耗时逻辑
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void testHandler() {
        Message msg = Message.obtain();
        msg.what = 1;
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(MemoryLeakActivity.this, "memory leak", Toast.LENGTH_SHORT).show();
            }
        }
    };

    class TestModule {
        private Context mContext = null;

        public TestModule(Context context) {
            mContext = context;
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> activityWeakReference;

        public MyHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                if (msg.what == 1) {
                    // 做相应逻辑
                }
            }
        }
    }


}
