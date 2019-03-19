package sank.xbook.model.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sank.xbook.R
import sank.xbook.base.BaseFragment

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/13
 */
class ClassifyFragment : BaseFragment(){
    private lateinit var views:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.framnent_classify,container,false)

        return views
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }


    override fun onDestroy() {

        super.onDestroy()
    }
}