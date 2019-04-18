package sank.xbook.base

import android.app.Application

class MyApp :  Application() {

    companion object {
        private var mApplication: MyApp? = null

        const val BASEURL = "http://www.xyxhome.cn/book/"
        const val USERBSEE = "http://www.xyxhome.cn/user/"

        fun getInstance() = mApplication!!
//        fun getInstance() : MyApp?{
//            return mApplication
//        }
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }
}