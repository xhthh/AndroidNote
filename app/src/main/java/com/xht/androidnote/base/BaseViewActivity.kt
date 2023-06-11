package com.xht.androidnote.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by xht on 2018/4/23.
 */
abstract class BaseViewActivity<T : ViewBinding> : FragmentActivity() {
    @JvmField
    protected var mContext: Activity? = null
    private var mBind: Unbinder? = null

    lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setBaseConfig()
        setContentView(binding.root)
        mBind = ButterKnife.bind(this)
        mContext = this
        initEventAndData()
    }

    protected abstract fun getViewBinding(): T

    /**
     * todo 这里没用了，setContentView(binding.getRoot())
     */
    protected abstract fun getLayoutId(): Int
    protected abstract fun initEventAndData()
    private fun setBaseConfig() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    protected fun skip2Activity(clazz: Class<*>?) {
        if (mContext != null) mContext!!.startActivity(Intent(mContext, clazz))
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