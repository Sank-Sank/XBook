package sank.xbook.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/13
 */
abstract class BaseFragment<P : BasePresenter<V>,V : IView> : Fragment() , IView{
    protected var contexts: Context? = null
    /**
     * Presenter对象
     */
    protected var mPresenter: P? = null

    /**
     * 创建此Fragment使用的Presenter
     */
    protected abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contexts = context
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        if(contexts!= null){
            contexts = null
        }
        if(mPresenter!= null){
            mPresenter?.detachView()
            mPresenter = null
        }
        super.onDestroy()
    }

}