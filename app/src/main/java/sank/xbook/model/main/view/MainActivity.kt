package sank.xbook.model.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
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
import sank.xbook.Utils.view.ResideLayout


class MainActivity : BaseActivity() , IView, View.OnClickListener {
    /**
     * 侧滑布局
     */
    private lateinit var drawerLayout: ResideLayout
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

    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun drawerLayoutMessage(message : DrawerLayoutOpen){
        if(!drawerLayout.isOpen) {
            drawerLayout.openPane()
        }else{
            drawerLayout.closePane()
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


//    fun onBackPressed() {
//        if (mResideLayout.isOpen()) {
//            mResideLayout.closePane()
//        } else {
//            val secondTime = System.currentTimeMillis()
//            if (secondTime - fristTime < 2000) {
//                finish()
//            } else {
//                SnackBarUtils.makeShort(window.decorView, "再点击一次退出应用").show()
//                fristTime = System.currentTimeMillis()
//            }
//        }
//    }

    override fun onDestroy() {
        if(mainPresenter != null){
            mainPresenter = null
        }
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
