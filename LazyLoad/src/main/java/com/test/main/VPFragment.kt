package com.test.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.test.lazyload.R
import com.test.utils.LogUtil

class FirstVPFragment : LOGFragment(1) {
    override fun getLogTag(): String {
        return FirstVPFragment::class.java.simpleName
    }

}

class SecondVPFragment : LOGFragment(2) {
    override fun getLogTag(): String {
        return SecondVPFragment::class.java.simpleName
    }
}
class ThirdVPFragment : LOGFragment(3) {
    override fun getLogTag(): String {
        return ThirdVPFragment::class.java.simpleName
    }
}
class FourVPFragment : LOGFragment(4) {
    override fun getLogTag(): String {
        return FourVPFragment::class.java.simpleName
    }

}
class FiveVPFragment : LOGFragment(5) {
    override fun getLogTag(): String {
        return FiveVPFragment::class.java.simpleName
    }

}

abstract class LOGFragment(private val index:Int):Fragment(){

    abstract fun getLogTag():String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vp_label,container,false)
    }
    init {
        val bundle = Bundle()
         bundle.putInt("Index",index)
        arguments = bundle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_label).text = ""+arguments?.get("Index")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.i(getLogTag()) {
            "onCreate index ${arguments?.get("Index")}"
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtil.i(getLogTag()) {
            "onAttach${arguments?.get("Index")}"
        }
    }

    override fun onStart() {
        super.onStart()
        LogUtil.i(getLogTag()) {
            "onStart${arguments?.get("Index")}"
        }
    }

    override fun onResume() {
        super.onResume()
        LogUtil.i(getLogTag()) {
            "onResume${arguments?.get("Index")}"
        }
    }

    override fun onPause() {
        super.onPause()
        LogUtil.i(getLogTag()) {
            "onPause${arguments?.get("Index")}"
        }
    }

    override fun onStop() {
        super.onStop()
        LogUtil.i(getLogTag()) {
            "onStop${arguments?.get("Index")}"
        }
    }

    override fun onDetach() {
        super.onDetach()
        LogUtil.i(getLogTag()) {
            "onDetach${arguments?.get("Index")}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.i(getLogTag()) {
            "onDestroyView${arguments?.get("Index")}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.i(getLogTag()) {
            "onDestroy${arguments?.get("Index")}"
        }
    }
}
