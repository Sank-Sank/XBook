package sank.xbook.model.book_mall

import sank.xbook.base.BasePresenter
import sank.xbook.base.IView

class BookMallPresenter() : BasePresenter<BookMallPresenter.IBookMallViewIView>(){


    interface IBookMallViewIView : IView {
        fun onSuccess()
        fun onFailure()
    }
}