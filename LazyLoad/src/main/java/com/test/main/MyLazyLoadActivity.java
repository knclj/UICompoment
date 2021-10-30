package com.test.main;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.lazyload.R;

import java.util.ArrayList;
import java.util.List;


public class MyLazyLoadActivity extends AppCompatActivity {

    private ViewPager viewPager; // v4
    private BottomNavigationView navigationView; // Tab 5个

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lazy_load);
        viewPager = findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        // ViewPage Adapter 绑定
        MyLazyLoadPagerAdapter adapter = new MyLazyLoadPagerAdapter(getSupportFragmentManager(), getShowData());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(100); // 0没有效果,内部会给你赋值1
        viewPager.setOnPageChangeListener(viewpagerChangeListener);
    }

    // 展示五个Fragment
    private final List<MyLazyLoadFragment> getShowData() {
        List<MyLazyLoadFragment> fragmentList = new ArrayList<>();
        fragmentList.add(MyLazyLoadFragment.newInstance(1));
        fragmentList.add(MyLazyLoadFragment.newInstance(2));
        fragmentList.add(MyLazyLoadFragment.newInstance(3));
        fragmentList.add(MyLazyLoadFragment.newInstance(4));
        fragmentList.add(MyLazyLoadFragment.newInstance(5));
        return fragmentList;
    }

    // tab 名称设置，例如： T1, T2, T3, T4, T5
    private ViewPager.OnPageChangeListener viewpagerChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int i, float v, int i1) {}

        @Override
        public void onPageSelected(int i) {
            int itemId = R.id.fragment_1;
            switch (i) {
                case 0:
                    itemId = R.id.fragment_1;
                    break;
                case 1:
                    itemId = R.id.fragment_2;
                    break;
                case 2:
                    itemId = R.id.fragment_3;
                    break;
                case 3:
                    itemId = R.id.fragment_4;
                    break;
                case 4:
                    itemId = R.id.fragment_5;
                    break;
            }
            navigationView.setSelectedItemId(itemId);
        }

        @Override
        public void onPageScrollStateChanged(int i) {}
    };

    // 当点击 tab1 的时候 就会 设置CurrentItem=0，来设置 ViewPager下标操作
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.fragment_1) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.fragment_2) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.fragment_3) {
                viewPager.setCurrentItem(2, true);
                return true;
            } else if (itemId == R.id.fragment_4) {
                viewPager.setCurrentItem(3, true);
                return true;
            } else if (itemId == R.id.fragment_5) {
                viewPager.setCurrentItem(4, true);
                return true;
            }
            return false;
        }

    };
}
