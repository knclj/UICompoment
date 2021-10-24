package com.test.utils

import android.util.Log

/**
 * 工具类
 */
object LogUtil {

    fun i(tag:String = "LazyLoad",print:()->String){
        Log.d(tag,print())
    }

}

