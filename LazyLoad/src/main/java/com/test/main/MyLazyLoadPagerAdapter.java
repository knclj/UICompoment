package com.test.main;


import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

// 同学们： 这就是为了给ViewPager显示的 适配器，来适配显示Fragment的Item页面数据
public class MyLazyLoadPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = MyLazyLoadPagerAdapter.class.getSimpleName();

    private List<MyLazyLoadFragment> fragmentList;

    public MyLazyLoadPagerAdapter(FragmentManager fm, List<MyLazyLoadFragment> flist) {
        super(fm);
        fragmentList = flist;
    }

    @Override
    public MyLazyLoadFragment getItem(int i) {
        Log.v(TAG, "getItem: " + i);
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    // 早期 Android   监听 ViewPager 的 Item  什么时候移除  记录下标
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
