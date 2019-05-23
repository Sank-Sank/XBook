package sank.xbook.model.book_rack.veiw

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.R
import sank.xbook.Utils.SPUtils
import sank.xbook.Utils.Utils
import sank.xbook.base.*
import sank.xbook.database.Books
import sank.xbook.database.greendao.BooksDao
import sank.xbook.model.book_rack.presenter.BookRackPresenter
import sank.xbook.model.read_book.view.ReadActivity
import sank.xbook.model.search_book.SearchActivity
import sank.xbook.model.user.login.LoginActivity

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/13
 */


class BookRackFragment : BaseFragment<BookRackPresenter, BookRackPresenter.IBookRackView>(), BookRackPresenter.IBookRackView {
    private lateinit var views: View
    private lateinit var menu: ImageView
    private lateinit var SearchBook: ImageView
    private lateinit var bookRockRecycler: RecyclerView
    private lateinit var login: TextView

    private var isLogin: Boolean = false         //是否已经登录
    private var bookItem: MutableList<BookRackBean2> = ArrayList()
    private lateinit var bookRockAdapter: BookRockAdapter

    override fun createPresenter(): BookRackPresenter = BookRackPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        views = inflater.inflate(R.layout.framnent_book_rack, container, false)
        initView()
        return views
    }

    private fun initView() {
        menu = views.findViewById(R.id.menu)
        SearchBook = views.findViewById(R.id.SearchBook)
        login = views.findViewById(R.id.login)

        bookRockRecycler = views.findViewById(R.id.bookRockRecycler)
        bookRockRecycler.layoutManager = LinearLayoutManager(contexts)
        bookRockRecycler.addItemDecoration(DividerItemDecoration(contexts, DividerItemDecoration.VERTICAL))
        bookRockAdapter = BookRockAdapter(contexts!!, bookItem, object : OnItemClickListeners {
            override fun onItemClicked(view: View, position: Int) {
                startActivity(Intent(contexts, ReadActivity::class.java).apply {
                    putExtra("bookName", bookItem[position].name)
                })
            }
        })
        bookRockRecycler.adapter = bookRockAdapter

        menu.setOnClickListener {
            EventBus.getDefault().post(DrawerLayoutOpen())
        }
        SearchBook.setOnClickListener {
            contexts?.startActivity(Intent(contexts, SearchActivity::class.java))
        }
        login.setOnClickListener {
            contexts?.startActivity(Intent(contexts, LoginActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        isLogin = SPUtils.getUserIsLogin(contexts!!, "login")
        if (isLogin) {
            bookRockRecycler.visibility = View.VISIBLE
            login.visibility = View.GONE
            //TODO 是否要加入第一次登录的判断，防止多用户显示同样的书籍
            if (Utils.isNetworkAvailable(contexts!!)) {
                Log.e("TAG","判断有网络")
                SPUtils.getUserInfo(contexts!!, "account")?.let {
                    mPresenter?.fetch(it)
                }
            } else {
                Log.e("TAG","判断没有网络")
                val books = MyApp.getGreenDao().booksDao.loadAll()
                if (books.isNotEmpty()) {
                    for (i in books) {
                        val b = BookRackBean2(
                                i.bookName, i.bookAuthor, i.bookSynopsis, i.bookImage, i.updateTime
                        )
                        bookItem.add(b)
                    }
                    bookRockAdapter.notifyDataSetChanged()
                }
            }
        } else {
            bookRockRecycler.visibility = View.GONE
            login.visibility = View.VISIBLE
        }
    }

    /**
     * 退出登录的广播接收方法
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun userLogOut(message: UserLogOut) {
        bookRockRecycler.visibility = View.GONE
        login.visibility = View.VISIBLE
    }

    override fun onSuccess(data: BookRackBean1) {
        Log.e("TAG","请求成功")
        data.books?.let {
            if (it.isNotEmpty()) {
                for (i in it) {
                    if (MyApp.getGreenDao().booksDao.queryBuilder().where(BooksDao.Properties.BookName.eq(i.name)).list().isEmpty()) {
                        val b = Books(
                                null, i.name, "玄幻小说", i.author, i.synopsis, i.image, i.update_time
                        )
                        MyApp.getGreenDao().booksDao.insert(b)
                    }
                }
                val books = MyApp.getGreenDao().booksDao.loadAll()
                if (books.isNotEmpty()) {
                    for (z in books) {
                        val b = BookRackBean2(
                                z.bookName, z.bookAuthor, z.bookSynopsis, z.bookImage, z.updateTime
                        )
                        bookItem.add(b)
                    }
                }
                bookRockAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onStop() {
        bookItem.clear()
        bookRockAdapter.notifyDataSetChanged()
        super.onStop()
    }

    override fun onFailure() {
        Log.e("TAG","请求失败")
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

}