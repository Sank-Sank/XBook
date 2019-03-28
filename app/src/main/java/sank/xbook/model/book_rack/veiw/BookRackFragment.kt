package sank.xbook.model.book_rack.veiw

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import org.greenrobot.eventbus.EventBus
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.R
import sank.xbook.base.*
import sank.xbook.model.book_rack.presenter.PBookRack
import sank.xbook.model.read_book.view.ReadActivity
import sank.xbook.model.search_book.SearchActivity

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/13
 */
class BookRackFragment : BaseFragment(),IView {
    private lateinit var views:View
    private lateinit var menu:ImageView
    private lateinit var SearchBook:ImageView
    private lateinit var bookRockRecycler: RecyclerView

    private var bookItem:MutableList<BookBean> = ArrayList()
    private lateinit var bookRockAdapter:BookRockAdapter

    private var p:IPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.framnent_book_rack, container, false)
        initView()
        return views
    }

    private fun initView(){
        p = PBookRack(this)
        menu = views.findViewById(R.id.menu)
        SearchBook = views.findViewById(R.id.SearchBook)
        menu.setOnClickListener {
            EventBus.getDefault().post(DrawerLayoutOpen())
        }
        SearchBook.setOnClickListener {
            contexts?.startActivity(Intent(contexts,SearchActivity::class.java))
        }
        bookRockRecycler = views.findViewById(R.id.bookRockRecycler)
        bookRockRecycler.layoutManager = LinearLayoutManager(contexts)
        bookRockRecycler.addItemDecoration(DividerItemDecoration(contexts, DividerItemDecoration.VERTICAL))
        bookRockAdapter= BookRockAdapter(contexts!!, bookItem, object : OnItemClickListeners {
            override fun onItemClicked(view: View, position: Int) {
                startActivity(Intent(contexts,ReadActivity::class.java).apply {
                    putExtra("bookName",bookItem[position].bookname)
                })
            }
        })
        bookRockRecycler.adapter = bookRockAdapter
        if(p!=null){
            p!!.requestNet()
        }
    }

    override fun onSuccess(data:BookBean) {
        bookItem.add(data)
        bookRockAdapter.notifyDataSetChanged()
    }

    override fun onFailure() {

    }

    /**
     * 每次hide show的时候都会调用
     */
    override fun onHiddenChanged(hidden: Boolean) {
        Log.e("TAG","BookRack->onHiddenChanged")
        super.onHiddenChanged(hidden)
    }

    override fun onDestroyView() {
        if(p != null){
            p = null
        }
        super.onDestroyView()
    }

}