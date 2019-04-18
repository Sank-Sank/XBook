package sank.xbook.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window


@SuppressLint("Registered")
abstract class BaseActivity<P : BasePresenter<V>,V : IView> : AppCompatActivity(), IView{
    /**
     * p层引用
     */
    protected var mPresenter: P? = null

    /**
     * 创建该Activity的Presenter
     */
    protected abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        if (mPresenter != null) {
            mPresenter?.detachView()
            mPresenter = null
        }
        super.onDestroy()
    }
}