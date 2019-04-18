package sank.xbook.model.classify.Details

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import sank.xbook.R
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.base.BaseActivity
import sank.xbook.base.TypeBean1
import sank.xbook.base.TypeBean2
import sank.xbook.model.search_book.SearchActivity

/**
 * 分类详情界面
 */
class ClassifyDetailsActivity : BaseActivity<ClassifyDetailsPresenter, ClassifyDetailsPresenter.IClassifyDetailsView>() , ClassifyDetailsPresenter.IClassifyDetailsView {
    private lateinit var type:String    //类型
    private var page = 1                    //页数
    private var count = 0                   //总页数
    private lateinit var back:ImageView
    private lateinit var SearchBook:ImageView
    private lateinit var classifyName:TextView
    private lateinit var noNet:TextView
    private lateinit var classifyRecycler:RecyclerView
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
        classifyRecycler.layoutManager = LinearLayoutManager(this)
        classifyRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        classifyDetailsAdapter = ClassifyDetailsAdapter(this, bookList!!, object : OnItemClickListeners {
            override fun onItemClicked(view: View, position: Int) {
                //TODO 分类打开书籍
            }
        })
        classifyRecycler.adapter = classifyDetailsAdapter

        mPresenter?.fetch(type,page)
    }

    override fun onTypeSuccess(data: TypeBean1) {
        noNet.visibility = View.GONE
        classifyRecycler.visibility = View.VISIBLE
        count = data.count
        bookList?.addAll(data.pages)
        classifyDetailsAdapter.notifyDataSetChanged()
    }

    override fun onTypeFailure() {
        noNet.visibility = View.VISIBLE
        classifyRecycler.visibility = View.GONE
    }

    override fun onDestroy() {
        if(bookList != null){
            bookList?.clear()
            bookList = null
        }
        super.onDestroy()
    }

}
