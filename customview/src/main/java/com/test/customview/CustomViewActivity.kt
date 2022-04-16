package com.test.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.ARouter
import com.test.AutoCreate
import com.test.Parameter

@ARouter(path = "/customview/CustomViewActivity")
class CustomViewActivity : AppCompatActivity() {

    @Parameter
    var name:String? = ""
    @Parameter
    var age:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
    }
}