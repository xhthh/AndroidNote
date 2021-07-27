package com.xht.androidnote.module.kotlin

import android.content.Intent
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.module.kotlin.multiSelect.MultiSelectActivity
import kotlinx.android.synthetic.main.activity_kotlin_test.*

class KotlinTestActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_kotlin_test
    }

    override fun initEventAndData() {
        btnNullTest.setOnClickListener {

            val bean = Bean()
            bean.desc = null

            //java.lang.IllegalStateException: bean.desc must not be null
            //这里没抓到异常，是不是kotlin版本的问题
            setMessage(bean.desc)

        }

        btnMultiSelect.setOnClickListener {
            startActivity(Intent(this, MultiSelectActivity::class.java))
        }

        btnSearch.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }
    }

    var data: String? = null

    fun setMessage(data: String) {
        this.data = data
    }

}