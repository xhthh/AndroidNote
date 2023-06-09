package com.xht.androidnote.module.kotlin

import android.util.Log
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityKotlinTestBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class KotlinTestActivity : BaseViewActivity<ActivityKotlinTestBinding>() {
    override fun getViewBinding(): ActivityKotlinTestBinding {
        return ActivityKotlinTestBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_kotlin_test
    }

    override fun initEventAndData() {
        binding.btnCoroutinesTest.setOnClickListener {
//            testCoroutines1()
//            testCoroutines2()
//            testCoroutines3()
            testCoroutines4()
        }
    }

    private fun testCoroutines4() {

    }

    @get:Synchronized
    var maxRequests = 64
        set(maxRequests) {
            synchronized(this) {
                field = maxRequests
            }
        }


    private fun testCoroutines3() {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("AA", "协程测试 开始执行，线程：${Thread.currentThread().name}")

            var token = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getToken()
            }.await()

            var response = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getResponse(token)
            }.await()

            setText(response)
        }

        Log.d("AA", "主线程协程后面代码执行，线程：${Thread.currentThread().name}")
    }

    /**
     * 多协程间 suspend 函数运行
     */
    private fun testCoroutines2() {
        //外层必须包一层协程，因为 await() 只能在一个协程中或者另外一个 suspend 方法中运行
        GlobalScope.launch(Dispatchers.Unconfined) {
            val token = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getToken()
            }.await()//await() 阻塞外部协程，所以代码还是顺序执行的，适用于多个同级 IO 操作的情况

            val response = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getResponse(token)
            }.await()
            setText(response)
        }
    }

    //单协程内多个suspend函数运行
    private fun testCoroutines1() {
        // 运行代码
        GlobalScope.launch(Dispatchers.Unconfined) {
            Log.d("AA", "协程 开始执行，时间:  ${System.currentTimeMillis()}")
            val token = getToken()
            val response = getResponse(token)
            setText(response)
        }
        for (i in 1..9) {
            Log.d("AA", "主线程打印第$i 次，时间:  ${System.currentTimeMillis()}")
        }
    }


    suspend fun getToken(): String {
        delay(1)
        Log.d(
            "AA",
            "getToken 开始执行，时间:  ${System.currentTimeMillis()} 线程：${Thread.currentThread().name}"
        )
        return "ask"
    }

    suspend fun getResponse(token: String): String {
        delay(2)
        Log.d(
            "AA",
            "getResponse 开始执行，时间:  ${System.currentTimeMillis()} 线程：${Thread.currentThread().name}"
        )
        return "response"
    }

    fun setText(response: String) {
        Log.d(
            "AA",
            "setText 执行，时间:  ${System.currentTimeMillis()} 线程：${Thread.currentThread().name}"
        )
    }
}