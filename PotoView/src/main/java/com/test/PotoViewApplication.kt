package com.test

import android.content.res.Configuration
import android.util.Log
import com.test.applicationlifecompoment_annotions.ApplicationLifeComponent
import com.test.applicationlifecompoment_api.IApplicationLifeComponent

@ApplicationLifeComponent
class PotoViewApplication() :IApplicationLifeComponent{
    private val TAG = "PotoViewApplication"
    override fun onCreate() {
        Log.i(TAG,"onCreate")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.i(TAG,"onConfigurationChanged")
    }

    override fun onLowMemory() {
        Log.i(TAG,"onLowMemory")
    }
}