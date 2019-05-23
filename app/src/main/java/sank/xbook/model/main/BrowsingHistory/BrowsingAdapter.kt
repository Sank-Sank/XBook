package sank.xbook.model.main.BrowsingHistory

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import sank.xbook.R
import sank.xbook.Utils.view.RecyclerView.adapter.CommonRecyclerAdapter
import sank.xbook.Utils.view.RecyclerView.adapter.ViewHolder
import sank.xbook.database.HistoryRecord

class BrowsingAdapter(private var contexts:Context,private var data:MutableList<HistoryRecord>): CommonRecyclerAdapter<HistoryRecord>(
        contexts, data, R.layout.book_mall_item_4
) {
    override fun convert(holder: ViewHolder?, item: HistoryRecord?) {
        holder?.getView<ImageView>(R.id.bookImage).let {
            Glide.with(contexts).load(item?.image).into(it!!)
        }
        holder?.getView<TextView>(R.id.bookName).let {
            it?.text = item?.bookName
        }
        holder?.getView<TextView>(R.id.bookAuthor).let {
            it?.text = item?.bookAuthor
        }
        holder?.getView<TextView>(R.id.bookType).let {
            it?.text = item?.bookType
        }
        holder?.getView<TextView>(R.id.bookJ).let {
            it?.text = item?.bookSynopsis
        }
    }
}