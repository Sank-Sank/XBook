package sank.xbook.model.book_mall

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import sank.xbook.R
import sank.xbook.base.BookBean
import sank.xbook.base.BookMallBean2
import sank.xbook.model.prepare_book.PrepareActivity

class BookMallAdapter(private var contexts: Context,private var data:MutableList<BookMallBean2>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mInflate: LayoutInflater = LayoutInflater.from(contexts)
    private lateinit var imageData:MutableList<ImageView>   //图片集合
    private var prePosition = 0     //第一个高亮显示的点

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when (p1) {
            0 -> ViewPageHolder(mInflate.inflate(R.layout.book_mall_item_0,p0,false))
            1 -> KuaiZhuangHolder(mInflate.inflate(R.layout.book_mall_item_1,null,false))
            else -> ShuZhuangHolder(mInflate.inflate(R.layout.book_mall_item_2,null,false))
        }
    }

    override fun getItemCount(): Int = data.size

    /**
     *  0 ： viewPager
     *  1 ： 块状布局
     *  2 ： 树状布局
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            data[position].tag == 0 -> 0
            data[position].tag == 1 -> 1
            else -> 2
        }
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        when(p0){
            is ViewPageHolder -> {
                imageData = ArrayList()
                data[p1].imageList?.let {
                    for (i in it.indices){
                        val imageView = ImageView(contexts)
                        imageView.scaleType = ImageView.ScaleType.FIT_XY
                        Glide.with(contexts).load(it[i]).into(imageView)
                        imageData.add(imageView)
                        //添加点
                        val point = ImageView(contexts)
                        val params = LinearLayout.LayoutParams(14,14)
                        point.setBackgroundResource(R.drawable.mall_point_selector)
                        if(i==0){
                            point.isEnabled = true  //将第一个设置成白色
                        }else{
                            point.isEnabled = false
                            params.leftMargin = 14
                        }
                        point.layoutParams = params
                        p0.ll_point.addView(point)
                    }
                }
                p0.viewPage.adapter = object : PagerAdapter(){
                    override fun instantiateItem(container: ViewGroup, position: Int): Any {
                        //对数组取模 防止下标越界
                        val rPosition = position % imageData.size
                        val imageView:ImageView = imageData[rPosition]
                        container.addView(imageView)
                        return imageView
                    }
                    override fun isViewFromObject(p0: View, p1: Any): Boolean {
                        return p0 == p1
                    }

                    override fun getCount(): Int = Int.MAX_VALUE

                    override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
                        container.removeView(o as View)
                    }
                }
                p0.viewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                    override fun onPageScrollStateChanged(p0: Int) {

                    }

                    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                    }

                    override fun onPageSelected(p1: Int) {
                        //将上一个点设置成透明
                        val rPosition = p1%imageData.size
                        p0.ll_point.getChildAt(prePosition).isEnabled = false
                        p0.ll_point.getChildAt(rPosition).isEnabled = true
                        prePosition = rPosition
                    }
                })
                //设置中间位置 无线滑动
                val item = Int.MAX_VALUE/2 - Int.MAX_VALUE/2 % imageData.size
                p0.viewPage.currentItem = item
            }
            is KuaiZhuangHolder -> {
                p0.KRecyclerView.layoutManager = GridLayoutManager(contexts, 4)
                p0.KRecyclerView.adapter = KuaiZhuangAdapter(contexts,data[p1].bookList!!)
            }
            is ShuZhuangHolder -> {
                p0.SRecyclerView.layoutManager = LinearLayoutManager(contexts)
                p0.SRecyclerView.adapter = ShuZhuangAdapter(contexts,data[p1].bookList!!)
            }
        }
    }

    inner class ViewPageHolder(view: View) : RecyclerView.ViewHolder(view){
        var viewPage:ViewPager = view.findViewById(R.id.viewPage)
        var ll_point:LinearLayout = view.findViewById(R.id.ll_point)
    }

    inner class KuaiZhuangHolder(view:View) : RecyclerView.ViewHolder(view){
        var KRecyclerView:RecyclerView = view.findViewById(R.id.KRecyclerView)
    }

    inner class ShuZhuangHolder(view:View) : RecyclerView.ViewHolder(view){
        var SRecyclerView:RecyclerView = view.findViewById(R.id.SRecyclerView)
    }
}

class KuaiZhuangAdapter(private var contexts: Context,private var data:List<BookBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder =
            KHolder(LayoutInflater.from(contexts).inflate(R.layout.book_mall_item_3,p0,false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        with(p0 as KHolder){
            Glide.with(contexts).load(data[p1].book_image).into(bookImage)
            bookName.text = data[p1].bookname
            bookAuthor.text = data[p1].book_author
            item.setOnClickListener{
                val intent = Intent(contexts,PrepareActivity::class.java)
                val b = Bundle()
                b.putSerializable("book",data[p1])
                intent.putExtra("bundle1",b)
                contexts.startActivity(intent)
            }
        }
    }

    inner class KHolder(view:View):RecyclerView.ViewHolder(view){
        var item:LinearLayout = view.findViewById(R.id.item)
        var bookImage:ImageView = view.findViewById(R.id.bookImage)
        var bookName:TextView = view.findViewById(R.id.bookName)
        var bookAuthor:TextView = view.findViewById(R.id.bookAuthor)
    }
}

class ShuZhuangAdapter(private var contexts: Context,private var data:List<BookBean>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder =
            SHolder(LayoutInflater.from(contexts).inflate(R.layout.book_mall_item_4,p0,false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        with(p0 as SHolder){
            Glide.with(contexts).load(data[p1].book_image).into(bookImage)
            bookName.text = data[p1].bookname
            bookJ.text = data[p1].book_synopsis
            bookAuthor.text = data[p1].book_author
            bookType.text = data[p1].booktype
            item.setOnClickListener{
                val intent = Intent(contexts,PrepareActivity::class.java)
                val b = Bundle()
                b.putSerializable("book",data[p1])
                intent.putExtra("bundle1",b)
                contexts.startActivity(intent)
            }
        }
    }

    inner class SHolder(view:View):RecyclerView.ViewHolder(view){
        var item:LinearLayout = view.findViewById(R.id.item)
        var bookImage:ImageView = view.findViewById(R.id.bookImage)
        var bookName:TextView = view.findViewById(R.id.bookName)
        var bookJ:TextView = view.findViewById(R.id.bookJ)
        var bookAuthor:TextView = view.findViewById(R.id.bookAuthor)
        var bookType:TextView = view.findViewById(R.id.bookType)
    }
}