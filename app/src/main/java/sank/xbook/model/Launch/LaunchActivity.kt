package sank.xbook.model.Launch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.Window
import sank.xbook.R
import sank.xbook.model.main.MainActivity

class LaunchActivity : AppCompatActivity() {

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg!!.what){
                1 -> {
                    startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
                    this@LaunchActivity.finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(0x00000400, 0x00000400)
        setContentView(R.layout.activity_launch)
        handler.sendEmptyMessageDelayed(1,500)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

}
