package sank.xbook.model.classify

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import sank.xbook.R
import sank.xbook.base.BookBean
import sank.xbook.model.search_book.SearchActivity

class ClassifyDetailsActivity : AppCompatActivity() {
    /**
     * 什么类型
     */
    private var classify = 0

    private lateinit var back:ImageView
    private lateinit var SearchBook:ImageView
    private lateinit var classifyName:TextView
    private lateinit var classifyRecycler:RecyclerView

    private var bookList:MutableList<BookBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classify_details)
        classify = intent.getIntExtra("classifyName",0)
        showClassify(classify)
        initAll()
    }

    private fun showClassify(c : Int){
        classifyName = findViewById(R.id.classifyName)
        when(c){
            0 -> {
                classifyName.text = "玄幻小说列表"
            }
            1 -> {
                classifyName.text = "修真小说列表"
            }
            2 -> {
                classifyName.text = "都市小说列表"
            }
            3 -> {
                classifyName.text = "穿越小说列表"
            }
            4 -> {
                classifyName.text = "网游小说列表"
            }
            5 -> {
                classifyName.text = "科幻小说列表"
            }
        }
    }

    private fun initAll(){
        back = findViewById(R.id.back)
        SearchBook = findViewById(R.id.SearchBook)
        classifyRecycler = findViewById(R.id.classifyRecycler)
        back.setOnClickListener { this.finish() }
        SearchBook.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        bookList = ArrayList()


    }

    override fun onDestroy() {
        if(bookList != null){
            bookList?.clear()
            bookList = null
        }
        super.onDestroy()
    }

}
