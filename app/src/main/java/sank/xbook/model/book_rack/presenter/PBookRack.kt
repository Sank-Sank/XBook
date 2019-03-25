package sank.xbook.model.book_rack.presenter

import sank.xbook.base.BookBean
import sank.xbook.base.IModel
import sank.xbook.base.IPresenter
import sank.xbook.base.IView
import sank.xbook.model.book_rack.model.MBookRack

class PBookRack(var view:IView) : IPresenter{
    private var model:IModel? = null

    override fun requestNet() {
        model = MBookRack(this)
        model!!.startRequestNet()
    }

    override fun requestSuccess(data: BookBean) {
        view.onSuccess(data)
    }

    override fun requestFailure() {
        view.onFailure()
    }

}