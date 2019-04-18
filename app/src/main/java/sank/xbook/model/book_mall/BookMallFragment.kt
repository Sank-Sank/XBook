package sank.xbook.model.book_mall

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import sank.xbook.base.BaseFragment
import sank.xbook.R

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/13
 */
class BookMallFragment : BaseFragment<BookMallPresenter, BookMallPresenter.IBookMallViewIView>(){
    private lateinit var views:View
    private lateinit var viewPager:ViewPager
    private lateinit var radioGroup:RadioGroup

    override fun createPresenter(): BookMallPresenter = BookMallPresenter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.framnent_book_mall,container,false)
        initView()
        return views
    }

    private fun initView(){



    }

    /**
     * 每次show hide的是调用
     */
    override fun onHiddenChanged(hidden: Boolean) {
        Log.e("TAG","BookMall->onHiddenChanged")
        super.onHiddenChanged(hidden)
    }


    override fun onDestroy() {

        super.onDestroy()
    }

}