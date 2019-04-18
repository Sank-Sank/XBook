package sank.xbook.model.main

import sank.xbook.base.BasePresenter
import sank.xbook.base.IView

class MainPresenter : BasePresenter<MainPresenter.IMainView>(){


    init {

    }

    interface IMainView : IView{
        fun onRequestSuccess()
        fun onRequestFailure()
    }
}
