package sank.xbook.model.classify.Details

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.MyApp.Companion.BASEURL
import sank.xbook.base.TypeBean1

interface IClassifyModel{
    fun loadData(type:String,page: Int,onLoadCompleteListener:OnLoadCompleteListener)

    interface OnLoadCompleteListener{
        fun onComplete(data: TypeBean1)
        fun onFailure()
    }
}

interface ClassAPI{
    @GET("type")
    fun getTypeBook(@Query("type") type:String,
                    @Query("page") page:Int): Call<TypeBean1>
}

class ClassifyModel : IClassifyModel {
    override fun loadData(type: String, page: Int , onLoadCompleteListener: IClassifyModel.OnLoadCompleteListener) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(ClassAPI::class.java)
        val call = api.getTypeBook(type, page)
        call.enqueue(object : Callback<TypeBean1>{
            override fun onFailure(call: Call<TypeBean1>, t: Throwable) {
                onLoadCompleteListener.onFailure()
            }
            override fun onResponse(call: Call<TypeBean1>, response: Response<TypeBean1>) {
                response.body()?.let {
                    onLoadCompleteListener.onComplete(it)
                }
            }
        })
    }
}

