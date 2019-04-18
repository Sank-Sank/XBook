package sank.xbook.model.user.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import sank.xbook.R
import sank.xbook.Utils.SPUtils
import sank.xbook.base.BaseActivity
import sank.xbook.model.user.register.RegisterActivity


data class LoginBean(var status:Int,
                     var message:String)

class LoginActivity : BaseActivity<LoginPresenter, LoginPresenter.ILoginView>() , View.OnClickListener, LoginPresenter.ILoginView {
    private lateinit var back:ImageView
    private lateinit var account: EditText
    private lateinit var password:EditText
    private lateinit var login:Button
    private lateinit var forgetPassword: TextView
    private lateinit var register:TextView
    //p层
    override fun createPresenter(): LoginPresenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initView()
    }

    private fun initView(){
        back = findViewById(R.id.back)
        back.setOnClickListener(this)
        account = findViewById(R.id.account)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        login.setOnClickListener(this)
        forgetPassword = findViewById(R.id.forgetPassword)
        forgetPassword.setOnClickListener(this)
        register = findViewById(R.id.register)
        register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back -> {
                this@LoginActivity.finish()
            }
            R.id.login -> {
                val account_number = account.text.toString()
                val password_number = password.text.toString()
                if(account_number.isEmpty()){
                    Toast.makeText(this@LoginActivity,"账户名不能为空",Toast.LENGTH_SHORT).show()
                }else if(password_number.isEmpty()){
                    Toast.makeText(this@LoginActivity,"密码不能为空",Toast.LENGTH_SHORT).show()
                }
                mPresenter?.fetch(account_number,password_number)
            }
            R.id.forgetPassword -> {

            }
            R.id.register -> {
                startActivityForResult(Intent(this@LoginActivity, RegisterActivity::class.java),1)
            }
        }
    }

    override fun onRequestSuccess(data: LoginBean) {
        when(data.status){
            0 -> {
                Toast.makeText(this, data.message,Toast.LENGTH_SHORT).show()
                SPUtils.putUserInfo(this,"用户名",data.message)
                SPUtils.putUserInfo(this,"密码",data.message)
                this@LoginActivity.finish()
            }
            1 -> {
                Toast.makeText(this,data.message,Toast.LENGTH_SHORT).show()
            }
            2 -> {
                Toast.makeText(this,data.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestFailure() {
        Toast.makeText(this,"登录失败，请检查网络是否链接",Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            val a = data!!.getStringExtra("account").toString()
            account.setText(a)
        }
    }
}
