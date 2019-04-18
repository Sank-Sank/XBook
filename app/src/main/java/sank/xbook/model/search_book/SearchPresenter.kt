package sank.xbook.model.search_book

import sank.xbook.base.BasePresenter
import sank.xbook.base.BookBean
import sank.xbook.base.IView

class SearchPresenter(iSearchView: ISearchView) : BasePresenter<SearchPresenter.ISearchView>(){
    private var iSearchView: ISearchView? = null
    private var iSearchModel : ISearchModel? = null

    init {
        this.iSearchView = iSearchView
        iSearchModel = SearchModel()
    }

    fun fetch(name : String){
        iSearchModel?.loadData(name,object : ISearchModel.OnLoadCompleteListener{
            override fun onComplete(data: BookBean) {
                iSearchView?.onSearchSuccess(data)
            }
            override fun onFailure() {
                iSearchView?.onSearchFailure()
            }
        })
    }

    interface ISearchView : IView{
        fun onSearchSuccess(data : BookBean)
        fun onSearchFailure()
    }
}