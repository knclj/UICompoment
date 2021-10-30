package com.test.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.test.lazyload.R

class MainVPActivity : AppCompatActivity() {
    private lateinit var viewPager2:ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_vp2)
        viewPager2 = findViewById(R.id.viewPager)
        initViewPager()
    }

    private fun initViewPager() {
        viewPager2.offscreenPageLimit = 2
        val fragments = mutableListOf<Fragment>()
        fragments.add(FirstVPFragment())
        fragments.add(SecondVPFragment())
        fragments.add(ThirdVPFragment())
        fragments.add(FourVPFragment())
        fragments.add(FiveVPFragment())
        val adapter = MyVPFragmentAdapter(fragments,this)
        viewPager2.adapter = adapter

    }

    class MyVPFragmentAdapter(val fragments:List<Fragment>,@NonNull fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {



        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }
}