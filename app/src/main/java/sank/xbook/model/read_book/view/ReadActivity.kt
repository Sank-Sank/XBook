package sank.xbook.model.read_book.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import sank.xbook.R
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.base.BaseActivity
import sank.xbook.base.ChaptersBean
import sank.xbook.base.ChaptersDetailsBean
import sank.xbook.model.read_book.Presenter.IPReadActivity
import sank.xbook.model.read_book.Presenter.PReadActivity
import sank.xbook.model.read_book.page.PageLoader
import sank.xbook.model.read_book.page.PageView

interface IReadActivity{
    fun onSuccess(data:ChaptersBean)
    fun onFailure()
}

class ReadActivity : BaseActivity() , IReadActivity{

    private var bookName:String? = null
    private lateinit var pageView: PageView
    private lateinit var book_name:TextView
    private lateinit var chapter_sum:TextView
    private lateinit var chapters:RecyclerView

    private var chaptersList:MutableList<ChaptersDetailsBean>? = null
    private var chaptersAdapter:ChaptersAdapter? = null

    private var p: IPReadActivity? = null

    private lateinit var mPageLoader: PageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        initView()
    }

    private fun initView(){
        bookName = intent.getStringExtra("bookName")
        p = PReadActivity(this)
        pageView = findViewById(R.id.pageView)
        book_name = findViewById(R.id.book_name)
        chapters = findViewById(R.id.chapters)
        chapter_sum = findViewById(R.id.chapter_sum)
        chaptersList= ArrayList()
        chapters.layoutManager = LinearLayoutManager(this)
        //chapters.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        chaptersAdapter = ChaptersAdapter(this,chaptersList!!,object : OnItemClickListeners{
            override fun onItemClicked(view: View, position: Int) {
                Toast.makeText(this@ReadActivity,"点击",Toast.LENGTH_SHORT).show()
            }
        })
        chapters.adapter = chaptersAdapter
        bookName?.let {
            p?.requestNet(it)
        }

        mPageLoader = pageView.pageLoader
        pageView.setTouchListener(object : PageView.TouchListener {
            override fun center() {

            }

            override fun onTouch(): Boolean {
                return true
            }

            override fun prePage(): Boolean {
                return true
            }

            override fun nextPage(): Boolean {
                return true
            }

            override fun cancel() {

            }
        })
        mPageLoader.setOnPageChangeListener(object : PageLoader.OnPageChangeListener{
            override fun onChapterChange(pos: Int) {

            }

            override fun onLoadChapter(chapters: MutableList<ChaptersDetailsBean>?, pos: Int) {

            }

            override fun onCategoryFinish(chapters: MutableList<ChaptersDetailsBean>?) {

            }

            override fun onPageCountChange(count: Int) {

            }

            override fun onPageChange(pos: Int) {

            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(data: ChaptersBean) {
        book_name.text = bookName
        chaptersList?.addAll(data.chapters)
        chaptersAdapter?.notifyDataSetChanged()
        chapter_sum.text = "共${chaptersList?.size}章"
        mPageLoader.openBook(data.chapters)
        mPageLoader.openChapter(data.chapters[0])
    }

    override fun onFailure() {

    }

    override fun onDestroy() {
        mPageLoader.closeBook()
        super.onDestroy()
    }

}
