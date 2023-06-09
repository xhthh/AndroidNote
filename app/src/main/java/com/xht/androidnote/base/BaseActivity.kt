package com.xht.androidnote.base

import android.app.Activity
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
        setContentView(getLayoutId())
        mBind = ButterKnife.bind(this)
        mContext = this
        initEventAndData()
    }

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