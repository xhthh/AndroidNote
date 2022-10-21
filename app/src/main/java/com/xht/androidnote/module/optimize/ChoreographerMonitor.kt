package com.xht.androidnote.module.optimize

import android.content.Context
import android.os.Build
import android.view.Choreographer
import android.view.WindowManager
import com.xht.androidnote.base.App


object ChoreographerMonitor {
    @Volatile
    private var isStart = false
    private val monitorFrameCallback by lazy { MonitorFrameCallback() }
    private var mListener: (Int) -> Unit = {}
    private var mLastTime = 0L

    fun startMonitor(listener: (Int) -> Unit) {
        if (isStart) {
            return
        }
        mListener = listener
        Choreographer.getInstance().postFrameCallback(monitorFrameCallback)
        isStart = true
    }

    fun stopMonitor() {
        isStart = false
        Choreographer.getInstance().removeFrameCallback { monitorFrameCallback }
    }

    class MonitorFrameCallback : Choreographer.FrameCallback {

        private val refreshRate by lazy {
            //计算刷新率 赋值给refreshRate
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                App.getAppContext().display?.refreshRate ?: 16.6f
            } else {
                val windowManager =
                    App.getAppContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                windowManager.defaultDisplay.refreshRate
            }
        }

        /**
         * 通过设置Choreographer的FrameCallback，可以在每一帧被渲染的时候记录下它开始渲染的时间，
         * 这样在下一帧被处理时，我们可以根据时间差来判断上一帧在渲染过程中是否出现掉帧。
         * Android中，每发出一个VSYNC信号都会通知界面进行重绘、渲染，
         * 每一次同步周期为16.6ms，代表一帧的刷新频率。
         * 每次需要开始渲染的时候都会回调doFrame()，如果某2次doFrame()之间的时间差大于16.6ms，
         * 则说明发生了UI有点卡顿，已经在掉帧了，拿着这个时间差除以16.6就得出了掉过了多少帧。
         */
        override fun doFrame(frameTimeNanos: Long) {
            mLastTime = if (mLastTime == 0L) {
                frameTimeNanos
            } else {
                //frameTimeNanos的单位是纳秒,这里需要计算时间差,然后转成毫秒
                val time = (frameTimeNanos - mLastTime) / 1000000
                //跳过了多少帧
                val frames = (time / (1000f / refreshRate)).toInt()
                if (frames > 1) {
                    mListener.invoke(frames)
                }
                frameTimeNanos
            }
            Choreographer.getInstance().postFrameCallback(this)
        }

    }
}