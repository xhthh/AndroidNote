package com.xht.androidnote.module.fragment.lazy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class BaseLazyFragment : Fragment() {

    // Fragment 当前状态是否可见
    protected var mVisible = false

    // 标志是否已被加载过，第二次就不再请求数据
    private var mHasLoadedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflateView(inflater, container, savedInstanceState)
        lazyLoad()
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            mVisible = true
            onVisible()
        } else {
            mVisible = false
            onInvisible()
        }
    }

    /**
     * 说明：加载 View 视图
     */
    protected abstract fun inflateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    /**
     * 说明：懒加载
     */
    private fun lazyLoad() {
        if (!mVisible || mHasLoadedOnce) return
        lazyLoadData()
        mHasLoadedOnce = true
    }

    /**
     * 说明：懒加载数据
     */
    protected fun lazyLoadData() {}

    /**
     * 说明：Fragment 当前状态可见
     */
    protected fun onVisible() {}

    /**
     * 说明：Fragment 当前状态不可见
     */
    protected fun onInvisible() {}

}