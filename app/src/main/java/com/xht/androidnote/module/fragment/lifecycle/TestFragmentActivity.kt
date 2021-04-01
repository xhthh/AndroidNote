package com.xht.androidnote.module.fragment.lifecycle

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_test_fragment.*

class TestFragmentActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_test_fragment
    }

    private val oneFragment = OneFragment()
    private val twoFragment = TwoFragment()

    private var title: String? = null

    private var tag = "one"

    private var currentFragment: Fragment = oneFragment

    private var fragment1: OneFragment? = null
    private var fragment2: TwoFragment? = null

    override fun initEventAndData() {
        //testAddAndHide()
        //testReplace(currentFragment, tag)
        initListener()
    }

    private fun initListener() {
        btn_replace.setOnClickListener {
            if (tag == "one") {
                tag = "two"
                currentFragment = twoFragment
            } else {
                tag = "one"
                currentFragment = oneFragment
            }
            testReplace(currentFragment, tag)
        }

        btn_add.setOnClickListener {
            tag = "one"
            val beginTransaction = supportFragmentManager.beginTransaction()
            if (fragment1 == null) {
                fragment1 = OneFragment()
                beginTransaction.add(R.id.fl_root, fragment1!!, tag)
            }
            hideFragment(beginTransaction)
            beginTransaction.show(fragment1!!)
            beginTransaction.commitAllowingStateLoss()
        }

        btn_hide.setOnClickListener {
            tag = "two"
            val beginTransaction = supportFragmentManager.beginTransaction()
            if (fragment2 == null) {
                fragment2 = TwoFragment()
                beginTransaction.add(R.id.fl_root, fragment2!!, tag)
            }
            hideFragment(beginTransaction)
            beginTransaction.show(fragment2!!)
            beginTransaction.commitAllowingStateLoss()
        }
    }


    private fun testReplace(fragment: Fragment, tag: String) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(R.id.fl_root, fragment, tag)

        beginTransaction.commitAllowingStateLoss()
    }

    private fun hideFragment(beginTransaction: FragmentTransaction) {
        if (fragment1 != null && tag == "two") {
            beginTransaction.hide(fragment1!!)
        }
        if (fragment2 != null && tag == "one") {
            beginTransaction.hide(fragment2!!)
        }
    }


    /*
    replace：
    OneFragment---onAttach()
    OneFragment---onCreate()
    OneFragment---onCreateView()
    OneFragment---onViewCreated()
    OneFragment---onActivityCreated()
    OneFragment---onStart()
    OneFragment---onResume()
    TwoFragment---onAttach()
    TwoFragment---onCreate()
    OneFragment---onPause()
    OneFragment---onStop()
    OneFragment---onDestroyView()
    OneFragment---onDestroy()
    OneFragment---onDetach()
    TwoFragment---onCreateView()
    TwoFragment---onViewCreated()
    TwoFragment---onActivityCreated()
    TwoFragment---onStart()
    TwoFragment---onResume()





    add f1 add f2 show f1 show f2
    OneFragment---onAttach()
    OneFragment---onCreate()
    OneFragment---onCreateView()
    OneFragment---onViewCreated()
    OneFragment---onActivityCreated()
    OneFragment---onStart()
    OneFragment---onResume()
    TwoFragment---onAttach()
    TwoFragment---onCreate()
    OneFragment---onHiddenChanged()
    TwoFragment---onCreateView()
    TwoFragment---onViewCreated()
    TwoFragment---onActivityCreated()
    TwoFragment---onStart()
    TwoFragment---onResume()
    TwoFragment---onHiddenChanged()
    OneFragment---onHiddenChanged()
    OneFragment---onHiddenChanged()
    TwoFragment---onHiddenChanged()
     */
}