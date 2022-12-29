package com.xht.androidnote.module.fragment.lifecycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xht.androidnote.R

class OneFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("xht", "OneFragment---onAttach()");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("xht", "OneFragment---onCreate()");
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.i("xht", "OneFragment---onCreateView()");
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("xht", "OneFragment---onViewCreated()");
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("xht", "OneFragment---onActivityCreated()");
    }

    override fun onStart() {
        super.onStart()
        Log.i("xht", "OneFragment---onStart()");
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i("xht", "OneFragment---onHiddenChanged()---hidden=$hidden");
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.i("xht", "OneFragment---setUserVisibleHint()---isVisibleToUser=$isVisibleToUser");
    }

    override fun onResume() {
        super.onResume()
        Log.i("xht", "OneFragment---onResume()");
    }

    override fun onPause() {
        super.onPause()
        Log.i("xht", "OneFragment---onPause()");
    }

    override fun onStop() {
        super.onStop()
        Log.i("xht", "OneFragment---onStop()");
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("xht", "OneFragment---onDestroyView()");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("xht", "OneFragment---onDestroy()");
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("xht", "OneFragment---onDetach()");
    }


}