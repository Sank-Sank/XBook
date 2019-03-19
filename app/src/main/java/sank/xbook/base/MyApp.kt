package sank.xbook.base

import android.app.Application

class MyApp :  Application() {
    companion object {
        private var mApplication: MyApp? = null
        fun getInstance() : MyApp?{
            return mApplication
        }
    }
    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }
}