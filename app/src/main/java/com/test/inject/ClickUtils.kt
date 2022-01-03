package com.test.inject

import android.app.Activity
import android.view.View
import com.test.annotation.EventType
import com.test.utils.LogUtil
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object ClickUtils {

    fun bind(activity: Activity){
        val clzz =   activity.javaClass
        clzz.declaredMethods.forEach { method ->
           method.annotations.forEach { annotation ->
                   val annotationType =   annotation.annotationClass
                  if(!annotationType.java.isAnnotationPresent(EventType::class.java)){
                      LogUtil.i {
                          "isAnnotationPresent not isAnnotationPresent"
                      }
                      return@forEach
                  }
               LogUtil.i {
                   "isAnnotationPresent eventType"
               }
                   val eventType =  annotationType.java.getAnnotation(EventType::class.java)
                   val clz =  eventType.listerType
                    val setter = eventType.listerSetter
                    LogUtil.i {
                        "event $clz"
                    }
                   runCatching {
                       val  valueMethod = annotation.javaClass.getMethod("value")
                       valueMethod.isAccessible = true
                       val viewIds = valueMethod.invoke(annotation)
                       val clickProxy = Proxy.newProxyInstance(clz::class.java.classLoader, arrayOf(clz.java),ListenerInvocationHandler<Activity>(activity,method))
                        if(viewIds is IntArray){
                            viewIds.forEach { id->
                               val view:View = activity.findViewById<View>(id) ?: return@forEach
                                val methodView =  view.javaClass.getMethod(setter,clz.java)
                                methodView.invoke(view,clickProxy)
                            }
                        }
                   }.onFailure {
                       LogUtil.i {
                           it.printStackTrace()
                           "failure:"+it.message+""
                       }
                   }.onSuccess {
                       LogUtil.i {
                           "onSuccess"
                       }
                   }
           }
        }
    }


    class ListenerInvocationHandler<T>(val t:T,val m:Method):InvocationHandler{
        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
          return  method?.invoke(t, (args?.get(0)))
        }
    }

}