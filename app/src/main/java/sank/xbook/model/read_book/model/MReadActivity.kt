package sank.xbook.model.read_book.model

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.ChaptersBean
import sank.xbook.model.read_book.Presenter.IPReadActivity

interface IMReadActivity{
    fun startRequestNet(bookName:String)
}

interface ChapterAPI{
    @GET("chapters")
    fun getChapters(@Query("bookName")
                bookName:String): Call<ChaptersBean>
}

class MReadActivity(private var presenter:IPReadActivity):IMReadActivity{
    override fun startRequestNet(bookName:String) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://www.xyxhome.cn/book/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(ChapterAPI::class.java)
        val call = api.getChapters(bookName)
        call.enqueue(object : Callback<ChaptersBean> {
            override fun onFailure(call: Call<ChaptersBean>, t: Throwable) {
                presenter.requestFailure()
            }

            override fun onResponse(call: Call<ChaptersBean>, response: Response<ChaptersBean>) {
                response.body()?.let {
                    presenter.requestSuccess(it)
                }
            }
        })
    }
}