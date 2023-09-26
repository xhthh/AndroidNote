package com.xht.androidnote.module.asm

import android.util.Log
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityAopTestBinding


class ASMTestActivity : BaseViewActivity<ActivityAopTestBinding>() {
    override fun getViewBinding(): ActivityAopTestBinding {
        return ActivityAopTestBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_aop_test
    }

    override fun initEventAndData() {
        binding.btnAsmTest.setOnClickListener {
            test()
        }
    }

    fun test() {
        Log.e("AopTest", "------asm 测试------")
    }
}