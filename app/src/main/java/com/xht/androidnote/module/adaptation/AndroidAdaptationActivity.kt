package com.xht.androidnote.module.adaptation

import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityAdaptationBinding

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
            skip2Activity(Adaptation10Activity::class.java)
        }
        binding.btnAndroid11.setOnClickListener {
            skip2Activity(Adaptation10Activity::class.java)
        }
        binding.btnAndroid12.setOnClickListener {
            skip2Activity(Adaptation10Activity::class.java)
        }
        binding.btnAndroid13.setOnClickListener {
            skip2Activity(Adaptation13Activity::class.java)
        }
    }
}