package sank.xbook.model.classify.Details

import sank.xbook.base.BasePresenter
import sank.xbook.base.IView
import sank.xbook.base.TypeBean1

class ClassifyDetailsPresenter(iClassifyDetailsView: IClassifyDetailsView) : BasePresenter<ClassifyDetailsPresenter.IClassifyDetailsView>(){
    private var iClassifyDetailsView: IClassifyDetailsView? = null
    private var iClassifyModel:IClassifyModel? = null

    init {
        this.iClassifyDetailsView = iClassifyDetailsView
        iClassifyModel = ClassifyModel()
    }

    fun fetch(type:String,page: Int){
        iClassifyModel?.loadData(type,page,object : IClassifyModel.OnLoadCompleteListener{
            override fun onComplete(data: TypeBean1) {
                iClassifyDetailsView?.onTypeSuccess(data)
            }

            override fun onFailure() {
                iClassifyDetailsView?.onTypeFailure()
            }
        })
    }

    interface IClassifyDetailsView : IView{
        fun onTypeSuccess(data: TypeBean1)
        fun onTypeFailure()
    }
}