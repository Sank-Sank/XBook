package sank.xbook.model.main.cache

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sank.xbook.base.ChapterContentBean
import sank.xbook.base.ChaptersBean
import sank.xbook.base.MyApp
import sank.xbook.database.Books
import sank.xbook.database.Chapters
import sank.xbook.database.greendao.BooksDao
import sank.xbook.model.read_book.model.ChapterAPI
import sank.xbook.model.read_book.model.RequestChapterAPI

class CacheService : Service() {
    /**
     * 缓存多少章
     * 0 全部
     * 1 当前章节后面50章
     */
    private var flag = 0
    private var bookName: String? = null
    private var mThread: Thread? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        flag = intent.getIntExtra("flag", 0)
        //获取书籍
        bookName = intent.getStringExtra("bookName")
        bookName?.let { s ->
            mThread = object : Thread() {
                override fun run() {
                    val retrofit: Retrofit = Retrofit.Builder()
                            .baseUrl("http://www.xyxhome.cn/book/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpClient())
                            .build()
                    val api = retrofit.create(ChapterAPI::class.java)
                    val call = api.getChapters(s)
                    call.enqueue(object : Callback<ChaptersBean> {
                        override fun onResponse(call: Call<ChaptersBean>, response: Response<ChaptersBean>) {
                            response.body()?.let { x ->
                                x.chapters.reversed()
                                for(i in x.chapters) {
                                    val retrofit1 = Retrofit.Builder()
                                            .baseUrl("http://www.xyxhome.cn/book/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .client(OkHttpClient())
                                            .build()
                                    val api1 = retrofit1.create(RequestChapterAPI::class.java)
                                    val call1 = api1.getChapter(i.id)
                                    call1.enqueue(object : Callback<ChapterContentBean> {
                                        override fun onResponse(call: Call<ChapterContentBean>, response: Response<ChapterContentBean>) {
                                            response.body()?.let {
                                                val b1 = MyApp.getGreenDao().booksDao.queryBuilder().where(BooksDao.Properties.BookName.eq(s)).list()
                                                if(b1.isNotEmpty()) {
                                                    val c = Chapters(null,b1[0].id,it.title,it.content)
                                                    MyApp.getGreenDao().chaptersDao.insert(c)
                                                }
                                            }
                                        }
                                        override fun onFailure(call: Call<ChapterContentBean>, t: Throwable) {
                                            Log.e("TAG","请求书籍内容失败")
                                        }
                                    })
                                }
                                Toast.makeText(MyApp.getInstance(),"${s}缓存成功",Toast.LENGTH_SHORT).show()
                                stopSelf()
                            }
                        }
                        override fun onFailure(call: Call<ChaptersBean>, t: Throwable) {
                            Log.e("TAG","请求书籍章节失败")
                            stopSelf()
                        }
                    })
                }
            }
        }
        mThread?.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mThread?.let {
            mThread = null
        }
        super.onDestroy()
    }

}