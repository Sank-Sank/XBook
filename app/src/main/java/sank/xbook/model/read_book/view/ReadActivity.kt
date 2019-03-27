package sank.xbook.model.read_book.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import sank.xbook.R
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.base.BaseActivity
import sank.xbook.base.ChapterContentBean
import sank.xbook.base.ChaptersBean
import sank.xbook.base.ChaptersDetailsBean
import sank.xbook.model.read_book.Presenter.IPReadActivity
import sank.xbook.model.read_book.Presenter.PReadActivity
import sank.xbook.model.read_book.model.IRequestChapterContent
import sank.xbook.model.read_book.page.NetPageLoad
import sank.xbook.model.read_book.page.PageView
import sank.xbook.model.read_book.model.RequestChapterContent

interface IReadActivity{
    fun onSuccess(data:ChaptersBean)
    fun onFailure()
}

interface IChapterContent{
    fun onRequestChapterContentSuccess(data: ChapterContentBean)
    fun onRequestChapterContentFailure()
}

class ReadActivity : BaseActivity() , IReadActivity , IChapterContent{

    companion object {
        var chapterPosition: Int = 1
    }

    private var bookName:String? = null
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var left_layout: LinearLayout
    private lateinit var pageView: PageView
    private lateinit var book_name:TextView
    private lateinit var chapter_sum:TextView
    private lateinit var chapters:RecyclerView

    private var chaptersList:MutableList<ChaptersDetailsBean>? = null
    private var chaptersAdapter:ChaptersAdapter? = null
    //网络请求
    private var p: IPReadActivity? = null
    private var iRequestChapterContent : IRequestChapterContent? = null

    private lateinit var mPageLoader: NetPageLoad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        initView()
    }

    private fun initView(){
        bookName = intent.getStringExtra("bookName")
        p = PReadActivity(this)
        drawerLayout = findViewById(R.id.drawerLayout)
        left_layout = findViewById(R.id.left_layout)
        pageView = findViewById(R.id.pageView)
        book_name = findViewById(R.id.book_name)
        chapters = findViewById(R.id.chapters)
        chapter_sum = findViewById(R.id.chapter_sum)
        chaptersList= ArrayList()
        chapters.layoutManager = LinearLayoutManager(this)
        chapters.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        chaptersAdapter = ChaptersAdapter(this,chaptersList!!,object : OnItemClickListeners{
            override fun onItemClicked(view: View, position: Int) {
                chapterPosition = position + 1
                iRequestChapterContent?.startRequest(chaptersList!![position].id)
                Log.e("TAG","${chaptersList!![position].id}")
                drawerLayout.closeDrawer(left_layout)
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
        mPageLoader.setOnLoadChapterContentListener(object : NetPageLoad.OnLoadChapterContentListener{
            override fun onLoadChapter() {
                iRequestChapterContent?.startRequest(chaptersList!![chapterPosition -1].id)
            }
        })
        iRequestChapterContent = RequestChapterContent(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(data: ChaptersBean) {
        book_name.text = bookName
        chaptersList?.addAll(data.chapters)
        chaptersAdapter?.notifyDataSetChanged()
        chapter_sum.text = "共${chaptersList?.size}章"
        mPageLoader.initBook(data.chapters)
        iRequestChapterContent?.startRequest(chaptersList!![0].id)
    }

    override fun onFailure() {

    }

    override fun onRequestChapterContentSuccess(data: ChapterContentBean) {
        val chapterContentList = mPageLoader.loadChapter(data.title,data.content)
        mPageLoader.openChapter(chapterContentList)
    }

    override fun onRequestChapterContentFailure() {
        Log.e("TAG","请求失败")
    }


    override fun onDestroy() {
        mPageLoader.cloneBook()
        if(chaptersList != null){
            chaptersList = null
        }
        if(iRequestChapterContent != null){
            iRequestChapterContent = null
        }
        if(p != null){
            p = null
        }
        super.onDestroy()
    }

}
