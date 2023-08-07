package com.xht.androidnote.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.FragmentActivity
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by xht on 2018/4/23.
 */
abstract class BaseActivity : FragmentActivity() {
    protected lateinit var mContext: Activity
    private var mBind: Unbinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBaseConfig()
        //BaseViewActivity中处理binding使用，避免initEventAndData()中使用binding使未初始化
        if (!initLayout()) {
            setContentView(getLayoutId())
        }
        mBind = ButterKnife.bind(this)
        mContext = this
        initEventAndData()
    }

    protected open fun initLayout(): Boolean {
        return false
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initEventAndData()
    private fun setBaseConfig() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    protected fun skip2Activity(clazz: Class<*>?) {
        mContext.startActivity(Intent(mContext, clazz))
    }

    /**
     * reified 将内联函数的类型参数标记为在运⾏时可访问
     */
    protected inline fun <reified T> startActivity(mContext: Context) {
        val intent = Intent(mContext, T::class.java)
        mContext.startActivity(intent)
    }

    protected inline fun <reified T> startActivity() {
        val intent = Intent(mContext, T::class.java)
        mContext.startActivity(intent)
    }

    protected inline fun <reified T> startActivity(mContext: Context, block: Intent.() -> Unit) {
        val intent = Intent(mContext, T::class.java)
        intent.block()
        mContext.startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBind != null) {
            mBind!!.unbind()
        }
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Class<?> event) {

    }*/
    override fun onResume() {
        super.onResume()
        //        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
//        ViewClickHookUtil.INSTANCE.hookAllChildView(decorView);
    }
}