package com.test.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.test.lazyload.BaseLazyLoadFragment
import com.test.lazyload.R
import com.test.utils.LogUtil


class MyLazyLoadFragment : BaseLazyLoadFragment() {
    private lateinit var imageView:ImageView
    private lateinit var textView:TextView
    var tabIndex = 0

    fun newInstance(position: Int): MyLazyLoadFragment? {
        val bundle = Bundle()
        bundle.putInt("Position", position)
        val fragment = MyLazyLoadFragment()
        fragment.arguments = bundle
        return fragment
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_lazy_loading
    }

    override fun initView(view: View) {
        imageView = view.findViewById<ImageView>(R.id.iv_content)
        textView = view.findViewById<TextView>(R.id.tv_loading)
        tabIndex = requireArguments().getInt(MyFragment.INTENT_INT_INDEX)
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        LogUtil.i {
            "onLazyLoad"
        }
    }

    override fun onStopLazyLoad() {
        super.onStopLazyLoad()
        LogUtil.i {
            "onStopLazyLoad"
        }
    }
}