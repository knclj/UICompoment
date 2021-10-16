package com.test.utils

import android.content.res.Resources
import android.util.TypedValue
object DisplayUtils {
    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
    fun px2dp(dp:Int):Int{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}