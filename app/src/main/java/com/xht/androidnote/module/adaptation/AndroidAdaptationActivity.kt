package com.xht.androidnote.module.adaptation

import android.util.Log
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityAdaptationBinding
import com.xht.androidnote.utils.CommonUtils

class AndroidAdaptationActivity : BaseViewActivity<ActivityAdaptationBinding>() {
    private val TAG = "adaptation"
    override fun getViewBinding(): ActivityAdaptationBinding {
        return ActivityAdaptationBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_adaptation
    }

    override fun initEventAndData() {
        binding.btnAndroid10.setOnClickListener {
            startActivity<Adaptation10Activity>(this)
        }
        binding.btnAndroid11.setOnClickListener {
            test()
        }
        binding.btnAndroid12.setOnClickListener {
            skip2Activity(Adaptation10Activity::class.java)
        }
        binding.btnAndroid13.setOnClickListener {
            startActivity<Adaptation13Activity>()
        }
    }

    private fun test() {
        //android 11 软件包可见性
        val b = CommonUtils.checkAppInstalled(this, "com.tencent.mm")
        val b1 = CommonUtils.checkAppInstalled(this, "com.alipay.android.app")
        val b12 = CommonUtils.checkAppInstalled(this, "com.eg.android.AlipayGphone")
        val b123 = CommonUtils.checkAppInstalled(this, "com.jfbank.wanka")
        Log.e(TAG, "------是否安装微信---$b------是否安装支付宝1---$b1------是否安装支付宝2---$b12")
        Log.e(TAG, "------是否安装万卡---$b123")

        //状态栏高度
        val statusBarHeight = CommonUtils.getStatusBarHeight(this)
        val statusBarHeight2 = CommonUtils.getNavigationBarHeight(this)
        Log.e(TAG, "------statusBarHeight = $statusBarHeight   导航栏高度 = $statusBarHeight2")

        //Display.getSize() 废弃
        val size = CommonUtils.calculateMaxBitmapSize(this)
        Log.e(TAG, "------Bitmap 最大可用---$size")
    }
}