package sank.xbook.model.prepare_book

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.BookBean
import sank.xbook.base.MyApp.Companion.USERBSEE

data class PrepareBean(var status:Int,var message:String)

interface IPrepareModel{
    fun loadData(account:String ,
            name:String ,
            author:String ,
            synopsis:String ,
            image:String ,
            updatetime:String ,
            onLoadCompleteListener: OnLoadCompleteListener)

    interface OnLoadCompleteListener{
        fun onComplete(data : PrepareBean)
        fun onFailure()
    }
}

interface PreAPI{
    @GET("addbook")
    fun addBook(@Query("account") account:String,
                @Query("name") name:String,
                @Query("author") author:String,
                @Query("synopsis") synopsis:String,
                @Query("image") image:String,
                @Query("updatetime") updatetime:String): Call<PrepareBean>
}

class PrepareModel : IPrepareModel{
    override fun loadData(account: String, name: String, author: String, synopsis: String, image: String, updatetime: String, onLoadCompleteListener: IPrepareModel.OnLoadCompleteListener) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(USERBSEE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(PreAPI::class.java)
        val call = api.addBook(account,name,author,synopsis,image, updatetime)
        call.enqueue(object : Callback<PrepareBean> {
            override fun onFailure(call: Call<PrepareBean>, t: Throwable) {
                onLoadCompleteListener.onFailure()
            }

            override fun onResponse(call: Call<PrepareBean>, response: Response<PrepareBean>) {
                response.body()?.let {
                    onLoadCompleteListener.onComplete(it)
                }
            }
        })
    }
}