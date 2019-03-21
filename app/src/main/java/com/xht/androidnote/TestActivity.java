package com.xht.androidnote;

import android.util.Log;
import android.widget.EditText;

import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.eventbus.EventBusHelper;
import com.xht.androidnote.module.eventbus.TestEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by xht on 2018/9/10.
 */

public class TestActivity extends BaseActivity {
    private int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 有N处理器，便长期保持N个活跃线程。
     */
    private final int CORE_POOL_SIZE = CPU_COUNT;
    /**
     * 不限制最大线程数
     */
    private final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    /**
     * 非活跃线程过期时间为10s
     */
    private final int KEEP_ALIVE = 10;
    /**
     * 采用直接提交的方式
     */
    private final BlockingQueue<Runnable> sPoolWorkQueue = new SynchronousQueue<>();
    /**
     * 设置线程优先级
     */
    private final MyThreadFactory sThreadFactory = new MyThreadFactory();

    private final ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);


    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;//尾部注释
    }

    @Override
    protected void initEventAndData() {
        EditText et_content = findViewById(R.id.et_content);
        et_content.setError("请输入发动机号");
        et_content.requestFocus();

        execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("xht", "run()------i=" + i);
                }
            }
        });

        Log.i("xht","执行结束");


    }

    private void execute(Runnable runnable) {
        Future future = mThreadPoolExecutor.submit(runnable);
        try {
            future.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    int a = 2;
    int b = 3;
    int c;

    private void test() {
        if (a > b)
            c = a + b;

        if (a < b)
            c = b - a;


    }

    /**
     * EventBus测试
     */
    private void eventBusTest() {
        EventBusHelper.getInstance().register(this);
        EventBusHelper.getInstance().post(new TestEvent("卡米哈米哈"));
    }

    /**
     * 如果不写接收的方法会报错，可以想办法封装到基类当中
     * Caused by: org.greenrobot.eventbus.EventBusException: Subscriber class com.xht.androidnote.TestActivity and its super classes have no public methods with the @Subscribe annotation
     * 接收的方法必须为public
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


    private class MyThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(final Runnable runnable) {
            return new Thread(new Runnable() {
                @Override
                public void run() {
                    //设置线程优先级为BACKGROUND，防止与UI线程竞争时间片
                    //通过android的方式设置线程优先级，尽量不使用java的方式thread.setPriority
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    runnable.run();
                }
            });
        }
    }
}
