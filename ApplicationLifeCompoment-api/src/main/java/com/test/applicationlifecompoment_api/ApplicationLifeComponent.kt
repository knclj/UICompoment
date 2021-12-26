package com.test.applicationlifecompoment_api

import android.content.res.Configuration
import java.util.*

class ApplicationLifeComponent private constructor():IApplicationLifeComponent{

    companion object{
        val INSTANCE = ApplicationLifeComponent()
    }

    private var loader:ServiceLoader<IApplicationLifeComponent>? = null;

    /**
     * must be run UI Thread.
     */
    fun init(){
        componment()
    }

    private fun componment() {
        loader =  ServiceLoader.load(IApplicationLifeComponent::class.java)
    }

    override fun onCreate() {
        loader?.forEach {
            it.onCreate()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        loader?.forEach {
            it.onConfigurationChanged(newConfig)
        }
    }

    override fun onLowMemory() {
        loader?.forEach {
            it.onLowMemory()
        }
    }

}