package sank.xbook.model.classify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.greenrobot.eventbus.EventBus
import sank.xbook.R
import sank.xbook.base.DrawerLayoutOpen
import sank.xbook.model.classify.Details.ClassifyDetailsActivity
import sank.xbook.model.search_book.SearchActivity

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/13
 */
class ClassifyFragment : Fragment(), View.OnClickListener {
    private var contexts:Context? = null

    private lateinit var views:View
    private lateinit var menu:ImageView
    private lateinit var SearchBook:ImageView
    private lateinit var xuanhuan:TextView
    private lateinit var xiuzhen:TextView
    private lateinit var dushi:TextView
    private lateinit var chuanyue:TextView
    private lateinit var wangyou:TextView
    private lateinit var kehuan:TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.framnent_classify,container,false)
        contexts = context
        initView()
        return views
    }

    private fun initView(){
        menu = views.findViewById(R.id.menu)
        menu.setOnClickListener(this)
        SearchBook = views.findViewById(R.id.SearchBook)
        SearchBook.setOnClickListener(this)
        xuanhuan = views.findViewById(R.id.xuanhuan)
        xiuzhen = views.findViewById(R.id.xiuzhen)
        dushi = views.findViewById(R.id.dushi)
        chuanyue = views.findViewById(R.id.chuanyue)
        wangyou = views.findViewById(R.id.wangyou)
        kehuan = views.findViewById(R.id.kehuan)
        xuanhuan.setOnClickListener(this)
        xiuzhen.setOnClickListener(this)
        dushi.setOnClickListener(this)
        chuanyue.setOnClickListener(this)
        wangyou.setOnClickListener(this)
        kehuan.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.menu -> {
                EventBus.getDefault().post(DrawerLayoutOpen())
            }
            R.id.SearchBook -> {
                contexts?.startActivity(Intent(contexts, SearchActivity::class.java))
            }
            R.id.xuanhuan -> {      //玄幻
                startActivity(Intent(contexts, ClassifyDetailsActivity::class.java).apply {
                    putExtra("classifyName","玄幻小说")
                })
            }
            R.id.xiuzhen -> {       //修真
                startActivity(Intent(contexts, ClassifyDetailsActivity::class.java).apply {
                    putExtra("classifyName","修真小说")
                })
            }
            R.id.dushi -> {         //都市
                startActivity(Intent(contexts, ClassifyDetailsActivity::class.java).apply {
                    putExtra("classifyName","都市小说")
                })
            }
            R.id.chuanyue -> {      //穿越
                startActivity(Intent(contexts, ClassifyDetailsActivity::class.java).apply {
                    putExtra("classifyName","穿越小说")
                })
            }
            R.id.wangyou -> {       //网游
                startActivity(Intent(contexts, ClassifyDetailsActivity::class.java).apply {
                    putExtra("classifyName","网游小说")
                })
            }
            R.id.kehuan -> {        //科幻
                startActivity(Intent(contexts, ClassifyDetailsActivity::class.java).apply {
                    putExtra("classifyName","科幻小说")
                })
            }
        }
    }

    override fun onDestroyView() {
        if(contexts!=null){
            contexts = null
        }
        super.onDestroyView()
    }
}