package sank.xbook.model.user.register

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.MyApp.Companion.USERBSEE


interface IRegisterModel {
    fun loadData(account: String,
                 password: String,
                 name: String,
                 gender: String,
                 mail: String,
                 onLoadCompleteListener: OnLoadCompleteListener)

    interface OnLoadCompleteListener{
        fun onComplete(data: RegisterBean)
        fun onFailure()
    }
}

interface RegisterAPI {
    @GET("create")
    fun login(@Query("account") account: String,
              @Query("password") password: String,
              @Query("name") name: String,
              @Query("gender") gender: String,
              @Query("mail") mail: String)
            : Call<RegisterBean>
}

class RegisterModel : IRegisterModel {
    override fun loadData(account: String, password: String, name: String, gender: String, mail: String, onLoadCompleteListener: IRegisterModel.OnLoadCompleteListener) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(USERBSEE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(RegisterAPI::class.java)
        val call = api.login(account, password, name, gender, mail)
        call.enqueue(object : Callback<RegisterBean>{
            override fun onFailure(call: Call<RegisterBean>, t: Throwable) {
                onLoadCompleteListener.onFailure()
            }
            override fun onResponse(call: Call<RegisterBean>, response: Response<RegisterBean>) {
                response.body()?.let {
                    onLoadCompleteListener.onComplete(it)
                }
            }
        })
    }
}
