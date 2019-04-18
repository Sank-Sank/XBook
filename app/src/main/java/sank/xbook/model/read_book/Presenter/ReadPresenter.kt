package sank.xbook.model.read_book.Presenter

import sank.xbook.base.BasePresenter
import sank.xbook.base.ChaptersBean
import sank.xbook.base.IView
import sank.xbook.model.read_book.model.IReadModel
import sank.xbook.model.read_book.model.ReadModel


class ReadPresenter(iReadView :IReadView) : BasePresenter<ReadPresenter.IReadView>() {
    private var iReadView : IReadView? = null
    private var model: IReadModel? = null

    init {
        this.iReadView = iReadView
        model = ReadModel()
    }

    fun fetch(bookName:String){
        model?.loadData(bookName,object : IReadModel.OnLoadCompleteListener{
            override fun onFailure() {
                iReadView?.requestFailure()
            }
            override fun onComplete(data: ChaptersBean) {
                iReadView?.requestSuccess(data)
            }
        })
    }

    interface IReadView : IView{
        fun requestSuccess(data: ChaptersBean)
        fun requestFailure()
    }
}