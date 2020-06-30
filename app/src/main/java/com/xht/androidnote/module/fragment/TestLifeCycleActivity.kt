package com.xht.androidnote.module.fragment

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.util.Log
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity

class TestLifeCycleActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test_life_cycle;
    }

    override fun initEventAndData() {
        val fragmentManager = supportFragmentManager
        val beginTransaction = fragmentManager.beginTransaction()

        val fragment = TestLifeCycleFragment()

        beginTransaction.add(R.id.fl_test_life_cycle, fragment)
        beginTransaction.commitAllowingStateLoss()
        beginTransaction.show(fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("xht", "Activity---onCreate()");
    }

    override fun onStart() {
        super.onStart()
        Log.i("xht", "Activity---onStart()");
    }

    override fun onResume() {
        super.onResume()
        Log.i("xht", "Activity---onResume()");
    }

    override fun onPause() {
        super.onPause()
        Log.i("xht", "Activity---onPause()");
    }

    override fun onStop() {
        super.onStop()
        Log.i("xht", "Activity---onStop()");
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("xht", "Activity---onRestart()");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("xht", "Activity---onDestroy()");
    }
}