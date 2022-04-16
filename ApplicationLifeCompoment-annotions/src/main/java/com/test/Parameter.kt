package com.test

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class Parameter(val name:String = "")
