package com.test.uicompoment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.test.constants.Constants
import com.test.main.LazyLoadActivity
import com.test.main.MainVPActivity
import com.test.main.MyLazyLoadActivity
import com.test.potoview.PhotoViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       findViewById<Button>(R.id.btn_flowlayout).setOnClickListener {
           val intent = Intent(this,FlowLayoutActity::class.java)
           intent.putExtra("name","Name test")
           intent.putExtra(Constants.IS_BODY_KEY,true)
           this.startActivity(intent)
       }
        findViewById<Button>(R.id.btn_FlexBoxLayout).setOnClickListener {
            val intent = Intent(this,FlexBoxActivity::class.java)
            this.startActivity(intent)
        }

        findViewById<Button>(R.id.btn_touchEvent).setOnClickListener {
            val intent = Intent(this,PhotoViewActivity::class.java)
            this.startActivity(intent)
        }
        findViewById<Button>(R.id.btn_fragment).setOnClickListener {
            val intent = Intent(this,LazyLoadActivity::class.java)
            this.startActivity(intent)
        }

        findViewById<Button>(R.id.btn_LazyLoad).setOnClickListener {
            val intent = Intent(this, MyLazyLoadActivity::class.java)
            this.startActivity(intent)
        }

        findViewById<Button>(R.id.btn_VPFragment).setOnClickListener {
            val intent = Intent(this, MainVPActivity::class.java)
            this.startActivity(intent)
        }
    }
}