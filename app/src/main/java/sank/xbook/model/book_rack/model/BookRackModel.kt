package sank.xbook.model.book_rack.model

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.BookRackBean1
import sank.xbook.base.MyApp.Companion.USERBSEE


interface IBookRackModel{
    fun loadData(account:String,onLoadCompleteListener:OnLoadCompleteListener)

    interface OnLoadCompleteListener{
        fun onComplete(data: BookRackBean1)
        fun onFailure()
    }
}

interface BookAPI{
    @GET("searchbook")
    fun getBook(@Query("account")
                account:String): Call<BookRackBean1>
}

class BookRack : IBookRackModel{
    override fun loadData(account:String,onLoadCompleteListener: IBookRackModel.OnLoadCompleteListener) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(USERBSEE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(BookAPI::class.java)
        val call = api.getBook(account)
        call.enqueue(object : Callback<BookRackBean1> {
            override fun onFailure(call: Call<BookRackBean1>, t: Throwable) {
                onLoadCompleteListener.onFailure()
            }

            override fun onResponse(call: Call<BookRackBean1>, response: Response<BookRackBean1>) {
                response.body()?.let {
                    onLoadCompleteListener.onComplete(it)
                }
            }
        })
    }
}