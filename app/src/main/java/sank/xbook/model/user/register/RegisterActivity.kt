package sank.xbook.model.user.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import sank.xbook.R
import sank.xbook.base.BaseActivity
import sank.xbook.base.IView


data class RegisterBean(var status:Int,
                     var message:String)

class RegisterActivity : BaseActivity<RegisterPresenter, RegisterPresenter.IRegisterView>(), View.OnClickListener , RegisterPresenter.IRegisterView {
    private lateinit var back:ImageView
    private lateinit var account: EditText
    private lateinit var password:EditText
    private lateinit var password2:EditText
    private lateinit var name:EditText
    private lateinit var gender:EditText
    private lateinit var mail:EditText
    private lateinit var register:Button
    //默认
    private var mName = "书友"
    private var mGender = "男"
    private var dAccount:String? = null

    override fun createPresenter(): RegisterPresenter = RegisterPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }


    private fun initView(){
        back = findViewById(R.id.back)
        back.setOnClickListener(this)
        account = findViewById(R.id.account)
        password = findViewById(R.id.password)
        password2 = findViewById(R.id.password2)
        name = findViewById(R.id.name)
        gender = findViewById(R.id.gender)
        mail = findViewById(R.id.mail)
        register = findViewById(R.id.register)
        register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back -> { this.finish() }
            R.id.register -> {
                val account_number = account.text.toString()
                dAccount = account_number
                val password_number = password.text.toString()
                val password_number2 = password2.text.toString()
                val _name = name.text.toString()
                val _gender = gender.text.toString()
                val _mail = mail.text.toString()
                if(!_name.isEmpty()){
                    mName = _name
                }
                if(!_gender.isEmpty()){
                    mGender = _gender
                }
                if(account_number.isEmpty() || account_number.length < 6){
                    Toast.makeText(this,"账户名不能小于6位数", Toast.LENGTH_SHORT).show()
                }else if(password_number.isEmpty() || password_number.length < 6){
                    Toast.makeText(this,"密码不能小于6位数", Toast.LENGTH_SHORT).show()
                }else if(password_number.equals(password_number2)){
                    Toast.makeText(this,"两次密码必须一致", Toast.LENGTH_SHORT).show()
                }else if(_mail.isEmpty() || !_mail.contains("@") || _mail.length>20){
                    Toast.makeText(this,"邮箱不合法", Toast.LENGTH_SHORT).show()
                }else {
                    mPresenter?.fetch(account_number,
                            password_number,
                            mName,
                            mGender,
                            _mail)
                }
            }
        }
    }

    override fun onRequestSuccess(data: RegisterBean) {
        when(data.status){
            0 -> {
                Toast.makeText(this, data.message,Toast.LENGTH_SHORT).show()
                setResult(1,Intent().apply {
                    putExtra("account",dAccount)
                })
                this@RegisterActivity.finish()
            }
            1 -> {
                Toast.makeText(this, data.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestFailure() {
        Toast.makeText(this,"注册失败，请检查网络是否链接",Toast.LENGTH_SHORT).show()
    }
}
