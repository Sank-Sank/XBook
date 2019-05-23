package sank.xbook.model.book_rack.veiw

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import sank.xbook.Utils.OnItemClickListeners
import sank.xbook.R
import sank.xbook.base.BookRackBean2

/**
 *  @description
 *  @Author Sank
 *  @Time 2019/3/14
 */
class BookRockAdapter (private var contexts:Context,
                       private var data:MutableList<BookRackBean2>,
                       private var onItemClickListener: OnItemClickListeners)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var mInflate: LayoutInflater = LayoutInflater.from(contexts)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder =
            BookRockHolder(mInflate.inflate(R.layout.item_book_rock_layout, p0, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        with(p0 as BookRockHolder){
            bookItem.setOnClickListener { v ->  onItemClickListener.onItemClicked(v,p1)}
            Glide.with(contexts).load(data[p1].image).into(bookImage)
            bookName.text = data[p1].name
            bookAuthor.text = data[p1].author
            updateTime.text = data[p1].update_time
        }
    }

    inner class BookRockHolder(view:View) : RecyclerView.ViewHolder(view){
        var bookItem:LinearLayout = view.findViewById(R.id.bookItem)
        var bookImage:ImageView = view.findViewById(R.id.bookImage)
        var bookName:TextView = view.findViewById(R.id.bookName)
        var bookAuthor:TextView = view.findViewById(R.id.bookAuthor)
        var updateTime:TextView = view.findViewById(R.id.updateTime)
    }
}