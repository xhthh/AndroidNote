package com.xht.androidnote.base

import androidx.viewbinding.ViewBinding

/**
 * 改成了 ViewBinding，为了全都重写一遍，单独抽一个基类
 */
abstract class BaseViewActivity<T : ViewBinding> : BaseActivity() {
    lateinit var binding: T

    override fun initLayout(): Boolean {
        binding = getViewBinding()
        setContentView(binding.root)
        return true
    }

    protected abstract fun getViewBinding(): T
}