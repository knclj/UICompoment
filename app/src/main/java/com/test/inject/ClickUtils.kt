package com.test.inject

import android.app.Activity
import android.view.View
import com.test.annotation.OnClick

object ClickUtils {

    fun bind(activity: Activity){
        val clzz =   activity.javaClass
        clzz.declaredMethods.forEach { method ->
            if(method!= null && method.isAnnotationPresent(OnClick::class.java)){
              val click:OnClick = method.getAnnotation(OnClick::class.java)
               val ids =  click.value
                ids.forEach { id->
                    val view =  activity.findViewById<View>(id)
                    view.setOnClickListener {
                        method.invoke(activity,it)
                    }
                }
            }
        }
    }
}