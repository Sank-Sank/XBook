package sank.xbook.model.user.register

import sank.xbook.base.BasePresenter
import sank.xbook.base.IView

class RegisterPresenter(iRegisterView : IRegisterView) : BasePresenter<RegisterPresenter.IRegisterView>(){
    private var iRegisterView : IRegisterView? = null
    private var iRegisterModel: IRegisterModel? = null

    init {
        this.iRegisterView = iRegisterView
        iRegisterModel = RegisterModel()
    }

    fun fetch(account: String, password: String, name: String, gender: String, mail: String){
        iRegisterModel?.loadData(account,password,name,gender,mail,object : IRegisterModel.OnLoadCompleteListener {
            override fun onComplete(data: RegisterBean) {
                iRegisterView?.onRequestSuccess(data)
            }
            override fun onFailure() {
                iRegisterView?.onRequestFailure()
            }
        })
    }

    interface IRegisterView : IView{
        fun onRequestSuccess(data: RegisterBean)
        fun onRequestFailure()
    }
}