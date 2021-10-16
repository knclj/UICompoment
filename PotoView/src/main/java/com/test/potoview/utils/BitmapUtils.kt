package com.test.potoview.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue

object BitmapUtils {


    fun dpToPix(value: Float):Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,value,Resources.getSystem().displayMetrics)
    }

    fun getBitmapFromRes(res:Resources,id:Int,width:Float):Bitmap?{
       var options:BitmapFactory.Options  = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, id,options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width.toInt()
        return BitmapFactory.decodeResource(res, id,options)
    }

}