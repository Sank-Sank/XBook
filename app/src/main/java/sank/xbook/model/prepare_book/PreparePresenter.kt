package sank.xbook.model.prepare_book

import sank.xbook.base.BasePresenter
import sank.xbook.base.IView

class PreparePresenter(iPrepareView: IPrepareView) : BasePresenter<PreparePresenter.IPrepareView>(){
    private var iPrepareView:IPrepareView? = null
    private var iPreparemodel:IPrepareModel? = null

    init {
        this.iPrepareView = iPrepareView
        iPreparemodel = PrepareModel()
    }

    fun fetch(account: String, name: String, author: String, synopsis: String, image: String, updatetime: String){
        iPreparemodel?.loadData(account,name,author,synopsis,image,updatetime,object : IPrepareModel.OnLoadCompleteListener {
            override fun onFailure() {
                iPrepareView?.onAddFailure()
            }

            override fun onComplete(data: PrepareBean) {
                iPrepareView?.onAddSuccess(data)
            }
        })
    }

    interface IPrepareView : IView{
        fun onAddSuccess(data:PrepareBean)
        fun onAddFailure()
    }
}
