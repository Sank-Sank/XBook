package sank.xbook.model.book_mall

import sank.xbook.base.BasePresenter
import sank.xbook.base.BookMallBean1
import sank.xbook.base.IView

class BookMallPresenter(iView:IBookMallViewView) : BasePresenter<BookMallPresenter.IBookMallViewView>(){
    private var iBookRackView: IBookMallViewView? = null
    private var model: IBookMallModel? = null

    init {
        this.iBookRackView = iView
        model = BookMallModel()
    }

    fun fetch(){
        model?.loadData(object : IBookMallModel.OnLoadCompleteListener{
            override fun onComplete(data: BookMallBean1) {
                iBookRackView?.onSuccess(data)
            }

            override fun onFailure() {
                iBookRackView?.onFailure()
            }
        })
    }

    interface IBookMallViewView : IView {
        fun onSuccess(data: BookMallBean1)
        fun onFailure()
    }
}