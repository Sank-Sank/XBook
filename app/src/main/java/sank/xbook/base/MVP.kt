package sank.xbook.base


/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/11
 */
interface IView {
    fun onSuccess(data:BookBean)
    fun onFailure()
}

interface IPresenter{
    fun requestNet()
    fun requestSuccess(data:BookBean)
    fun requestFailure()
}

interface IModel{
    fun startRequestNet()
}