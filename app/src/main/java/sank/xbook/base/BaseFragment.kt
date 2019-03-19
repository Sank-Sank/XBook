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
open class BaseFragment : Fragment(){
    protected var contexts: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("TAG","父类的fragment")
        contexts = context
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        contexts = null
        super.onDestroy()
    }

}