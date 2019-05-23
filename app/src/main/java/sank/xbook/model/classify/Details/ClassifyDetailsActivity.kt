package sank.xbook.model.classify.Details

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import sank.xbook.R
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.Utils.view.RecyclerView.*
import sank.xbook.base.BaseActivity
import sank.xbook.base.BookBean
import sank.xbook.base.TypeBean1
import sank.xbook.base.TypeBean2
import sank.xbook.model.prepare_book.PrepareActivity
import sank.xbook.model.search_book.SearchActivity

/**
 * 分类详情界面
 */
class ClassifyDetailsActivity : BaseActivity<ClassifyDetailsPresenter, ClassifyDetailsPresenter.IClassifyDetailsView>() , ClassifyDetailsPresenter.IClassifyDetailsView, RefreshRecyclerView.OnRefreshListener, LoadRefreshRecyclerView.OnLoadMoreListener {
    private lateinit var type:String    //类型
    private var page = 1                    //页数
    private var count = 0                   //总页数
    private lateinit var back:ImageView
    private lateinit var SearchBook:ImageView
    private lateinit var classifyName:TextView
    private lateinit var noNet:TextView
    private lateinit var classifyRecycler: LoadRefreshRecyclerView
    private var bookList:MutableList<TypeBean2>? = null
    private lateinit var classifyDetailsAdapter: ClassifyDetailsAdapter

    override fun createPresenter(): ClassifyDetailsPresenter = ClassifyDetailsPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classify_details)
        type = intent.getStringExtra("classifyName")
        initAll()
    }

    private fun initAll(){
        back = findViewById(R.id.back)
        classifyName = findViewById(R.id.classifyName)
        classifyName.text = type
        SearchBook = findViewById(R.id.SearchBook)
        classifyRecycler = findViewById(R.id.classifyRecycler)
        noNet = findViewById(R.id.noNet)
        back.setOnClickListener { this.finish() }
        SearchBook.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        bookList = ArrayList()
        //为adapter设置参数
        classifyRecycler.addRefreshViewCreator(DefaultRefreshCreator())
        classifyRecycler.addLoadViewCreator(DefaultLoadCreator())
        classifyRecycler.setOnRefreshListener(this)
        classifyRecycler.setOnLoadMoreListener(this)
        classifyRecycler.addLoadingView(findViewById(R.id.loading))
        classifyRecycler.layoutManager = LinearLayoutManager(this)
        classifyRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        classifyDetailsAdapter = ClassifyDetailsAdapter(this, bookList!!)
        classifyDetailsAdapter.setOnItemClickListener { position ->
            val intent = Intent(this@ClassifyDetailsActivity,PrepareActivity::class.java)
            val b = Bundle()
            //使用的时万能adapter所以position要减一
            val typeBean = bookList!![position -1].fields
            val book = BookBean(0,
                    typeBean.book_name,
                    typeBean.book_type,
                    typeBean.book_author,
                    typeBean.book_synopsis,
                    typeBean.book_image,
                    typeBean.update_time,null)
            b.putSerializable("book",book)
            intent.putExtra("bundle1",b)
            startActivity(intent)
        }
        classifyRecycler.adapter = classifyDetailsAdapter
        mPresenter?.fetch(type,page)
    }

    /**
     * 下拉加载回调
     */
    override fun onLoad() {
        if(page <= count){
            page++
            mPresenter?.fetch(type,page)
        }
    }

    /**
     * 上拉刷新回调
     */
    override fun onRefresh() {
        bookList?.clear()
        mPresenter?.fetch(type,1)
    }

    override fun onTypeSuccess(data: TypeBean1) {
        noNet.visibility = View.GONE
        classifyRecycler.visibility = View.VISIBLE
        count = data.count
        bookList?.addAll(data.pages)
        classifyDetailsAdapter.notifyDataSetChanged()
        classifyRecycler.onStopRefresh()
        classifyRecycler.onStopLoad()
    }

    override fun onTypeFailure() {
        noNet.visibility = View.VISIBLE
        classifyRecycler.visibility = View.GONE
        classifyRecycler.onStopRefresh()
        classifyRecycler.onStopLoad()
    }

    override fun onDestroy() {
        if(bookList != null){
            bookList?.clear()
            bookList = null
        }
        super.onDestroy()
    }

}
