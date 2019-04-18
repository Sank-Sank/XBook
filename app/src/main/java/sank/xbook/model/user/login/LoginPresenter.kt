package sank.xbook.model.user.login

import sank.xbook.base.BasePresenter
import sank.xbook.base.IView

class LoginPresenter(iLoginView:ILoginView) : BasePresenter<LoginPresenter.ILoginView>(){
    private var iLoginView:ILoginView? = null
    private var modle:ILoginModel? = null

    init {
        this.iLoginView = iLoginView
        modle = LoginModel()
    }

    fun fetch(account:String,password:String){
        modle?.loadData(account,password,object : ILoginModel.OnLoadCompleteListener{
            override fun onFailure() {
                iLoginView?.onRequestFailure()
            }
            override fun onComplete(data: LoginBean) {
                iLoginView?.onRequestSuccess(data)
            }
        })
    }

    interface ILoginView : IView {
        fun onRequestSuccess(data: LoginBean)
        fun onRequestFailure()
    }
}