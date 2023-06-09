package com.xht.androidnote.module.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseFragment
import com.xht.androidnote.databinding.FragmentTestLifeCycleBinding

class TestLifeCycleFragment : BaseFragment() {

    private lateinit var binding: FragmentTestLifeCycleBinding
    override fun getLayoutId(): Int {
        return R.layout.fragment_test_life_cycle;
    }

    override fun initEventAndData() {
        val str = "Activity---onCreate()\n" +
                "Fragment---onAttach()\n" +
                "Fragment---onCreate()\n" +
                "Fragment---onCreateView()\n" +
                "Fragment---onActivityCreated()\n" +
                "Fragment---onStart()\n" +
                "Activity---onStart()\n" +
                "Activity---onResume()\n" +
                "Fragment---onResume()\n" +
                "Fragment---onPause()\n" +
                "Activity---onPause()\n" +
                "Fragment---onStop()\n" +
                "Activity---onStop()\n" +
                "Fragment---onDestroyView()\n" +
                "Fragment---onDestroy()\n" +
                "Fragment---onDetach()\n" +
                "Activity---onDestroy()"
        binding.tvResult.text = str
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("xht", "Fragment---onAttach()");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("xht", "Fragment---onCreate()");
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.i("xht", "Fragment---onCreateView()");
        binding = FragmentTestLifeCycleBinding.inflate(layoutInflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("xht", "Fragment---onViewCreated()");
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("xht", "Fragment---onActivityCreated()");
    }

    override fun onStart() {
        super.onStart()
        Log.i("xht", "Fragment---onStart()");
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i("xht", "Fragment---onHiddenChanged()");
    }

    override fun onResume() {
        super.onResume()
        Log.i("xht", "Fragment---onResume()");
    }

    override fun onPause() {
        super.onPause()
        Log.i("xht", "Fragment---onPause()");
    }

    override fun onStop() {
        super.onStop()
        Log.i("xht", "Fragment---onStop()");
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("xht", "Fragment---onDestroyView()");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("xht", "Fragment---onDestroy()");
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("xht", "Fragment---onDetach()");
    }
}