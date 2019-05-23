package sank.xbook.model.search_book

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.BookBean
import sank.xbook.base.MyApp.Companion.BASEURL


/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/28
 */
interface ISearchModel{
    fun loadData(name:String ,onLoadCompleteListener: OnLoadCompleteListener)

    interface OnLoadCompleteListener{
        fun onComplete(data: BookBean)
        fun onFailure()
    }
}

interface SearchAPI{
    @GET("search")
    fun getBook(@Query("name")
                name:String): Call<BookBean>
}

class SearchModel : ISearchModel{
    override fun loadData(name: String, onLoadCompleteListener: ISearchModel.OnLoadCompleteListener) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(SearchAPI::class.java)
        val call = api.getBook(name)
        call.enqueue(object : Callback<BookBean> {
            override fun onFailure(call: Call<BookBean>, t: Throwable) {
                onLoadCompleteListener.onFailure()
            }

            override fun onResponse(call: Call<BookBean>, response: Response<BookBean>) {
                response.body()?.let {
                    onLoadCompleteListener.onComplete(it)
                }
            }
        })
    }
}
