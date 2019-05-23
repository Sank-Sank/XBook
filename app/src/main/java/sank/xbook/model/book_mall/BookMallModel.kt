package sank.xbook.model.book_mall

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import sank.xbook.base.BookMallBean1
import sank.xbook.base.MyApp.Companion.BASEURL

interface IBookMallModel{
    fun loadData(onLoadCompleteListener:OnLoadCompleteListener)

    interface OnLoadCompleteListener{
        fun onComplete(data: BookMallBean1)
        fun onFailure()
    }
}

interface BookMallAPI{
    @GET("mall")
    fun getMall(): Call<BookMallBean1>
}

class BookMallModel : IBookMallModel{
    override fun loadData(onLoadCompleteListener: IBookMallModel.OnLoadCompleteListener) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(BookMallAPI::class.java)
        val call = api.getMall()
        call.enqueue(object : Callback<BookMallBean1>{
            override fun onFailure(call: Call<BookMallBean1>, t: Throwable) {
                onLoadCompleteListener.onFailure()
            }

            override fun onResponse(call: Call<BookMallBean1>, response: Response<BookMallBean1>) {
                response.body()?.let {
                    onLoadCompleteListener.onComplete(it)
                }
            }
        })
    }
}
