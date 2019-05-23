package sank.xbook.model.main.cache

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import sank.xbook.R
import sank.xbook.base.MyApp
import sank.xbook.database.Books

class CacheActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var back: ImageView
    private lateinit var noBook: TextView
//    private lateinit var menu: ImageView
    private lateinit var cacheRecyclerView:RecyclerView
    private lateinit var cacheAdapter: CacheAdapter
    private lateinit var data:MutableList<Books>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_cache)
        initAll()
    }

    private fun initAll(){
        back = findViewById(R.id.back)
        cacheRecyclerView = findViewById(R.id.cacheRecyclerView)
        noBook = findViewById(R.id.noBook)
        back.setOnClickListener(this)
//        menu = findViewById(R.id.menu)
//        menu.setOnClickListener(this)
        cacheRecyclerView.layoutManager = LinearLayoutManager(this)
        cacheRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        data = ArrayList()
        cacheAdapter = CacheAdapter(this,data)
        cacheRecyclerView.adapter = cacheAdapter
        initData()
    }

    private fun initData(){
        val books = MyApp.getGreenDao().booksDao.loadAll()
        if(books.isNotEmpty()){
            cacheRecyclerView.visibility = View.VISIBLE
            noBook.visibility = View.GONE
            data.addAll(books)
            cacheAdapter.notifyDataSetChanged()
        }else{
            cacheRecyclerView.visibility = View.GONE
            noBook.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back -> {this.finish()}
//            R.id.menu -> {
//
//            }
        }
    }

}
