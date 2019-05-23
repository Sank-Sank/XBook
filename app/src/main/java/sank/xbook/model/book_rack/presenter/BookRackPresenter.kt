package sank.xbook.model.book_rack.presenter

import sank.xbook.base.BasePresenter
import sank.xbook.base.BookBean
import sank.xbook.base.BookRackBean1
import sank.xbook.base.IView
import sank.xbook.model.book_rack.model.BookRack
import sank.xbook.model.book_rack.model.IBookRackModel


class BookRackPresenter(iBookRackView: IBookRackView) : BasePresenter<BookRackPresenter.IBookRackView>(){
    private var iBookRackView: IBookRackView? = null
    private var model: IBookRackModel? = null

    init {
        this.iBookRackView = iBookRackView
        model = BookRack()
    }

    fun fetch(account:String){
        model?.loadData(account,object : IBookRackModel.OnLoadCompleteListener{
            override fun onFailure() {
                iBookRackView?.onFailure()
            }

            override fun onComplete(data: BookRackBean1) {
                iBookRackView?.onSuccess(data)
            }
        })
    }

    interface IBookRackView : IView{
        fun onSuccess(data: BookRackBean1)
        fun onFailure()
    }
}