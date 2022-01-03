package com.test.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventType(val listerType:KClass<*>,val listerSetter:String)
