package sank.xbook.model.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sank.xbook.base.BaseActivity
import sank.xbook.model.book_mall.BookMallFragment
import sank.xbook.model.book_rack.veiw.BookRackFragment
import sank.xbook.model.classify.ClassifyFragment
import sank.xbook.base.DrawerLayoutOpen
import sank.xbook.R
import com.nineoldandroids.view.ViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.Utils.SPUtils
import sank.xbook.base.UserLogOut
import sank.xbook.base.UserLoginInfo
import sank.xbook.model.main.BrowsingHistory.BrowsingHistoryActivity
import sank.xbook.model.main.cache.CacheActivity
import sank.xbook.model.user.login.LoginActivity


class MainActivity : BaseActivity<MainPresenter, MainPresenter.IMainView>() , View.OnClickListener {
    /**
     * 侧滑布局
     */
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var main_left: LinearLayout
    /**
     * fragment
     */
    private lateinit var fm:FragmentManager
    private lateinit var bookRackFragment: BookRackFragment
    private lateinit var bookMallFragment: BookMallFragment
    private lateinit var classifyFragment: ClassifyFragment
    /**
     * 布局
     */
    private lateinit var fragment: FrameLayout
    private lateinit var book_rack:LinearLayout
    private lateinit var book_mall:LinearLayout
    private lateinit var classify:LinearLayout
    /**
     * 侧滑菜单的布局
     */
    private lateinit var login:TextView
    private lateinit var logOut:TextView
    private lateinit var about:ImageView
    private lateinit var setting:ImageView
    private lateinit var leftMenuRecycler:RecyclerView
    private var adapter : leftMenuAdapter? = null
    private var leftMenuList:MutableList<String> = ArrayList()
    //判断用户是否已经登录
    private var isLogin = false

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        /**
         *初始化布局
         */
        initView()
        /**
         * 添加Fragment
         */
        addFragment()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(){
        drawerLayout = findViewById(R.id.drawerLayout)
        main_left = findViewById(R.id.main_left)
        fragment = findViewById(R.id.fragment)      //fragment容器
        book_rack = findViewById(R.id.book_rack)    //书架
        book_rack.setOnClickListener(this)
        book_rack.isSelected = true
        book_mall = findViewById(R.id.book_mall)    //书城
        book_mall.setOnClickListener(this)
        classify = findViewById(R.id.classify)    //分类
        classify.setOnClickListener(this)
        login = findViewById(R.id.login)        //登录
        logOut = findViewById(R.id.logOut)        //退出登录
        about = findViewById(R.id.about)        //关于
        setting = findViewById(R.id.setting)        //设置
        //侧滑菜单的recycler
        leftMenuRecycler = findViewById(R.id.leftMenuRecycler)
        //侧滑动画
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(p0: Int) {

            }

            override fun onDrawerSlide(p0: View, p1: Float) {
                slideAnim(p0,p1)
            }

            override fun onDrawerClosed(p0: View) {

            }

            override fun onDrawerOpened(p0: View) {

            }
        })
        //为左侧菜单添加数据
        leftMenuList.add("浏览记录")
        leftMenuList.add("缓存管理")
        leftMenuRecycler.layoutManager = LinearLayoutManager(this)
        leftMenuRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter = leftMenuAdapter(this, leftMenuList, object : OnItemClickListeners {
            override fun onItemClicked(view: View, position: Int) {
                when (position) {
                    0 -> {      //浏览记录
                        this@MainActivity.startActivity(Intent(this@MainActivity, BrowsingHistoryActivity::class.java))
                    }
                    1 -> {      //缓存管理
                        this@MainActivity.startActivity(Intent(this@MainActivity, CacheActivity::class.java))
                    }
                }
            }
        })
        leftMenuRecycler.adapter = adapter

        login.setOnClickListener {
            this@MainActivity.startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
        about.setOnClickListener {
            this@MainActivity.startActivity(Intent(this@MainActivity,AboutActivity::class.java))
        }
        setting.setOnClickListener {
            this@MainActivity.startActivity(Intent(this@MainActivity,SettingActivity::class.java))
        }
        logOut.setOnClickListener {
            logOut.visibility = View.GONE
            login.text = "登录/注册"
            SPUtils.clear(this)
            EventBus.getDefault().post(UserLogOut())
        }
    }

    private fun slideAnim(drawerView: View, slideOffset: Float) {
        val contentView = drawerLayout.getChildAt(0)
        val scale = 1 - slideOffset
        ViewHelper.setScaleX(drawerView, 1F)
        ViewHelper.setScaleY(drawerView, 1F)
        ViewHelper.setAlpha(drawerView, 0.6f + 0.4f * (1 - scale))
        ViewHelper.setTranslationX(contentView, drawerView.measuredWidth * (1 - scale))
        ViewHelper.setPivotX(contentView, 0f)
        ViewHelper.setPivotY(contentView, (contentView.measuredHeight / 2).toFloat())
        contentView.invalidate()
        ViewHelper.setScaleX(contentView, 1F)
        ViewHelper.setScaleY(contentView, 1F)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun drawerLayoutMessage(message : DrawerLayoutOpen){
        if(drawerLayout.isDrawerOpen(main_left)) {
            drawerLayout.closeDrawer(main_left)
        }else{
            drawerLayout.openDrawer(main_left)
        }
    }

    /**
     * 用户登录后，广播
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun UserLoginInfo(message: UserLoginInfo){
        logOut.visibility = View.VISIBLE
        val name = SPUtils.getUserInfo(this,"name")
        login.text = name
    }

    override fun onStart() {
        super.onStart()
        isLogin = SPUtils.getUserIsLogin(this,"login")
        if(isLogin){
            logOut.visibility = View.VISIBLE
            val name = SPUtils.getUserInfo(this,"name")
            login.text = name
        }else{
            logOut.visibility = View.GONE
            login.text = "登录/注册"
        }
    }

    private fun addFragment() {
        fm = supportFragmentManager
        val ft = fm.beginTransaction()
        bookRackFragment = BookRackFragment()
        bookMallFragment = BookMallFragment()
        classifyFragment = ClassifyFragment()
        ft.add(R.id.fragment, bookRackFragment)
        ft.add(R.id.fragment, bookMallFragment)
        ft.add(R.id.fragment, classifyFragment)
        ft.show(bookRackFragment)
        ft.hide(bookMallFragment)
        ft.hide(classifyFragment)
        ft.commit()
    }

    override fun onClick(v: View?) {
        val ft = fm.beginTransaction()
        when(v!!.id){
            R.id.book_rack -> {
                ft.show(bookRackFragment)
                ft.hide(bookMallFragment)
                ft.hide(classifyFragment)
                book_rack_text.setTextColor(resources.getColor(R.color.colorPrimary))
                book_mall_text.setTextColor(resources.getColor(R.color.font_color))
                classify_text.setTextColor(resources.getColor(R.color.font_color))
                book_rack.isSelected = true
                book_mall.isSelected = false
                classify.isSelected = false
            }
            R.id.book_mall -> {
                ft.show(bookMallFragment)
                ft.hide(bookRackFragment)
                ft.hide(classifyFragment)
                book_rack_text.setTextColor(resources.getColor(R.color.font_color))
                book_mall_text.setTextColor(resources.getColor(R.color.colorPrimary))
                classify_text.setTextColor(resources.getColor(R.color.font_color))
                book_mall.isSelected = true
                book_rack.isSelected = false
                classify.isSelected = false
            }
            R.id.classify -> {
                ft.show(classifyFragment)
                ft.hide(bookRackFragment)
                ft.hide(bookMallFragment)
                book_rack_text.setTextColor(resources.getColor(R.color.font_color))
                book_mall_text.setTextColor(resources.getColor(R.color.font_color))
                classify_text.setTextColor(resources.getColor(R.color.colorPrimary))
                classify.isSelected = true
                book_mall.isSelected = false
                book_rack.isSelected = false
            }
        }
        ft.commit()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
