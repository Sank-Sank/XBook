package sank.xbook.model.book_rack.presenter

import sank.xbook.base.BasePresenter
import sank.xbook.base.BookBean
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

    fun fetch(){
        model?.loadData(object : IBookRackModel.OnLoadCompleteListener{
            override fun onFailure() {
                iBookRackView?.onFailure()
            }

            override fun onComplete(data: BookBean) {
                iBookRackView?.onSuccess(data)
            }
        })
    }

    interface IBookRackView : IView{
        fun onSuccess(data:BookBean)
        fun onFailure()
    }
}