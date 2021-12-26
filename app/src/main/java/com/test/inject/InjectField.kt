package com.test.inject

import android.app.Activity
import com.test.annotation.Autowired

object InjectField {

    /**
     *注入字段复制
     */
    fun injectFiled(activity:Activity){
      val clzz =   activity.javaClass
        clzz.declaredFields.forEach { field ->
            if(field.isAnnotationPresent(Autowired::class.java)){
               val autowired  =  field.getAnnotation(Autowired::class.java)
                var key  = autowired?.value
                if(key?.isEmpty()!!){
                    //默认域名key
                    key = field.name
                }
               var value =  activity.intent.extras?.get(key)
                field.isAccessible = true
                field.set(activity,value)
            }
        }
    }

}