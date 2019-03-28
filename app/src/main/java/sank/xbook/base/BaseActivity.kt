package sank.xbook.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.os.Build
import android.view.View


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(){

    companion object {
        const val BASEURL = "http://www.xyxhome.cn/book/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }



}