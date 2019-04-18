package sank.xbook.model.search_book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import sank.xbook.R
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.base.BaseActivity
import sank.xbook.base.BookBean
import sank.xbook.model.read_book.view.ReadActivity
import android.view.inputmethod.InputMethodManager

class SearchActivity : BaseActivity<SearchPresenter, SearchPresenter.ISearchView>(), View.OnClickListener , SearchPresenter.ISearchView {
    private lateinit var voice:ImageView
    private lateinit var searchText:EditText
    private lateinit var searchButton:ImageView
    private lateinit var searchRecyclerView:RecyclerView
    private lateinit var progressBar:ProgressBar
    private lateinit var searchNoData: TextView
    private lateinit var noNet: TextView

    private var adapter:SearchAdapter? = null
    private var bookItem:MutableList<BookBean>? = null

    override fun createPresenter(): SearchPresenter = SearchPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
    }

    private fun initView(){
        voice = findViewById(R.id.voice)
        searchText = findViewById(R.id.searchText)
        searchButton = findViewById(R.id.searchButton)
        searchRecyclerView = findViewById(R.id.searchRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        searchNoData = findViewById(R.id.searchNoData)
        noNet = findViewById(R.id.noNet)

        voice.setOnClickListener(this)
        searchButton.setOnClickListener(this)

        bookItem = ArrayList()
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(this, bookItem!!, object : OnItemClickListeners {
            override fun onItemClicked(view: View, position: Int) {
                val intent = Intent(this@SearchActivity,ReadActivity::class.java).apply {
                    putExtra("bookName",bookItem!![position].bookname)
                }
                startActivity(intent)
            }
        })
        searchRecyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.voice -> {

            }
            R.id.searchButton -> {
                requestBook()
            }
        }
    }

    override fun onSearchSuccess(data : BookBean) {
        if (data.status == 0) {
            bookItem?.clear()
            adapter?.notifyDataSetChanged()
            if (progressBar.visibility == View.VISIBLE)
                progressBar.visibility = View.GONE
            noNet.visibility = View.GONE
            searchNoData.visibility = View.GONE
            searchRecyclerView.visibility = View.VISIBLE
            bookItem?.add(data)
            adapter?.notifyDataSetChanged()
        }else{
            if (progressBar.visibility == View.VISIBLE)
                progressBar.visibility = View.GONE
            searchNoData.visibility = View.VISIBLE
            noNet.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
        }
    }

    override fun onSearchFailure() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(this@SearchActivity.currentFocus.windowToken, 0)
        }
        if(progressBar.visibility == View.VISIBLE)
            progressBar.visibility = View.GONE
        searchRecyclerView.visibility = View.GONE
        searchNoData.visibility = View.GONE
        noNet.visibility = View.VISIBLE
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if(event?.keyCode == KeyEvent.KEYCODE_ENTER){
            requestBook()
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    private fun requestBook() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(this@SearchActivity.currentFocus.windowToken, 0)
        }
        if (progressBar.visibility == View.GONE)
            progressBar.visibility = View.VISIBLE
        val name = searchText.text.toString()
        mPresenter?.fetch(name)
    }

    override fun onDestroy() {
        if(bookItem != null){
            bookItem?.clear()
            bookItem = null
        }
        if(adapter != null){
            adapter = null
        }
        super.onDestroy()
    }
}
