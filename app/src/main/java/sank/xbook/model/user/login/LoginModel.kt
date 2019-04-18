package sank.xbook.model.user.login

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sank.xbook.base.MyApp.Companion.USERBSEE

interface ILoginModel{
    fun loadData(account:String,password:String,onLoadCompleteListener: OnLoadCompleteListener)

    interface OnLoadCompleteListener{
        fun onComplete(data: LoginBean)
        fun onFailure()
    }
}

interface LoginAPI{
    @GET("login")
    fun login(@Query("account") account:String,
                    @Query("password") password:String): Call<LoginBean>
}

class LoginModel : ILoginModel {
    override fun loadData(account: String, password: String, onLoadCompleteListener: ILoginModel.OnLoadCompleteListener) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(USERBSEE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        val api = retrofit.create(LoginAPI::class.java)
        val call = api.login(account, password)
        call.enqueue(object : Callback<LoginBean>{
            override fun onFailure(call: Call<LoginBean>, t: Throwable) {
                onLoadCompleteListener.onFailure()
            }
            override fun onResponse(call: Call<LoginBean>, response: Response<LoginBean>) {
                response.body()?.let{
                    onLoadCompleteListener.onComplete(it)
                }
            }
        })
    }
}