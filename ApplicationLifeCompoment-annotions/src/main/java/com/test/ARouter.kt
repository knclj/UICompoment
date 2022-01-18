package com.test
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class ARouter(val path:String,val group:String = "")
