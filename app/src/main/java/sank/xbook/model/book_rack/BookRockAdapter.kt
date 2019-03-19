package sank.xbook.model.book_rack

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.R

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/14
 */
class BookRockAdapter (private var contexts:Context,
                       private var data:MutableList<String> =  ArrayList(),
                       private var onItemClickListener: OnItemClickListeners)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var mInflate: LayoutInflater = LayoutInflater.from(contexts)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder =
            BookRockHolder(mInflate.inflate(R.layout.item_book_rock_layout, p0, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val book = p0 as BookRockHolder
        book.bookName.text = data[p1]
        book.bookItem.setOnClickListener { v ->  onItemClickListener.onItemClicked(v,p1)}
    }

    inner class BookRockHolder(view:View) : RecyclerView.ViewHolder(view){
        var bookItem:LinearLayout = view.findViewById(R.id.bookItem)
        var bookName: TextView = view.findViewById(R.id.bookName)
    }
}