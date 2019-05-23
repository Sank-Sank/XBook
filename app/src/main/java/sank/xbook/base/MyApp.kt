package sank.xbook.base

import android.app.Application
import sank.xbook.database.greendao.DaoMaster
import sank.xbook.database.greendao.DaoSession



class MyApp:  Application() {

    companion object {
        private var mApplication: MyApp? = null
        private var mDaoSession: DaoSession? = null

        const val BASEURL = "http://www.xyxhome.cn/book/"
        const val USERBSEE = "http://www.xyxhome.cn/user/"

        fun getInstance() = mApplication!!
        fun getGreenDao() = mDaoSession!!
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        mDaoSession = initGreenDao()
    }

    private fun initGreenDao():DaoSession{
        val openHelper = DaoMaster.DevOpenHelper(this, "xbook.db")
        val db = openHelper.writableDatabase
        val dm = DaoMaster(db)
        return dm.newSession()
    }

}