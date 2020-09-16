package com.xht.androidnote.module.thread;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.thread.executor.ExecutorActivity;
import com.xht.androidnote.utils.L;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2019/8/18.
 */
public class ThreadTestActivity extends BaseActivity {

    @BindView(R.id.btn_start1)
    Button btnStart1;
    @BindView(R.id.btn_start2)
    Button btnStart2;
    @BindView(R.id.btn_executor)
    Button btnExecutor;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_thread_test;
    }

    @Override
    protected void initEventAndData() {
    }

    @OnClick({R.id.btn_executor, R.id.btn_start1, R.id.btn_start2, R.id.btn_start3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_executor:
                skip2Activity(ExecutorActivity.class);
                break;
            case R.id.btn_start1:
                test1();
                break;
            case R.id.btn_start2:
                test2();
                break;
            case R.id.btn_start3:
                t2.start();
                t1.start();
                t3.start();
                break;
        }
    }


    class MyThread extends Thread {
        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            L.i("正在运行的线程名称：" + currentThread().getName() + " 开始");
            try {
                Thread.sleep(2000);    //延时2秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            L.i("正在运行的线程名称：" + currentThread().getName() + " 结束");

            Looper.prepare();
            Toast.makeText(ThreadTestActivity.this, "子线程弹toast", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            return "Hello Callable";
        }
    }

    private void test3() {
        MyCallable callable = new MyCallable();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(callable);
        try {
            String result = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test2() {
        MyThread myThread = new MyThread("线程2");
        //线程可以设置优先级，1<=priority<=10
        //myThread.setPriority(10);
        /*
            导致当前执行线程处于让步状态。如果有其他的可运行线程具有至少与此线程同样高的优先级，
            那么这些线程接下来会被调度。注意，这是一个静态方法。
         */
        //Thread.yield();

        myThread.start();
    }

    private void test1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("xht", "当前线程 " + Thread.currentThread().getName());
            }
        }, "线程1").start();
    }


    class MyRunnable implements Runnable {

        @Override
        public void run() {
            L.i("MyRunnable---run()---name==" + Thread.currentThread().getName());
        }
    }

    private String str = "xht";

    public synchronized String getStr() {
        return str;
    }

    public synchronized void setStr(String str) {
        this.str = str;
    }

    final Thread t1 = new Thread(new Runnable() {
        public void run() {
            System.out.println(Thread.currentThread().getName() + " run 1");
        }
    }, "T1");
    final Thread t2 = new Thread(new Runnable() {
        public void run() {
            try {
                t1.join(10);
                System.out.println(Thread.currentThread().getName() + " run 2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }, "T2");
    final Thread t3 = new Thread(new Runnable() {
        public void run() {
            try {
                t2.join(10);
                System.out.println(Thread.currentThread().getName() + " run 3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }, "T3");

}
