package sank.xbook.model.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import sank.xbook.R

class SettingActivity : AppCompatActivity() {
    private lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_setting)
        back = findViewById(R.id.back)
        back.setOnClickListener { this.finish() }
    }
}
