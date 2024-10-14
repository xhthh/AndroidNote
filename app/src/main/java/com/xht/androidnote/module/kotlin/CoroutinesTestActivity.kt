package com.xht.androidnote.module.kotlin

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlin.concurrent.thread

class CoroutinesTestActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_coroutines
    }

    override fun initEventAndData() {
    }

    private inner class MyHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            //主线程弹出toast
            Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //获取学生信息
    fun showStuInfo() {
        thread {
            //模拟网络请求
            Thread.sleep(3000)
            var handler = MyHandler()
            var msg = Message.obtain()
            msg.obj = "我是小鱼人"
            //发送到主线程执行
            handler.sendMessage(msg)
        }
    }

}