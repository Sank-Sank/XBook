package sank.xbook.model.classify.Details

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import sank.xbook.R
import sank.xbook.Utils.view.RecyclerView.adapter.CommonRecyclerAdapter
import sank.xbook.Utils.view.RecyclerView.adapter.ViewHolder
import sank.xbook.base.TypeBean2

class ClassifyDetailsAdapter(private var context: Context,private var data:MutableList<TypeBean2>) : CommonRecyclerAdapter<TypeBean2>(
        context, data, R.layout.book_mall_item_4
){
    override fun convert(holder: ViewHolder?, item: TypeBean2?) {
        holder?.getView<ImageView>(R.id.bookImage).let {
            Glide.with(context).load(item?.fields?.book_image).into(it!!)
        }
        holder?.getView<TextView>(R.id.bookName).let {
            it?.text = item?.fields?.book_name
        }
        holder?.getView<TextView>(R.id.bookAuthor).let {
            it?.text = item?.fields?.book_author
        }
        holder?.getView<TextView>(R.id.bookType).let {
            it?.text = item?.fields?.book_type
        }
        holder?.getView<TextView>(R.id.bookJ).let {
            it?.text = item?.fields?.book_synopsis
        }
    }
}