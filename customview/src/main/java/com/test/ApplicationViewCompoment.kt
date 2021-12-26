package com.test

import android.content.res.Configuration
import android.util.Log
import com.test.applicationlifecompoment_annotions.ApplicationLifeComponent
import com.test.applicationlifecompoment_api.IApplicationLifeComponent

@ApplicationLifeComponent
class ApplicationViewCompoment :IApplicationLifeComponent{
    companion object{
        const val TAG = "ApplicationVCompoment"
    }
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