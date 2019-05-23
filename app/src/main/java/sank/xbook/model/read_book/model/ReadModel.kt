package sank.xbook.model.read_book.model

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.ChapterContentBean
import sank.xbook.base.ChaptersBean


interface IReadModel{
    fun loadData(bookName:String,onLoadCompleteListener: OnLoadCompleteListener)
    fun requestContent(id : Int, onLoadContentCompleteListener: OnLoadContentCompleteListener)

    interface OnLoadCompleteListener{
        fun onComplete(data: ChaptersBean)
        fun onFailure()
    }

    interface OnLoadContentCompleteListener{
        fun onComplete(data: ChapterContentBean)
        fun onFailure()
    }
}

interface ChapterAPI{
    @GET("chapters")
    fun getChapters(@Query("bookName")
                bookName:String): Call<ChaptersBean>
}

internal interface RequestChapterAPI {
    @GET("chaptercontent")
    fun getChapter(@Query("id")
                   id: Int): Call<ChapterContentBean>
}

class ReadModel:IReadModel{
    override fun loadData(bookName: String, onLoadCompleteListener: IReadModel.OnLoadCompleteListener) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://www.xyxhome.cn/book/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(ChapterAPI::class.java)
        val call = api.getChapters(bookName)
        call.enqueue(object : Callback<ChaptersBean> {
            override fun onFailure(call: Call<ChaptersBean>, t: Throwable) {
                onLoadCompleteListener.onFailure()
            }

            override fun onResponse(call: Call<ChaptersBean>, response: Response<ChaptersBean>) {
                response.body()?.let {
                    onLoadCompleteListener.onComplete(it)
                }
            }
        })
    }

    override fun requestContent(id: Int, onLoadContentCompleteListener: IReadModel.OnLoadContentCompleteListener) {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.xyxhome.cn/book/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(RequestChapterAPI::class.java)
        val call = api.getChapter(id)
        call.enqueue(object : Callback<ChapterContentBean> {
            override fun onResponse(call: Call<ChapterContentBean>, response: Response<ChapterContentBean>) {
                response.body()?.let {
                    onLoadContentCompleteListener.onComplete(it)
                }
            }

            override fun onFailure(call: Call<ChapterContentBean>, t: Throwable) {
                onLoadContentCompleteListener.onFailure()
            }
        })
    }
}