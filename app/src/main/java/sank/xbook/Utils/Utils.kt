package sank.xbook.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*
import android.net.NetworkInfo


object Utils {
    //将当前时间转换成对应的格式
    @SuppressLint("SimpleDateFormat")
    fun dateConvert(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(date)
    }

    /**
     * 检查是否有网络
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        if (info != null) {
            return info.isAvailable
        }
        return false
    }
}