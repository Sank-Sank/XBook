package sank.xbook.model.book_rack

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.R
import sank.xbook.base.BaseFragment
import sank.xbook.base.DrawerLayoutOpen

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/13
 */
class BookRackFragment : BaseFragment() {
    private lateinit var views:View
    private lateinit var menu:ImageView
    private lateinit var bookRockRecycler: RecyclerView

    private var bookItem:MutableList<String> =  ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("TAG","BookRack->onCreateView")
        views = inflater.inflate(R.layout.framnent_book_rack, container, false)
        initView()
        return views
    }

    private fun initView(){
        menu = views.findViewById(R.id.menu)
        menu.setOnClickListener {
            EventBus.getDefault().post(DrawerLayoutOpen())
        }
        bookItem.add("1")
        bookItem.add("2")
        bookItem.add("3")
        bookItem.add("4")
        bookItem.add("5")
        bookItem.add("6")
        bookItem.add("7")
        bookItem.add("8")
        bookItem.add("9")
        bookItem.add("10")
        bookItem.add("11")
        bookItem.add("12")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookItem.add("13")
        bookRockRecycler = views.findViewById(R.id.bookRockRecycler)
        bookRockRecycler.layoutManager = LinearLayoutManager(contexts)
        bookRockRecycler.addItemDecoration(DividerItemDecoration(contexts, DividerItemDecoration.VERTICAL))
        bookRockRecycler.adapter = BookRockAdapter(contexts!!,bookItem, object : OnItemClickListeners {
            override fun onItemClicked(view: View, position: Int) {
                Toast.makeText(contexts,"点击",Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * 每次hide show的时候都会调用
     */
    override fun onHiddenChanged(hidden: Boolean) {
        Log.e("TAG","BookRack->onHiddenChanged")
        super.onHiddenChanged(hidden)
    }

    override fun onDestroy() {

        super.onDestroy()
    }

}