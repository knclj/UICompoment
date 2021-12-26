package com.test;

import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.test.applicationlifecompoment_annotions.ApplicationLifeComponent;
import com.test.applicationlifecompoment_api.IApplicationLifeComponent;
import com.test.utils.LogUtil;

@ApplicationLifeComponent
public class LazyLoadApplication implements IApplicationLifeComponent {
    private static final String TAG = "LazyLoadApplication";
    @Override
    public void onCreate() {
        LogUtil.INSTANCE.i(TAG,()->"onCreate");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        LogUtil.INSTANCE.i(TAG,()->"onConfigurationChanged");

    }

    @Override
    public void onLowMemory() {
        LogUtil.INSTANCE.i(TAG,()->"onLowMemory");

    }
}
