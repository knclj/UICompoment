package com.test.applicationlifecompoment_api

import android.content.res.Configuration

interface IApplicationLifeComponent {
    fun onCreate()
    fun onConfigurationChanged(newConfig: Configuration)
    fun onLowMemory()
}