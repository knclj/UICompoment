package com.test.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.ARouter
import com.test.AutoCreate

@ARouter(path = "/customview/CustomViewActivity")
class CustomViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
    }
}