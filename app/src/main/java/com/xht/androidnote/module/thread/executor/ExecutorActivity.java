package com.xht.androidnote.module.thread.executor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2019/8/18.
 */
public class ExecutorActivity extends BaseActivity {


    @BindView(R.id.btn_extcutor_submit)
    Button btnExtcutorSubmit;
    @BindView(R.id.btn_test_execute)
    Button btnTestExecute;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_executor;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.btn_extcutor_submit, R.id.btn_test_execute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_extcutor_submit:
                testSubmit();
                break;
            case R.id.btn_test_execute:
                testExecuteRunnable(new MyThreadExecutor());
                break;
        }
    }

    private void testExecuteRunnable(MyThreadExecutor executor) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("xht", "testExecuteRunnable()---当前线程 ：" + Thread.currentThread().getName());

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("xht", "testExecuteRunnable()---切换线程 ：" + Thread.currentThread().getName());
                    }
                });
            }
        }, "工作线程000，须切换回主线程").start();
    }

    private void testSubmit() {
        ExecutorService executorService = Executors.newFixedThreadPool(7);

        String outputs = null;
        try {
            outputs = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "I am a task, which submited by the so called laoda, and run by those anonymous workers";
                }
            }).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i("xht", "testtSubmit()---outputs==" + outputs);
    }

    class MyThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            handler.post(runnable);
        }
    }
}
