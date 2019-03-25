package sank.xbook.model.read_book.Presenter

import sank.xbook.base.ChaptersBean
import sank.xbook.model.read_book.model.IMReadActivity
import sank.xbook.model.read_book.model.MReadActivity
import sank.xbook.model.read_book.view.IReadActivity

interface IPReadActivity{
    fun requestNet(bookName:String)
    fun requestSuccess(data: ChaptersBean)
    fun requestFailure()
}
class PReadActivity(var view:IReadActivity) : IPReadActivity{
    private var model:IMReadActivity? = null
    override fun requestNet(bookName:String) {
        model = MReadActivity(this)
        model?.startRequestNet(bookName)
    }

    override fun requestSuccess(data: ChaptersBean) {
        view.onSuccess(data)
    }

    override fun requestFailure() {
        view.onFailure()
    }
}