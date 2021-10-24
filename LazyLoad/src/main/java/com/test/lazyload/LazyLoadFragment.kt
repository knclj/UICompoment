package com.test.lazyload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.utils.LogUtil

/**
 *
 *ViewPage 旧版本懒加载实现方案
 */
abstract class LazyLoadFragment() : Fragment() {

    private var rootView:View?= null;
    private var isCreateViewed = false
    private var isVisibleShowed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(),container,false)
        isCreateViewed = true
        initView(rootView!!)
        if(userVisibleHint){
            dispatchVisible(true)
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        LogUtil.i {
            "onResume"
        }
    }

    /**
     * 此方法为非生命周期函数，需要状态控制
     * 使用 setUserVisibleHint
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //界面已初始化
        if(isCreateViewed){
            //之前可视化状态为isVisibleShowed
            if(isVisibleShowed && !isVisibleToUser){
                dispatchVisible(false)
            }else if(!isVisibleToUser && isVisibleToUser){
                dispatchVisible(true)
            }
        }
    }

    private fun dispatchVisible(isVisibleToUser: Boolean){
        isVisibleShowed = isVisibleToUser
        if(isVisibleToUser && !isParentVisible()){
            return
        }
        if(isVisibleToUser){
            //仅仅支持一层嵌套
            onLazyLoad()
            dispatchChildVisible(true)
        }else{
            //仅仅支持一层嵌套
            onStopLazyLoad()
            dispatchChildVisible(false)
        }
    }

    private fun dispatchChildVisible(visible: Boolean){
       val fragmentManager = parentFragmentManager
        fragmentManager.fragments.forEach {
            if (!it.isHidden && it.userVisibleHint &&it is LazyLoadFragment){
                it.dispatchVisible(visible)
            }
        }
    }

    //需要实现双层嵌套
    private fun isParentVisible(): Boolean {
        val fragment = parentFragment
        if(fragment is LazyLoadFragment){
            return !fragment.isVisibleShowed
        }
        return requireParentFragment().userVisibleHint
    }

    abstract fun getLayoutId():Int

    abstract fun initView(view:View)

    //懒加载方法
    open fun onLazyLoad(){
        LogUtil.i {
            "onLazyLoad"
        }
    }
    //退出界面懒加载方法
    open fun onStopLazyLoad(){
        LogUtil.i {
            "onStopLazyLoad"
        }
    }

    override fun onStop() {
        super.onStop()
        LogUtil.i {
            "onStop"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.i {
            "onDestroy"
        }
    }
}