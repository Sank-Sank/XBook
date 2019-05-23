package sank.xbook.model.read_book.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import sank.xbook.R
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.base.BaseActivity
import sank.xbook.base.ChapterContentBean
import sank.xbook.base.ChaptersBean
import sank.xbook.base.ChaptersDetailsBean
import sank.xbook.model.read_book.Presenter.ReadPresenter
import sank.xbook.model.read_book.page.NetPageLoad
import sank.xbook.model.read_book.page.PageView
import android.widget.*
import kotlinx.android.synthetic.main.activity_read.*
import org.w3c.dom.Text
import sank.xbook.model.read_book.Tools.ReadSettingManager
import java.util.*


class ReadActivity : BaseActivity<ReadPresenter, ReadPresenter.IReadView>() , ReadPresenter.IReadView, View.OnClickListener {
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
    //阅读菜单栏
    private lateinit var top_menu: RelativeLayout
    private lateinit var bottom_menu: LinearLayout
    private lateinit var back: ImageView
    private lateinit var left_menu: ImageView
    private lateinit var lastChapter: TextView
    private lateinit var SecondName: TextView           //书名
    private lateinit var nextChapter: TextView
    private lateinit var readSeekBar: SeekBar
    private lateinit var nightOrDayTimeText: TextView
    private lateinit var nightOrDayTimeImage: ImageView
    private lateinit var night_daytime: LinearLayout
    private lateinit var catalog: LinearLayout          //目录
    private lateinit var cache: LinearLayout          //缓存
    private lateinit var settings: LinearLayout          //设置

    private var chaptersList:MutableList<ChaptersDetailsBean>? = null
    private var chaptersAdapter:ChaptersAdapter? = null

    private lateinit var mPageLoader: NetPageLoad

    override fun createPresenter(): ReadPresenter = ReadPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        //设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.BLACK
        showOrHideSystemBar(true)
        initView()
        initData()
    }

    private fun initView(){
        bookName = intent.getStringExtra("bookName")
        drawerLayout = findViewById(R.id.drawerLayout)
        //禁止手势划出
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        left_layout = findViewById(R.id.left_layout)
        pageView = findViewById(R.id.pageView)
        book_name = findViewById(R.id.book_name)
        SecondName = findViewById(R.id.SecondName)
        SecondName.text = bookName
        chapters = findViewById(R.id.chapters)
        chapter_sum = findViewById(R.id.chapter_sum)
        top_menu = findViewById(R.id.top_menu)
        bottom_menu = findViewById(R.id.bottom_menu)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)
        left_menu = findViewById(R.id.left_menu)
        left_menu.setOnClickListener(this)
        lastChapter = findViewById(R.id.lastChapter)
        lastChapter.setOnClickListener(this)
        nextChapter = findViewById(R.id.nextChapter)
        nextChapter.setOnClickListener(this)
        readSeekBar = findViewById(R.id.readSeekBar)
        night_daytime = findViewById(R.id.night_daytime)
        nightOrDayTimeText = findViewById(R.id.nightOrDayTimeText)
        nightOrDayTimeImage = findViewById(R.id.nightOrDayTimeImage)
        night_daytime.setOnClickListener(this)
        catalog = findViewById(R.id.catalog)
        catalog.setOnClickListener(this)
        cache = findViewById(R.id.cache)
        cache.setOnClickListener(this)
        settings = findViewById(R.id.settings)
        settings.setOnClickListener(this)

        chaptersList= ArrayList()
        chapters.layoutManager = LinearLayoutManager(this)
        chapters.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        chaptersAdapter = ChaptersAdapter(this,chaptersList!!,object : OnItemClickListeners{
            override fun onItemClicked(view: View, position: Int) {
                chapterPosition = position + 1
                mPresenter?.getContent(chaptersList!![position].id)
                drawerLayout.closeDrawer(left_layout)
            }
        })
        chapters.adapter = chaptersAdapter
        bookName?.let {
            mPresenter?.fetch(it)
        }

        mPageLoader = pageView.pageLoader
        pageView.setTouchListener(object : PageView.TouchListener {
            override fun center() {
                hideOrShowMenu()
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
                mPresenter?.getContent(chaptersList!![chapterPosition -1].id)
            }
        })
    }

    private fun initData(){
        showNightOrDayTime(ReadSettingManager.getInstance().isNightMode)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back -> {this.finish()}
            R.id.left_menu -> {     //左侧菜单栏
                Toast.makeText(this,"章节位置：${mPageLoader.getChapterPos()}，章节页码：${mPageLoader.getPagePos()}" +
                        "，当前是否是夜间模式${ReadSettingManager.getInstance().isNightMode}",Toast.LENGTH_SHORT).show()
            }
            R.id.lastChapter -> {      //上一章
                mPageLoader.skipPreChapter()
            }
            R.id.nextChapter -> {       //下一章
                mPageLoader.skipNextChapter()
            }
            R.id.night_daytime -> {     //夜间模式—白天模式
                if(ReadSettingManager.getInstance().isNightMode){
                    mPageLoader.setNightMode(false)
                    showNightOrDayTime(false)
                }else{
                    mPageLoader.setNightMode(true)
                    showNightOrDayTime(true)
                }
                hideOrShowMenu()
            }
            R.id.catalog -> {           //目录
                hideOrShowMenu()
                if(drawerLayout.isDrawerOpen(left_layout)){
                    drawerLayout.closeDrawer(left_layout)
                }else{
                    drawerLayout.openDrawer(left_layout)
                }
            }
            R.id.cache -> {             //缓存

            }
            R.id.settings -> {          //设置

            }
        }
    }

    private fun hideOrShowMenu(){
        if(top_menu.visibility == View.VISIBLE && bottom_menu.visibility == View.VISIBLE){
            showOrHideSystemBar(true)
            top_menu.visibility = View.GONE
            bottom_menu.visibility = View.GONE
        }else{
            showOrHideSystemBar(false)
            top_menu.visibility = View.VISIBLE
            bottom_menu.visibility = View.VISIBLE
        }
    }

    private fun showOrHideSystemBar(isHide : Boolean) {
        if(isHide){
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN) //隐藏状态栏
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN) //显示状态栏
        }
    }


    @SuppressLint("SetTextI18n")
    override fun requestSuccess(data: ChaptersBean) {
        book_name.text = bookName
        chaptersList?.addAll(data.chapters.reversed())
        chaptersAdapter?.notifyDataSetChanged()
        chapter_sum.text = "共${chaptersList?.size}章"
        mPageLoader.initBook(data.chapters)
        if(chaptersList!!.isNotEmpty()) {
            mPresenter?.getContent(chaptersList!![0].id)
        }else{
            Toast.makeText(this,"没有找到这本书的章节",Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }

    override fun requestFailure() {

    }

    override fun onRequestChapterContentSuccess(data: ChapterContentBean) {
        val chapterContentList = mPageLoader.loadChapter(data.title,data.content)
        mPageLoader.openChapter(chapterContentList)
    }

    override fun onRequestChapterContentFailure() {
        Log.e("TAG","请求失败")
    }

    private fun showNightOrDayTime(isNight : Boolean){
        if(!isNight){
            nightOrDayTimeText.text = "日间"
            nightOrDayTimeImage.setBackgroundResource(R.drawable.baitian)
        }else{
            nightOrDayTimeText.text = "夜间"
            nightOrDayTimeImage.setBackgroundResource(R.drawable.heiye)
        }
    }

    override fun onDestroy() {
        mPageLoader.cloneBook()
        if(chaptersList != null){
            chaptersList = null
        }
        super.onDestroy()
    }

}
