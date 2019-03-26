package sank.xbook.model.read_book.page

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.ChapterContentBean

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/26
 */
internal interface RequestChapterAPI {
    @GET("chaptercontent")
    fun getChapter(@Query("id")
                   id: Int): Call<ChapterContentBean>
}


interface IRequestChapterContent{
    fun startRequest(id : Int)
}


class RequestChapterContent(var view : IPageView) : IRequestChapterContent{
    override fun startRequest(id : Int) {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.xyxhome.cn/book/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(RequestChapterAPI::class.java)
        val call = api.getChapter(id)
        call.enqueue(object : Callback<ChapterContentBean> {
            override fun onResponse(call: Call<ChapterContentBean>, response: Response<ChapterContentBean>) {
                view.onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<ChapterContentBean>, t: Throwable) {
                view.onFailure()
            }
        })
    }
}