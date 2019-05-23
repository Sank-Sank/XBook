package sank.xbook.model.book_mall

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import org.greenrobot.eventbus.EventBus
import sank.xbook.base.BaseFragment
import sank.xbook.R
import sank.xbook.Utils.view.RecyclerView.DefaultLoadCreator
import sank.xbook.Utils.view.RecyclerView.DefaultRefreshCreator
import sank.xbook.Utils.view.RecyclerView.LoadRefreshRecyclerView
import sank.xbook.Utils.view.RecyclerView.RefreshRecyclerView
import sank.xbook.base.BookMallBean1
import sank.xbook.base.BookMallBean2
import sank.xbook.base.DrawerLayoutOpen
import sank.xbook.model.search_book.SearchActivity

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/13
 */
class BookMallFragment : BaseFragment<BookMallPresenter, BookMallPresenter.IBookMallViewView>(), View.OnClickListener , BookMallPresenter.IBookMallViewView{
    private lateinit var views:View
    private lateinit var menu:ImageView
    private lateinit var search_ll:LinearLayout
    private lateinit var bookMallRecycler: RecyclerView
    private lateinit var bookMallAdapter: BookMallAdapter
    private var list:MutableList<BookMallBean2>? = null

    override fun createPresenter(): BookMallPresenter = BookMallPresenter(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.framnent_book_mall,container,false)
        initView()
        return views
    }

    private fun initView(){
        list = ArrayList()
        menu = views.findViewById(R.id.menu)
        menu.setOnClickListener(this)
        search_ll = views.findViewById(R.id.search_ll)
        search_ll.setOnClickListener(this)
        bookMallRecycler = views.findViewById(R.id.bookMallRecycler)
//        bookMallRecycler.addRefreshViewCreator(DefaultRefreshCreator())
//        bookMallRecycler.addLoadViewCreator(DefaultLoadCreator())
//        bookMallRecycler.setOnRefreshListener(this)
//        bookMallRecycler.setOnLoadMoreListener(this)
//        bookMallRecycler.addLoadingView(views.findViewById(R.id.loading))
        bookMallRecycler.layoutManager = LinearLayoutManager(contexts)
        bookMallRecycler.addItemDecoration(DividerItemDecoration(contexts, DividerItemDecoration.VERTICAL))
        bookMallAdapter = BookMallAdapter(contexts!!,list!!)
        bookMallRecycler.adapter = bookMallAdapter
        mPresenter?.fetch()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.menu -> {
                EventBus.getDefault().post(DrawerLayoutOpen())
            }
            R.id.search_ll -> {
                contexts?.startActivity(Intent(contexts, SearchActivity::class.java))
            }
        }
    }

    override fun onSuccess(data: BookMallBean1) {
        if(data.status==0) {
            list?.addAll(data.data)
        }
    }

    override fun onFailure() {

    }

//    override fun onLoad() {
//
//    }
//
//    override fun onRefresh() {
//
//    }

    override fun onDestroy() {
        list?.let {
            it.clear()
            list = null
        }
        super.onDestroy()
    }

}