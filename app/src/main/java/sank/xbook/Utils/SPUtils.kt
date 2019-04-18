package sank.xbook.Utils

import android.content.Context

object SPUtils {
    private const val FILENAME = "UserInfo"

    /**
     * 存放用户名
     */
    fun putUserInfo(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getUserInfo(context: Context, key: String): String? {
        val sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }

    /**
     * 清除所有数据
     */
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        sp.edit().clear().apply()
    }
}