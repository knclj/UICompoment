package com.test

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.test.applicationlifecompoment_api.ApplicationLifeComponent

class MyApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ApplicationLifeComponent.INSTANCE.init()
    }
    override fun onCreate() {
        super.onCreate()
        ApplicationLifeComponent.INSTANCE.onCreate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ApplicationLifeComponent.INSTANCE.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        ApplicationLifeComponent.INSTANCE.onConfigurationChanged(newConfig)
    }
}