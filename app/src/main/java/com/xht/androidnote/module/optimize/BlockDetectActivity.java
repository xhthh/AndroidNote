package com.xht.androidnote.module.optimize;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.xht.androidnote.R;
import com.xht.androidnote.module.view.fps.FpsViewActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Choreographer周期性的在UI重绘时候触发，在代码中记录上一次和下一次绘制的时间间隔，
 * 如果超过16ms，就意味着一次UI线程重绘的“丢帧”。
 * 丢帧的数量为间隔时间除以16，如果超过3，就开始有卡顿的感知。
 */
public class BlockDetectActivity extends AppCompatActivity {

    private static final String TAG = "BlockDetect";
    Button button;
    TextView tvFps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Choreographer.getInstance().postFrameCallback(mFrameCallback);
        MYTest();
        button = findViewById(R.id.bottom);
        tvFps = findViewById(R.id.tv_fps);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiLongTimeWork();
                Log.e(BlockDetectActivity.class.getSimpleName(), "button click");
            }
        });
        ChoreographerMonitor.INSTANCE.startMonitor(new Function1<Integer, Unit>() {
            @SuppressLint("SetTextI18n")
            @Override
            public Unit invoke(Integer integer) {
                String result = String.valueOf(integer);
                Log.e(TAG,"------monitor---result = " + result);
                tvFps.setText(result);
                return null;
            }
        });
    }

    private MyFrameCallback mFrameCallback = new MyFrameCallback();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public class MyFrameCallback implements Choreographer.FrameCallback {
        private String TAG = "性能检测";
        private long lastTime = 0;

        @Override
        public void doFrame(long frameTimeNanos) {
            if (lastTime == 0) {
                //代码第一次初始化。不做检测统计。
                lastTime = frameTimeNanos;
            } else {
                long times = (frameTimeNanos - lastTime) / 1000000;
                int frames = (int) (times / (1000 / getRefreshRate()));
                if (times > 16) {
                    Log.e(TAG, "UI线程超时(超过16ms):" + times + "ms" + " , 丢帧:" + frames);
                }
                lastTime = frameTimeNanos;
            }
            Choreographer.getInstance().postFrameCallback(mFrameCallback);
        }
    }

    private float getRefreshRate() {
        //获取屏幕主频频率
        Display display = getWindowManager().getDefaultDisplay();
        float refreshRate = display.getRefreshRate();
        Log.e(TAG, "屏幕主频频率 =" + refreshRate);
        return refreshRate;
    }

    private void MYTest() {
        setContentView(R.layout.activity_block_detect);
        Log.e(BlockDetectActivity.class.getSimpleName(), "MYTest");
    }

    private void uiLongTimeWork() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}