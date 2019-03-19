package sank.xbook.model.main.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import sank.xbook.base.BaseActivity
import sank.xbook.R

class LaunchActivity : BaseActivity() {

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
        window.setFlags(0x00000400, 0x00000400)
        setContentView(R.layout.activity_launch)
        handler.sendEmptyMessageDelayed(1,3000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

}
