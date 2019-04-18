package sank.xbook.base

import java.lang.ref.WeakReference

interface IView

abstract class BasePresenter<V : IView>{
    protected var viewRef:WeakReference<V>? = null

    /**
     * 绑定View
     */
    fun attachView(view: V) {
        viewRef = WeakReference(view)
    }

    /**
     * 获取绑定的view
     */
    fun getView() : V? {
        return if(viewRef == null){
            null
        }else{
            viewRef?.get()
        }
    }

    /**
     * 判断是否绑定了view
     */
    fun isViewAttached(): Boolean {
        return viewRef != null
    }

    /**
     * 解绑
     */
    fun detachView(){
        if(viewRef!=null){
            viewRef?.clear()
            viewRef = null
        }
    }
}