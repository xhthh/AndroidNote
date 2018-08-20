package com.xht.androidnote.module.asynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/20.
 * 一、AsyncTask封装了线程池和Handler
 *      两个线程池：
 *          1、SerialExecutor：用于任务的排队
 *          2、THREAD_POOL_EXECUTOR：用于真正的执行任务
 *      Android3.0后，串行执行，但是可以通过executeOnExecutor()方法并行执行。
 *
 * 二、基本使用
 * 三、缺陷
 *      容易造成内存泄漏
 */

public class AsyncTaskActivity extends BaseActivity {
    @BindView(R.id.tv_execute_result)
    TextView mTvExecuteResult;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private String result = "";
    private DownloadTask mDownloadTask;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asynctask;
    }

    @Override
    protected void initEventAndData() {

        //异步任务的实例必须在UI线程中创建，即AsyncTask对象必须在UI线程中创建?
        //这里在子线程中也能成功
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadTask task = new DownloadTask();
                task.execute();
                //一个任务实例只能执行一次，如果执行第二次将会抛出异常：java.lang.IllegalStateException: Cannot execute task: the task is already running.
                //mDownloadTask.execute();
            }
        }).start();
    }

    @OnClick({R.id.btn_execute_serial, R.id.btn_execute_async, R.id.btn_async_task_download, R.id
            .btn_async_task_download_cancel})
    public void onViewClicked(View view) {
        mTvExecuteResult.setText("");
        result = "";
        switch (view.getId()) {
            case R.id.btn_execute_serial:
                new MyAsyncTask("AsyncTask#1").execute("");
                new MyAsyncTask("AsyncTask#2").execute("");
                new MyAsyncTask("AsyncTask#3").execute("");
                new MyAsyncTask("AsyncTask#4").execute("");
                new MyAsyncTask("AsyncTask#5").execute("");
                break;
            case R.id.btn_execute_async:
                new MyAsyncTask("AsyncTask#1").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        "");
                new MyAsyncTask("AsyncTask#2").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        "");
                new MyAsyncTask("AsyncTask#3").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        "");
                new MyAsyncTask("AsyncTask#4").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        "");
                new MyAsyncTask("AsyncTask#5").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        "");
                break;

            case R.id.btn_async_task_download:
                mProgressBar.setMax(100);
                mDownloadTask = new DownloadTask();
                mDownloadTask.execute();
                break;
            case R.id.btn_async_task_download_cancel:
                // 注意这里cancle方法只是将对应的AsyncTask标注为cancle状态，并不是真正的取消线程的执行
                mDownloadTask.cancel(true);
                mProgressBar.setProgress(0);
                break;
        }
    }

    class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = 0; i < 100; i++) {
                if (isCancelled()) {
                    break;
                }

                publishProgress(i);

                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (isCancelled()) {
                return;
            }
            L.i("values[0]==" + values[0]);
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        private String mName = "AsyncTask";

        public MyAsyncTask(String name) {
            super();
            mName = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            publishProgress(222);

            return mName;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            result = s + "execute finish at " + dateFormat.format(new Date()) + "\n" + result;

            mTvExecuteResult.setText(result);

            L.i(result);
        }
    }
}
