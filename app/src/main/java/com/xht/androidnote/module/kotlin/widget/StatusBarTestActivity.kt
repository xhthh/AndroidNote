package com.xht.androidnote.module.kotlin.widget

import android.os.Build
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityStatusBarBinding
import com.xht.androidnote.utils.StatusBarUtil

/**
 * 沉浸式状态栏测试
 */
class StatusBarTestActivity : BaseViewActivity<ActivityStatusBarBinding>() {
    override fun getViewBinding(): ActivityStatusBarBinding {
        return ActivityStatusBarBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_status_bar
    }

    override fun initEventAndData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BarUtils.setStatusBarAlpha(this, 0)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val statusBarHeight = StatusBarUtil.getStatusBarHeight(this)
        Log.e("statusBarHeight", "---statusBarHeight = $statusBarHeight")
    }
}