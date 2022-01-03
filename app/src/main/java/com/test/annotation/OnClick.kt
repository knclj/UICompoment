package com.test.annotation

import android.view.View

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@EventType(View.OnClickListener::class,"setOnClickListener")
annotation class OnClick(val value: IntArray = [])
