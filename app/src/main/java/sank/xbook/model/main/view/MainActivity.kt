package sank.xbook.model.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.nineoldandroids.view.ViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sank.xbook.base.BaseActivity
import sank.xbook.base.IPresenter
import sank.xbook.base.IView
import sank.xbook.model.book_mall.BookMallFragment
import sank.xbook.model.book_rack.BookRackFragment
import sank.xbook.model.community.ClassifyFragment
import sank.xbook.model.main.presenter.MainPresenter
import sank.xbook.base.DrawerLayoutOpen
import sank.xbook.R


class MainActivity : BaseActivity() , IView, View.OnClickListener {
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
    private lateinit var fragment: RelativeLayout
    private lateinit var book_rack:LinearLayout
    private lateinit var book_mall:LinearLayout
    private lateinit var classify:LinearLayout

    /**
     * p曾引用
     */
    private var mainPresenter: IPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        mainPresenter = MainPresenter(this)
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
        classify = findViewById(R.id.classify)    //社区
        classify.setOnClickListener(this)


        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
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
    }

    /**
     * 侧滑菜单动画
     * slideOffset:示菜单项滑出来的比例，打开菜单时取值为0->1,关闭菜单时取值为1->0
     */
    private fun slideAnim(drawerView:View,slideOffset:Float){
        val contentView = drawerLayout.getChildAt(0)

        val scale = 1 - slideOffset
        val rightScale = 1F//0.8f + scale * 0.2f
        val leftScale = 1F// - 0.3f * scale

        ViewHelper.setScaleX(drawerView, leftScale)
        ViewHelper.setScaleY(drawerView, leftScale)
        ViewHelper.setAlpha(drawerView, 0.6f + 0.4f * (1 - scale))
        ViewHelper.setTranslationX(contentView, drawerView.measuredWidth * (1 - scale))
        ViewHelper.setPivotX(contentView, 0F)
        ViewHelper.setPivotY(contentView, (contentView.measuredHeight / 2).toFloat())
        contentView.invalidate()
        ViewHelper.setScaleX(contentView, rightScale)
        ViewHelper.setScaleY(contentView, rightScale)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun drawerLayoutMessage(message : DrawerLayoutOpen){
        if(!drawerLayout.isDrawerOpen(main_left)) {
            drawerLayout.openDrawer(main_left)
        }else{
            drawerLayout.closeDrawer(main_left)
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

    override fun onSuccess() {

    }

    override fun onFailure() {

    }

    override fun onDestroy() {
        if(mainPresenter != null){
            mainPresenter = null
        }
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
