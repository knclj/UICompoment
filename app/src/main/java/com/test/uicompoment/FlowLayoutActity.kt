package com.test.uicompoment

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.test.annotation.Autowired
import com.test.constants.Constants
import com.test.inject.InjectField
import com.test.utils.LogUtil

class FlowLayoutActity : AppCompatActivity() {

    @Autowired(Constants.IS_BODY_KEY)
    private var isBody = false

    @Autowired
    private var name = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_layout_actity)
        setSupportActionBar(findViewById(R.id.toolbar))
        InjectField.injectFiled(this)
        LogUtil.i {
            "body: ${isBody} name: ${name}"
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}