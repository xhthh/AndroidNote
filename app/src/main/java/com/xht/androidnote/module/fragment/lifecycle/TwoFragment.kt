package com.xht.androidnote.module.fragment.lifecycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xht.androidnote.R

class TwoFragment : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.i("xht", "TwoFragment---onAttach()");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("xht", "TwoFragment---onCreate()");
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.i("xht", "TwoFragment---onCreateView()");
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("xht", "TwoFragment---onViewCreated()");
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("xht", "TwoFragment---onActivityCreated()");
    }

    override fun onStart() {
        super.onStart()
        Log.i("xht", "TwoFragment---onStart()");
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i("xht", "TwoFragment---onHiddenChanged()---hidden=$hidden");
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.i("xht", "TwoFragment---setUserVisibleHint()---isVisibleToUser=$isVisibleToUser");
    }

    override fun onResume() {
        super.onResume()
        Log.i("xht", "TwoFragment---onResume()");
    }

    override fun onPause() {
        super.onPause()
        Log.i("xht", "TwoFragment---onPause()");
    }

    override fun onStop() {
        super.onStop()
        Log.i("xht", "TwoFragment---onStop()");
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("xht", "TwoFragment---onDestroyView()");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("xht", "TwoFragment---onDestroy()");
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("xht", "TwoFragment---onDetach()");
    }


}