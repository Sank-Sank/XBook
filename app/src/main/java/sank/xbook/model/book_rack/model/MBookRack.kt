package sank.xbook.model.book_rack.model

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.BookBean
import sank.xbook.base.IModel
import sank.xbook.base.IPresenter

interface BookAPI{
    @GET("search")
    fun getBook(@Query("name")
                name:String): Call<BookBean>
}

class MBookRack(var p:IPresenter) : IModel{
    override fun startRequestNet() {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://www.xyxhome.cn/book/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(BookAPI::class.java)
        val call = api.getBook("步步莲劫")
        call.enqueue(object : Callback<BookBean> {
            override fun onFailure(call: Call<BookBean>, t: Throwable) {
                p.requestFailure()
            }

            override fun onResponse(call: Call<BookBean>, response: Response<BookBean>) {
                if(response.body() != null){
                    p.requestSuccess(response.body()!!)
                }
            }
        })
    }

}