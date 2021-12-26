package com.test.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Inject
annotation class Autowired(val value:String="" )
