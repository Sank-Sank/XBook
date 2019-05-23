package sank.xbook.model.prepare_book

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import sank.xbook.R
import sank.xbook.Utils.SPUtils
import sank.xbook.Utils.Utils
import sank.xbook.base.BaseActivity
import sank.xbook.base.BookBean
import sank.xbook.base.MyApp
import sank.xbook.database.Books
import sank.xbook.database.HistoryRecord
import sank.xbook.database.greendao.BooksDao
import sank.xbook.model.main.cache.CacheService
import sank.xbook.model.read_book.view.ReadActivity
import java.util.*

class PrepareActivity : BaseActivity<PreparePresenter, PreparePresenter.IPrepareView>(), View.OnClickListener, PreparePresenter.IPrepareView {
    override fun createPresenter(): PreparePresenter = PreparePresenter(this)

    private lateinit var bookImage:ImageView
    private lateinit var back:ImageView
    private lateinit var bookName:TextView
    private lateinit var bookAuthor:TextView
    private lateinit var bookType:TextView
    private lateinit var bookJAN:TextView
    private lateinit var readBook:TextView
    private lateinit var cache:LinearLayout
    private lateinit var addRack:LinearLayout
    private var book:BookBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare)
        initAll()
        setData()
        addDataBase()
    }

    private fun initAll(){
        val bundleExtra = intent.getBundleExtra("bundle1")
        book = bundleExtra.getSerializable("book") as BookBean?
        bookImage = findViewById(R.id.bookImage)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)
        bookName = findViewById(R.id.bookName)
        bookAuthor = findViewById(R.id.bookAuthor)
        bookType = findViewById(R.id.bookType)
        bookJAN = findViewById(R.id.bookJAN)
        readBook = findViewById(R.id.readBook)
        readBook.setOnClickListener(this)
        cache = findViewById(R.id.cache)
        cache.setOnClickListener(this)
        addRack = findViewById(R.id.addRack)
        addRack.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        Glide.with(this).load(book?.book_image).into(bookImage)
        bookName.text = book?.bookname
        bookAuthor.text = "作者:${book?.book_author}"
        bookType.text = "类型:${book?.booktype}"
        bookJAN.text = "    ${book?.book_synopsis}"
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back -> {
                this@PrepareActivity.finish()
            }
            R.id.readBook -> {
                startActivity(Intent(this@PrepareActivity, ReadActivity::class.java).apply {
                    putExtra("bookName",book?.bookname)
                })
            }
            R.id.cache -> {
                if(SPUtils.getUserIsLogin(this,"login")){
                    val account = SPUtils.getUserInfo(this,"account")
                    if(account!=null){
                        mPresenter?.fetch(
                                account,book?.bookname!!,book?.book_author!!,book?.book_synopsis!!,book?.book_image!!,book?.update_time!!
                        )
                        val b = Books(
                                null,book?.bookname,book?.booktype,book?.book_author,book?.book_synopsis,book?.book_image,book?.update_time
                        )
                        if(MyApp.getGreenDao().booksDao.queryBuilder().where(BooksDao.Properties.BookName.eq(book?.bookname)).list().isEmpty()){
                            MyApp.getGreenDao().booksDao.insert(b)
                        }
                        //开启缓存服务
                        startService(Intent(this, CacheService::class.java).apply {
                            putExtra("bookName",book?.bookname)
                        })
                    }else{
                        Toast.makeText(this,"请重试~",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.addRack -> {
                if(SPUtils.getUserIsLogin(this,"login")){
                    val account = SPUtils.getUserInfo(this,"account")
                    if(account!=null){
                        mPresenter?.fetch(
                                account,book?.bookname!!,book?.book_author!!,book?.book_synopsis!!,book?.book_image!!,book?.update_time!!
                        )
                        if(MyApp.getGreenDao().booksDao.queryBuilder().where(BooksDao.Properties.BookName.eq(book?.bookname)).list().isEmpty()) {
                            val b = Books(
                                    null, book?.bookname, book?.booktype, book?.book_author, book?.book_synopsis, book?.book_image, book?.update_time
                            )
                            MyApp.getGreenDao().booksDao.insert(b)
                        }
                    }else{
                        Toast.makeText(this,"请重试~",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onAddSuccess(data : PrepareBean) {
        Toast.makeText(this,data.message,Toast.LENGTH_SHORT).show()
    }

    override fun onAddFailure() {
        Toast.makeText(this,"添加失败，请检查网络链接",Toast.LENGTH_SHORT).show()
    }

    private fun addDataBase(){
        val historyRecord = HistoryRecord(
                null,
                Utils.dateConvert(System.currentTimeMillis()),
                book?.book_image,
                book?.bookname,
                book?.book_author,
                book?.booktype,
                book?.book_synopsis,
                book?.update_time
        )
        MyApp.getGreenDao().historyRecordDao.insert(historyRecord)
    }

}
