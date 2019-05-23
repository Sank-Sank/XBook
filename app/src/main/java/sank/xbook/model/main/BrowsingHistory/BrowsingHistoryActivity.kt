package sank.xbook.model.main.BrowsingHistory

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import sank.xbook.R
import sank.xbook.base.MyApp
import sank.xbook.database.HistoryRecord
import android.view.Gravity
import android.view.ViewGroup


class BrowsingHistoryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var back:ImageView
    private lateinit var menu:ImageView
    private lateinit var HRecyclerView: RecyclerView
    private lateinit var noData:TextView
    private var browsingAdapter:BrowsingAdapter? = null
    private var data:MutableList<HistoryRecord>? = null
    //数据库中所有历史记录的集合
    private lateinit var bookList:List<HistoryRecord>
    //每页20条数据，共有多少条
    private var count = 0
    //当前是第几页的数据
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_browsing_history)
        initAll()
        initdata()
    }

    private fun initAll(){
        back = findViewById(R.id.back)
        back.setOnClickListener(this)
        menu = findViewById(R.id.menu)
        menu.setOnClickListener(this)
        noData = findViewById(R.id.noData)
        HRecyclerView = findViewById(R.id.HRecyclerView)
        HRecyclerView.layoutManager = LinearLayoutManager(this)
        HRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        data = ArrayList()
        browsingAdapter = BrowsingAdapter(this,data!!)
        HRecyclerView.adapter = browsingAdapter
    }

    private fun initdata(){
        bookList = MyApp.getGreenDao().historyRecordDao.loadAll()
        if(bookList.isNotEmpty()){
            HRecyclerView.visibility = View.VISIBLE
            noData.visibility = View.GONE
            bookList.reversed()
            if(bookList.size <= 20){
                data?.addAll(bookList)
                browsingAdapter?.notifyDataSetChanged()
            }else{
                for(i in 1..20){
                    data?.add(bookList[i])
                    browsingAdapter?.notifyDataSetChanged()
                }
            }
        }else{
            HRecyclerView.visibility = View.GONE
            noData.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back -> {this@BrowsingHistoryActivity.finish()}
            R.id.menu -> {
                val dialog = Dialog(this,R.style.DialogTheme)
                val view = View.inflate(this,R.layout.custom_dialog_layout,null)
                dialog.setContentView(view)
                val window = dialog.window
                //设置弹出位置
                window?.setGravity(Gravity.BOTTOM)
                //设置弹出动画
                window?.setWindowAnimations(R.style.main_menu_animStyle)
                //设置对话框大小
                window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog.show()
                dialog.findViewById<TextView>(R.id.clear).setOnClickListener {
                    data?.clear()
                    browsingAdapter?.notifyDataSetChanged()
                    HRecyclerView.visibility = View.GONE
                    noData.visibility = View.VISIBLE
                    MyApp.getGreenDao().historyRecordDao.deleteAll()
                    dialog.dismiss()
                }
                dialog.findViewById<TextView>(R.id.cancel).setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
    }

}
