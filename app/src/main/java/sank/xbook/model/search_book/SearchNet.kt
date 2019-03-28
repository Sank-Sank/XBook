package sank.xbook.model.search_book

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.BaseActivity.Companion.BASEURL
import sank.xbook.base.BookBean

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/28
 */

interface ISearchNet{
    fun startRequest(name:String)
}

interface SearchAPI{
    @GET("search")
    fun getBook(@Query("name")
                name:String): Call<BookBean>
}

class SearchNet(var v:ISearchActivity) : ISearchNet{
    override fun startRequest(name:String) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(SearchAPI::class.java)
        val call = api.getBook(name)
        call.enqueue(object : Callback<BookBean> {
            override fun onFailure(call: Call<BookBean>, t: Throwable) {
                v.onSearchFailure()
            }

            override fun onResponse(call: Call<BookBean>, response: Response<BookBean>) {
                response.body()?.let {
                    v.onSearchSuccess(it)
                }
            }
        })
    }
}
